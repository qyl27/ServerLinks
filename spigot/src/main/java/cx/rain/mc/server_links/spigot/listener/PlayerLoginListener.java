package cx.rain.mc.server_links.spigot.listener;

import cx.rain.mc.server_links.spigot.PluginConstants;
import cx.rain.mc.server_links.spigot.config.ConfigManager;
import cx.rain.mc.server_links.spigot.packet.PacketHelper;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerLoginListener implements Listener {

    private final ConfigManager configManager;

    public PlayerLoginListener(ConfigManager configManager) {
        this.configManager = configManager;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        var player = event.getPlayer();
        if (player.hasPermission(PluginConstants.PERMISSION_USER)) {
            PacketHelper.sendServerLinks(configManager.getLinks(), player);
        }
    }
}
