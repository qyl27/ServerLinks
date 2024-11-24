package cx.rain.mc.server_links.velocity.utility;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class TextStyleHelper {
    public static Map<Integer, Function<Style, Style>> COLOR_CODES = new HashMap<>();
    public static Map<Integer, Function<Style, Style>> STYLE_CODES = new HashMap<>();

    static {
        COLOR_CODES.put((int) '0', style -> withColor(style, NamedTextColor.BLACK));
        COLOR_CODES.put((int) '1', style -> withColor(style, NamedTextColor.DARK_BLUE));
        COLOR_CODES.put((int) '2', style -> withColor(style, NamedTextColor.DARK_GREEN));
        COLOR_CODES.put((int) '3', style -> withColor(style, NamedTextColor.DARK_AQUA));
        COLOR_CODES.put((int) '4', style -> withColor(style, NamedTextColor.DARK_RED));
        COLOR_CODES.put((int) '5', style -> withColor(style, NamedTextColor.DARK_PURPLE));
        COLOR_CODES.put((int) '6', style -> withColor(style, NamedTextColor.GOLD));
        COLOR_CODES.put((int) '7', style -> withColor(style, NamedTextColor.GRAY));
        COLOR_CODES.put((int) '8', style -> withColor(style, NamedTextColor.DARK_GRAY));
        COLOR_CODES.put((int) '9', style -> withColor(style, NamedTextColor.BLUE));
        COLOR_CODES.put((int) 'A', style -> withColor(style, NamedTextColor.GREEN));
        COLOR_CODES.put((int) 'B', style -> withColor(style, NamedTextColor.AQUA));
        COLOR_CODES.put((int) 'C', style -> withColor(style, NamedTextColor.RED));
        COLOR_CODES.put((int) 'D', style -> withColor(style, NamedTextColor.LIGHT_PURPLE));
        COLOR_CODES.put((int) 'E', style -> withColor(style, NamedTextColor.YELLOW));
        COLOR_CODES.put((int) 'F', style -> withColor(style, NamedTextColor.WHITE));
        COLOR_CODES.put((int) 'R', style -> empty());
        STYLE_CODES.put((int) 'K', style -> with(style, TextDecoration.OBFUSCATED));
        STYLE_CODES.put((int) 'L', style -> with(style, TextDecoration.BOLD));
        STYLE_CODES.put((int) 'M', style -> with(style, TextDecoration.STRIKETHROUGH));
        STYLE_CODES.put((int) 'N', style -> with(style, TextDecoration.UNDERLINED));
        STYLE_CODES.put((int) 'O', style -> with(style, TextDecoration.ITALIC));
    }

    private static Style withColor(Style style, TextColor color) {
        return style.color(color);
    }

    private static Style with(Style style, TextDecoration decoration) {
        return style.decorate(decoration);
    }

    private static Style empty() {
        return Style.empty();
    }

    public static Component parseStyle(String literalText) {
        var component = Component.empty();

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
                            var hex = TextColor.fromHexString("#" + hexColorBuilder);
                            var c = Component.text(builder.toString()).style(style);
                            component = component.append(c);
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
                    var c = Component.text(builder.toString()).style(style);
                    component = component.append(c);
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

        var c = Component.text(builder.toString()).style(style);
        component = component.append(c);
        return component;
    }
}
