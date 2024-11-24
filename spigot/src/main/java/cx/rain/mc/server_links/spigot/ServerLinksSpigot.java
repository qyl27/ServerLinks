package cx.rain.mc.server_links.spigot;

import cx.rain.mc.server_links.spigot.command.ServerLinksCommand;
import cx.rain.mc.server_links.spigot.config.ConfigManager;
import cx.rain.mc.server_links.spigot.listener.PlayerLoginListener;
import org.bukkit.plugin.java.JavaPlugin;

public final class ServerLinksSpigot extends JavaPlugin {
    private final ConfigManager configManager;

    public ServerLinksSpigot() {
        configManager = new ConfigManager(this);
    }

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(new PlayerLoginListener(configManager), this);

        var pluginCommand = getServer().getPluginCommand("serverlinks");
        assert pluginCommand != null;

        var command = new ServerLinksCommand(configManager);
        pluginCommand.setExecutor(command);
        pluginCommand.setTabCompleter(command);
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }
}
