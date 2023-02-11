package cc.stormworth.meetup.util;

import org.bukkit.Bukkit;

public class BorderUtil {

    public static int calculateBorderSize() {
        int players = Bukkit.getOnlinePlayers().size();

        if (players >= 26)
            return 250;
        if (players >= 22)
            return 175;
        if (players >= 18)
            return 125;
        if (players >= 14)
            return 100;
        if (players >= 10)
            return 75;

        return 50;
    }
}
