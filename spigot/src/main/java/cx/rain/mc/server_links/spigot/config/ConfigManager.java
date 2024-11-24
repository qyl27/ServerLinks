package cx.rain.mc.server_links.spigot.config;

import com.comphenix.protocol.wrappers.ComponentConverter;
import cx.rain.mc.server_links.spigot.packet.EitherImpl;
import cx.rain.mc.server_links.spigot.packet.LinkEntry;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;

public class ConfigManager {
    private final Plugin plugin;

    private final List<LinkEntry> links = new ArrayList<>();

    public ConfigManager(Plugin plugin) {
        this.plugin = plugin;
        plugin.saveDefaultConfig();

        reload();
    }

    public void reload() {
        plugin.reloadConfig();
        var config = plugin.getConfig();

        links.clear();
        for (var e : config.getMapList("server-links")) {
            var type = LinkType.valueOf(e.get("type").toString());
            var url = e.get("url").toString();
            if (type == LinkType.CUSTOM) {
                var text = e.get("text").toString();
                links.add(new LinkEntry(new EitherImpl<>(null, ComponentConverter.fromBaseComponent(TextStyleHelper.parseStyle(text))), url));
                continue;
            }
            links.add(new LinkEntry(new EitherImpl<>(type, null), url));
        }
    }

    public List<LinkEntry> getLinks() {
        return links;
    }
}
