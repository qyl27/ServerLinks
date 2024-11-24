package cx.rain.mc.server_links.velocity.config;

import com.velocitypowered.api.util.ServerLink;
import cx.rain.mc.server_links.velocity.utility.TextStyleHelper;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

@ConfigSerializable
public class Config {
    public static final Config DEFAULT_CONFIG;

    static {
        DEFAULT_CONFIG = new Config();
        var list = new ArrayList<Entry>();
        list.add(new Entry(ServerLink.Type.WEBSITE, null, "https://github.com/MeowCraftMC/ServerLinks"));
        list.add(new Entry(null, "&6Custom example.com", "https://www.example.com"));
        DEFAULT_CONFIG.setServerLinks(list);
    }

    private List<Entry> serverLinks = new ArrayList<>();

    private transient List<ServerLink> resolved;

    public List<ServerLink> getServerLinks() {
        if (resolved == null) {
            resolved = new ArrayList<>();
            resolved.addAll(serverLinks.stream()
                    .filter(e -> e.type != null ^ e.label != null)
                    .map(e -> {
                        if (e.type == null) {
                            return ServerLink.serverLink(TextStyleHelper.parseStyle(e.label), e.url());
                        }
                        return ServerLink.serverLink(e.type, e.url);
                    })
                    .toList());
        }

        return resolved;
    }

    public void setServerLinks(List<Entry> serverLinks) {
        this.serverLinks = serverLinks;
    }

    @ConfigSerializable
    public record Entry(@Nullable ServerLink.Type type, @Nullable String label, String url) {
    }
}
