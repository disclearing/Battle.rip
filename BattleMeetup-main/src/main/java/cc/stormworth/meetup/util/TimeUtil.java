package cc.stormworth.meetup.util;

import org.apache.commons.lang.time.DurationFormatUtils;

import java.util.Arrays;
import java.util.List;

public class TimeUtil {

    public static final long ONE_TICK_IN_MS = 50L;

    private static final List<Integer> broadcastSeconds = Arrays.asList(30, 10, 5, 4, 3, 2, 1);
    private static final List<Integer> broadcastMinutes = Arrays.asList(300, 240, 180, 120, 60);

    public static List<Integer> getBroadcastSeconds() {
        return broadcastSeconds;
    }

    public static List<Integer> getBroadcastMinutes() {
        return broadcastMinutes;
    }

    public static String format(final int i) {
        final int r = i * 1000;
        final int sec = r / 1000 % 60;
        final int min = r / 60000 % 60;
        final int h = r / 3600000 % 24;
        return ((h > 0) ? (((h < 10) ? "0" : "") + h + ":") : "") + ((min < 10) ? ("0" + min) : Integer.valueOf(min)) + ":" + ((sec < 10) ? ("0" + sec) : Integer.valueOf(sec));
    }

    public static String formatDuration(long input) {
        return DurationFormatUtils.formatDurationWords(input, true, true);
    }
}
