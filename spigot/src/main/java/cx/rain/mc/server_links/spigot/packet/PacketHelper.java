package cx.rain.mc.server_links.spigot.packet;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.reflect.EquivalentConverter;
import com.comphenix.protocol.utility.MinecraftReflection;
import com.comphenix.protocol.wrappers.*;
import cx.rain.mc.server_links.spigot.config.LinkType;
import org.bukkit.entity.Player;

import java.util.List;

public class PacketHelper {
    private static final Class<?> KNOWN_LINK_TYPE = MinecraftReflection.getMinecraftClass("server.ServerLinks$KnownLinkType");

    public static final PacketType SERVER_LINKS_PACKET_TYPE = new PacketType(PacketType.Protocol.PLAY, PacketType.Sender.SERVER, 0x7B, "cx.rain.mc.server_links.ServerLinks");

    public static PacketContainer createServerLinksPacket() {
        return ProtocolLibrary.getProtocolManager().createPacket(SERVER_LINKS_PACKET_TYPE);
    }

    public static final EquivalentConverter<LinkEntry> CONVERTER = AutoWrapper
            .wrap(LinkEntry.class, "server.ServerLinks$UntrustedEntry")
            .field(0, getEitherConverter(BukkitConverters.getEitherConverter(EnumWrappers.getGenericConverter(KNOWN_LINK_TYPE, LinkType.class),
                    BukkitConverters.getWrappedChatComponentConverter()), EitherImpl.class));


    public static <A, B> EquivalentConverter<Either<A, B>> getEitherConverter(final EquivalentConverter<Either<A, B>> converter, Class<?> clazz) {
        return Converters.ignoreNull(new EquivalentConverter<>() {
            public Object getGeneric(Either<A, B> specific) {
                return converter.getGeneric(specific);
            }

            public Either<A, B> getSpecific(Object generic) {
                return converter.getSpecific(generic);
            }

            @SuppressWarnings("all")
            public Class<Either<A, B>> getSpecificType() {
                return (Class) clazz;
            }
        });
    }

    public static void sendServerLinks(List<LinkEntry> links, Player player) {
        var packet = PacketHelper.createServerLinksPacket();
        packet.getLists(PacketHelper.CONVERTER).write(0, links);
        ProtocolLibrary.getProtocolManager().sendServerPacket(player, packet);
    }
}
