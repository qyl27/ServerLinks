package cx.rain.mc.server_links.spigot.config;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentStyle;
import net.md_5.bungee.api.chat.TextComponent;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

public class TextStyleHelper {
    public static Map<Integer, Function<ComponentStyle, ComponentStyle>> COLOR_CODES = new HashMap<>();
    public static Map<Integer, Function<ComponentStyle, ComponentStyle>> STYLE_CODES = new HashMap<>();

    static {
        COLOR_CODES.put((int) '0', style -> withColor(style, ChatColor.BLACK));
        COLOR_CODES.put((int) '1', style -> withColor(style, ChatColor.DARK_BLUE));
        COLOR_CODES.put((int) '2', style -> withColor(style, ChatColor.DARK_GREEN));
        COLOR_CODES.put((int) '3', style -> withColor(style, ChatColor.DARK_AQUA));
        COLOR_CODES.put((int) '4', style -> withColor(style, ChatColor.DARK_RED));
        COLOR_CODES.put((int) '5', style -> withColor(style, ChatColor.DARK_PURPLE));
        COLOR_CODES.put((int) '6', style -> withColor(style, ChatColor.GOLD));
        COLOR_CODES.put((int) '7', style -> withColor(style, ChatColor.GRAY));
        COLOR_CODES.put((int) '8', style -> withColor(style, ChatColor.DARK_GRAY));
        COLOR_CODES.put((int) '9', style -> withColor(style, ChatColor.BLUE));
        COLOR_CODES.put((int) 'A', style -> withColor(style, ChatColor.GREEN));
        COLOR_CODES.put((int) 'B', style -> withColor(style, ChatColor.AQUA));
        COLOR_CODES.put((int) 'C', style -> withColor(style, ChatColor.RED));
        COLOR_CODES.put((int) 'D', style -> withColor(style, ChatColor.LIGHT_PURPLE));
        COLOR_CODES.put((int) 'E', style -> withColor(style, ChatColor.YELLOW));
        COLOR_CODES.put((int) 'F', style -> withColor(style, ChatColor.WHITE));
        COLOR_CODES.put((int) 'R', style -> empty());
        STYLE_CODES.put((int) 'K', style -> with(style, style::setObfuscated));
        STYLE_CODES.put((int) 'L', style -> with(style, style::setBold));
        STYLE_CODES.put((int) 'M', style -> with(style, style::setStrikethrough));
        STYLE_CODES.put((int) 'N', style -> with(style, style::setUnderlined));
        STYLE_CODES.put((int) 'O', style -> with(style, style::setItalic));
    }

    private static ComponentStyle withColor(ComponentStyle style, ChatColor color) {
        style.setColor(color);
        return style;
    }

    private static ComponentStyle with(ComponentStyle style, Consumer<Boolean> consumer) {
        consumer.accept(true);
        return style;
    }

    private static ComponentStyle empty() {
        return new ComponentStyle();
    }

    public static BaseComponent parseStyle(String literalText) {
        var component = new TextComponent();

        var it = literalText.codePoints().iterator();
        var escaping = false;
        var builder = new StringBuilder();
        var style = empty();
        var buildingHexColor = false;
        var hexColorBuilder = new StringBuilder();
        while (it.hasNext()) {
            var codePoint = it.nextInt();
            var ch = Character.toChars(codePoint);
            if (codePoint == '&') {
                if (escaping) {
                    escaping = false;
                    builder.append('&');
                } else {
                    escaping = true;
                }
                continue;
            }

            if (escaping) {
                var u = Character.toUpperCase(codePoint);
                if (buildingHexColor) {
                    if (!COLOR_CODES.containsKey(u)) {
                        builder.append(hexColorBuilder);
                        buildingHexColor = false;
                        hexColorBuilder = new StringBuilder();
                        escaping = false;
                        continue;
                    }

                    hexColorBuilder.append(ch);
                    if (hexColorBuilder.length() == 6) {
                        try {
                            var hex = ChatColor.of("#" + hexColorBuilder);
                            var c = new TextComponent(builder.toString());
                            c.setStyle(style);
                            component.addExtra(c);
                            builder = new StringBuilder();
                            style = withColor(empty(), hex);
                        } catch (IllegalArgumentException ignored) {
                        }
                        buildingHexColor = false;
                        hexColorBuilder = new StringBuilder();
                        escaping = false;
                    }
                    continue;
                }

                if (COLOR_CODES.containsKey(u)) {
                    var c = new TextComponent(builder.toString());
                    c.setStyle(style);
                    component.addExtra(c);
                    builder = new StringBuilder();
                    style = COLOR_CODES.get(u).apply(empty());
                    hexColorBuilder = new StringBuilder();
                    escaping = false;
                    continue;
                } else if (STYLE_CODES.containsKey(u)) {
                    style = STYLE_CODES.get(u).apply(style);
                    escaping = false;
                    continue;
                } else if (u == '#') {
                    buildingHexColor = true;
                    hexColorBuilder = new StringBuilder();
                    continue;
                }
            }

            builder.append(ch);
        }

        var c = new TextComponent(builder.toString());
        c.setStyle(style);
        component.addExtra(c);
        return component;
    }
}
