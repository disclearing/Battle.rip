package cc.stormworth.meetup.tasks;

import cc.stormworth.meetup.Meetup;
import cc.stormworth.meetup.managers.GameManager;
import org.bukkit.scheduler.BukkitRunnable;

public class AlivePlayersCheckTask extends BukkitRunnable {

    public AlivePlayersCheckTask() {
        runTaskTimer(Meetup.getInstance(), 5 * 20L, 5 * 20L);
    }

    @Override
    public void run() {

        if (GameManager.getInstance().getAlivePlayers().size() == 0) {
            this.cancel();
            GameManager.getInstance().endGame();
        }
    }
}
