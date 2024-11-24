package cx.rain.mc.server_links;

import java.io.IOException;
import java.time.OffsetDateTime;
import java.util.Properties;

public class ServerLinks {
    public static final String VERSION;
    public static final OffsetDateTime BUILD_TIME;

    static {
        var properties = new Properties();
        var version = "";
        OffsetDateTime buildTime;
        try {
            properties.load(ServerLinks.class.getResourceAsStream("/build_info.properties"));
            version = properties.getProperty("plugin_version");
            buildTime = OffsetDateTime.parse(properties.getProperty("build_time"));
        } catch (IOException ex) {
            version = "Unknown";
            buildTime = null;
        }
        VERSION = version;
        BUILD_TIME = buildTime;
    }
}
