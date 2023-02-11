package cc.stormworth.meetup.util;

import cc.stormworth.meetup.Meetup;
import cc.stormworth.meetup.states.Mode;

public class ServerUtil {

    public static Mode getMode() {
        String mode = Meetup.getInstance().getConfig().getString("Game.Mode");

        if (mode.equals("FFA")) {
            return Mode.FFA;
        }

        if (mode.equals("To2")) {
            return Mode.TO2;
        }

        return Mode.FFA;
    }

    public static String getRegion() {
        String region = Meetup.getInstance().getConfig().getString("Server.Region");
        return region;
    }

    public static String getIP() {
        String region = Meetup.getInstance().getConfig().getString("Server.Region");

        if (region.equals("Europe")) {
            return "eu.battle.rip";
        }

        if (region.equals("North America")) {
            return "na.battle.rip";
        }

        return "battle.rip";
    }
}
