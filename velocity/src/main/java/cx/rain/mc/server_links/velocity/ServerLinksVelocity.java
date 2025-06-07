package cx.rain.mc.server_links.velocity;

import com.google.inject.Inject;
import com.velocitypowered.api.event.EventTask;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.PostLoginEvent;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.proxy.ProxyReloadEvent;
import com.velocitypowered.api.network.ProtocolVersion;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import cx.rain.mc.server_links.velocity.config.Config;
import org.slf4j.Logger;
import org.spongepowered.configurate.hocon.HoconConfigurationLoader;

import java.nio.file.Path;

@Plugin(id = "serverlinks",
        name = "Server Links",
        version = "1.1.0",
        url = "https://github.com/qyl27/ServerLinks",
        authors = "qyl27")
public class ServerLinksVelocity {
    private final ProxyServer server;
    private final Logger logger;
    private final Path dataDir;

    private Config config;

    @Inject
    public ServerLinksVelocity(ProxyServer server, Logger logger, @DataDirectory Path dataDir) {
        this.server = server;
        this.logger = logger;
        this.dataDir = dataDir;
    }

    @Subscribe
    private void onProxyInit(ProxyInitializeEvent event) {
        loadConfig();
    }

    @Subscribe
    private void onProxyReload(ProxyReloadEvent event) {
        loadConfig();
        server.getAllPlayers().forEach(this::sendServerLinks);
    }

    private void loadConfig() {
        var configFile = dataDir.resolve("config.conf").toFile();

        var configLoader = HoconConfigurationLoader.builder()
                .file(configFile)
                .build();
        try {
            if (!configFile.exists()) {
                var root = configLoader.createNode().set(Config.DEFAULT_CONFIG);
                configLoader.save(root);
                config = root.get(Config.class);
            } else {
                var root = configLoader.load();
                config = root.get(Config.class);
            }
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    @Subscribe
    public EventTask onPlayerLoggedIn(PostLoginEvent event) {
        return EventTask.async(() -> {
            var player = event.getPlayer();
            sendServerLinks(player);
        });
    }

    private void sendServerLinks(Player player) {
        if (player.getProtocolVersion().getProtocol() >= ProtocolVersion.MINECRAFT_1_21.getProtocol()) {
            player.setServerLinks(config.getServerLinks());
        }
    }
}
