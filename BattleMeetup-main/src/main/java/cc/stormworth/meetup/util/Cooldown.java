package cc.stormworth.meetup.util;

import java.util.UUID;

/**
 * Created by Ryzeon
 * Project: AsumaUHC [v2]
 * Date: 09/01/2021 @ 05:46 p. m.
 */

public class Cooldown {

    private final UUID uuid;
    private final Long start;
    private final Long expired;
    private boolean notified;

    public Cooldown(String duration) {
        this.uuid = UUID.randomUUID();
        this.start = System.currentTimeMillis();
        long durationLong = NumberUtil.fromString(duration);
        this.expired = this.start + durationLong;
        if (durationLong == 0L)
            this.notified = true;
    }

    public Cooldown(int duration) {
        this.uuid = UUID.randomUUID();
        this.start = System.currentTimeMillis();
        long durationLong = (1000 * duration);
        this.expired = this.start + durationLong;
        if (durationLong == 0L)
            this.notified = true;
    }

    public UUID getUuid() {
        return uuid;
    }

    public boolean isNotified() {
        return notified;
    }

    public long getPassed() {
        return System.currentTimeMillis() - this.start;
    }

    public long getRemaining() {
        return hasExpired() ? 0 : this.expired - System.currentTimeMillis();
    }

    public boolean hasExpired() {
        return (System.currentTimeMillis() - this.expired >= 0L);
    }

    public String getTimeLeft() {
        if (getRemaining() >= 60000L)
            return NumberUtil.millisToRoundedTime(getRemaining());
        return NumberUtil.millisToSeconds(getRemaining());
    }

    public String getTimeMilisLeft() {
        return NumberUtil.millisToSeconds(getRemaining());
    }

    public String getContextLeft() {
        return "second" + ((getRemaining() / 1000L > 1L) ? "s" : "");
    }
}
