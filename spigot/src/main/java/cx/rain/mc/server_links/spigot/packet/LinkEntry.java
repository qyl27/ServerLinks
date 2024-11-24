package cx.rain.mc.server_links.spigot.packet;

import com.comphenix.protocol.wrappers.*;
import cx.rain.mc.server_links.spigot.config.LinkType;

public record LinkEntry(Either<LinkType, WrappedChatComponent> type, String url) {
}
