package cc.stormworth.meetup.util;

import cc.stormworth.core.util.chat.CC;
import org.bukkit.ChatColor;

import java.util.List;
import java.util.stream.Collectors;

public class Colors {

    public static final String PRIMARY = CC.PRIMARY;

    public static final String SECONDARY = CC.SECONDARY;

    public static final String BLUE = CC.BLUE;

    public static final String AQUA = CC.AQUA;

    public static final String YELLOW = CC.YELLOW;

    public static final String RED = CC.RED;

    public static final String GRAY = CC.GRAY;

    public static final String GOLD = CC.GOLD;

    public static final String GREEN = CC.GREEN;

    public static final String WHITE = CC.WHITE;

    public static final String BLACK = CC.BLACK;

    public static final String BOLD = CC.BOLD;

    public static final String ITALIC = CC.ITALIC;

    public static final String STRIKE_THROUGH = CC.STRIKE_THROUGH;

    public static final String RESET = CC.RESET;

    public static final String MAGIC = CC.MAGIC;

    public static final String DARK_BLUE = CC.DARK_BLUE;

    public static final String DARK_AQUA = CC.DARK_AQUA;

    public static final String DARK_GRAY = CC.DARK_GRAY;

    public static final String DARK_GREEN = CC.DARK_GREEN;

    public static final String DARK_PURPLE = CC.DARK_PURPLE;

    public static final String DARK_RED = CC.DARK_RED;

    public static final String LIGHT_PURPLE = CC.LIGHT_PURPLE;

    public static String translate(String input) {
        return ChatColor.translateAlternateColorCodes('&', input);
    }

    public static List<String> translate(List<String> input) {
        return input.stream().map(Colors::translate).collect(Collectors.toList());
    }
}
