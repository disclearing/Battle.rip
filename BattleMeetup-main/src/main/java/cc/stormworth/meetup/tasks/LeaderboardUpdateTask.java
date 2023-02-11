package cc.stormworth.meetup.tasks;

import cc.stormworth.meetup.Meetup;
import org.bukkit.scheduler.BukkitRunnable;

public class LeaderboardUpdateTask extends BukkitRunnable {
    @Override
    public void run() {
        Meetup.getInstance().getLeaderboards().forEach((leaderboardType, leaderboard) -> {
            leaderboard.load(false);
        });
    }
}
