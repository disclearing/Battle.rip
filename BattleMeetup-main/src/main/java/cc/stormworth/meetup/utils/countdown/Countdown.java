package cc.stormworth.meetup.utils.countdown;

import cc.stormworth.core.util.chat.CC;
import cc.stormworth.core.util.time.TimeUtils;
import cc.stormworth.meetup.Meetup;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;

public class Countdown implements Runnable {

    private final String broadcastMessage;
    private final Runnable tickHandler, broadcastHandler, finishHandler;
    private final Predicate<Player> messageFilter;
    @Getter
    private final List<Player> playerList;
    // Our scheduled task's assigned id, needed for canceling
    @Getter
    private final Integer assignedTaskId;
    private final int[] broadcastAt;
    private int seconds;
    private boolean first;

    public Countdown(int seconds, String broadcastMessage, Runnable tickHandler,
                     Runnable broadcastHandler,
                     Runnable finishHandler, Predicate<Player> messageFilter, List<Player> playerList,
                     int... broadcastAt) {
        this.first = true;
        this.seconds = seconds;
        this.broadcastMessage = broadcastMessage;
        this.broadcastAt = broadcastAt;
        this.tickHandler = tickHandler;
        this.broadcastHandler = broadcastHandler;
        this.finishHandler = finishHandler;
        this.messageFilter = messageFilter;
        this.playerList = playerList;
        this.assignedTaskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(Meetup.getInstance(), this, 0L, 20L);
    }

    public static CountdownBuilder of(int amount, TimeUnit unit) {
        return new CountdownBuilder((int) unit.toSeconds(amount));
    }

    public void run() {
        if (!this.first) {
            --this.seconds;
        } else {
            this.first = false;
        }
        for (int index : this.broadcastAt) {
            if (this.seconds == index) {
                if (broadcastMessage != null) {
                    String message = this.broadcastMessage.replace("{time}",
                            TimeUtils.formatIntoDetailedString(this.seconds));
                    if (playerList == null) {
                        for (Player player : Bukkit.getOnlinePlayers()) {
                            if (this.messageFilter == null || this.messageFilter.test(player)) {
                                player.sendMessage(CC.translate(message));
                            }
                        }
                    } else {
                        for (Player player : playerList) {
                            if (player != null && (player.isOnline() && this.messageFilter == null
                                    || this.messageFilter.test(player))) {
                                player.sendMessage(CC.translate(message));
                            }
                        }
                    }
                }
                if (this.broadcastHandler != null) {
                    this.broadcastHandler.run();
                }
            }
        }
        if (this.seconds == 0) {
            if (this.finishHandler != null) {
                this.finishHandler.run();
            }
            if (assignedTaskId != null) {
                Bukkit.getScheduler().cancelTask(assignedTaskId);
            }
        } else if (this.tickHandler != null) {
            this.tickHandler.run();
        }
    }

    public void cancel() {
        if (assignedTaskId != null) {
            Bukkit.getScheduler().cancelTask(assignedTaskId);
        }
    }

    public int getSecondsRemaining() {
        return this.seconds;
    }
}
