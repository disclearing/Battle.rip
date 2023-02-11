package cc.stormworth.meetup.tasks;

import cc.stormworth.core.CorePlugin;
import cc.stormworth.core.util.chat.CC;
import cc.stormworth.meetup.Meetup;
import cc.stormworth.meetup.managers.GameManager;
import cc.stormworth.meetup.states.GameState;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

public class BroadcastCheckTask extends BukkitRunnable {

    public BroadcastCheckTask() {
        runTaskTimer(Meetup.getInstance(), 45 * 20L, 45 * 20L);
    }

    @Override
    public void run() {

        if (GameManager.getInstance().getGameState() == GameState.WAITING) {
            if (Bukkit.getOnlinePlayers().size() >= 2) {
                CorePlugin.getInstance().getRedisManager().writeAlertClickable(CC.GRAY + "[" + CC.PRIMARY + "✪" + CC.GRAY + "] " + CC.B_PRIMARY + CorePlugin.getInstance().getServerId().toUpperCase() + CC.SECONDARY + " is looking for players. " + CC.GRAY + CC.ITALIC + "(Click to join)", "/joinsv " + CorePlugin.getInstance().getServerId(), false);
            }
        }
    }
}
