package cc.stormworth.meetup.tasks;

import cc.stormworth.meetup.Meetup;
import cc.stormworth.meetup.managers.GameManager;
import cc.stormworth.meetup.states.GameState;
import cc.stormworth.meetup.util.Colors;
import cc.stormworth.meetup.util.Msg;
import cc.stormworth.meetup.util.TaskUtil;
import cc.stormworth.meetup.util.TimeUtil;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

public class EndTask extends BukkitRunnable {

    public EndTask() {

        if (TimeUtil.getBroadcastMinutes().contains(GameManager.getInstance().getRestartingIn())) {
            int minutes = GameManager.getInstance().getRestartingIn() / 60;
            Msg.sendMessage(Colors.WHITE + "The server is restarting in " + Colors.SECONDARY + minutes + " minute" + (minutes > 1 ? "s" : "") + Colors.WHITE + ".");
        } else if (TimeUtil.getBroadcastSeconds().contains(GameManager.getInstance().getRestartingIn())) {
            int seconds = GameManager.getInstance().getRestartingIn();
            Msg.sendMessage(Colors.WHITE + "The server is restarting in " + Colors.SECONDARY + seconds + " second" + (seconds > 1 ? "s" : "") + Colors.WHITE + ".");
        }

        runTaskTimer(Meetup.getInstance(), 20L, 20L);
    }

    @Override
    public void run() {
        GameManager.getInstance().setRestartingIn(GameManager.getInstance().getRestartingIn() - 1);

        if (TimeUtil.getBroadcastMinutes().contains(GameManager.getInstance().getRestartingIn())) {
            int minutes = GameManager.getInstance().getRestartingIn() / 60;
            Msg.sendMessage(Colors.WHITE + "The server is restarting in " + Colors.SECONDARY + minutes + " minute" + (minutes > 1 ? "s" : "") + Colors.WHITE + ".");
        } else if (TimeUtil.getBroadcastSeconds().contains(GameManager.getInstance().getRestartingIn())) {
            int seconds = GameManager.getInstance().getRestartingIn();
            Msg.sendMessage(Colors.WHITE + "The server is restarting in " + Colors.SECONDARY + seconds + " second" + (seconds > 1 ? "s" : "") + Colors.WHITE + ".");
        } else if (GameManager.getInstance().getRestartingIn() == 0) {
            this.cancel();
            Msg.sendMessage(Colors.PRIMARY + "The server is now restarting.");
            GameManager.getInstance().setGameState(GameState.OFFLINE);
            TaskUtil.runLater(() -> Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "restart"), 10L);
        }
    }
}
