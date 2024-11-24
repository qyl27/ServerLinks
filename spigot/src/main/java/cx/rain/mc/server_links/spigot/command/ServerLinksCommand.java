package cx.rain.mc.server_links.spigot.command;

import cx.rain.mc.server_links.spigot.PluginConstants;
import cx.rain.mc.server_links.spigot.config.ConfigManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ServerLinksCommand implements CommandExecutor, TabCompleter {

    private final ConfigManager configManager;

    public ServerLinksCommand(ConfigManager configManager) {
        this.configManager = configManager;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (!sender.hasPermission(PluginConstants.PERMISSION_ADMIN)) {
            return false;
        }

        if (args.length == 1 && "reload".equals(args[0])) {
            configManager.reload();
            sender.sendMessage("Done. Rejoin to make effect.");
            return true;
        }

        sender.sendMessage("Usage: /serverlinks reload");
        return false;
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (sender.hasPermission(PluginConstants.PERMISSION_ADMIN)) {
            if (args == null || args.length == 0) {
                return List.of("reload");
            }
        }

        return List.of();
    }
}
