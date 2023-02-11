package cc.stormworth.meetup.tasks;

import cc.stormworth.core.util.chat.CC;
import cc.stormworth.core.util.onedoteight.TitleBuilder;
import cc.stormworth.meetup.Meetup;
import cc.stormworth.meetup.managers.GameManager;
import cc.stormworth.meetup.util.Colors;
import cc.stormworth.meetup.util.Msg;
import cc.stormworth.meetup.util.TimeUtil;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.scheduler.BukkitRunnable;

public class StartingTask extends BukkitRunnable {

    public static boolean editorBlocked;

    public StartingTask() {

        if (TimeUtil.getBroadcastMinutes().contains(GameManager.getInstance().getStartingIn())) {
            int minutes = GameManager.getInstance().getStartingIn() / 60;
            Msg.sendMessage(Colors.WHITE + "The game is starting in " + Colors.SECONDARY + minutes + " minute" + (minutes > 1 ? "s" : "") + Colors.WHITE + ".");
        } else if (TimeUtil.getBroadcastSeconds().contains(GameManager.getInstance().getStartingIn())) {
            int seconds = GameManager.getInstance().getStartingIn();
            Msg.sendMessage(Colors.WHITE + "The game is starting in " + Colors.SECONDARY + seconds + " second" + (seconds > 1 ? "s" : "") + Colors.WHITE + ".");
        }
        runTaskTimer(Meetup.getInstance(), 0L, 20L);
    }

    @Override
    public void run() {
        GameManager.getInstance().setStartingIn(GameManager.getInstance().getStartingIn() - 1);

        if (TimeUtil.getBroadcastMinutes().contains(GameManager.getInstance().getStartingIn())) {
            int minutes = GameManager.getInstance().getStartingIn() / 60;
            Msg.sendMessage(Colors.WHITE + "The game is starting in " + Colors.SECONDARY + minutes + " minute" + (minutes > 1 ? "s" : "") + Colors.WHITE + ".");
        } else if (TimeUtil.getBroadcastSeconds().contains(GameManager.getInstance().getStartingIn())) {
            int seconds = GameManager.getInstance().getStartingIn();

            TitleBuilder titleBuilder = new TitleBuilder(CC.translate("&6&lMeetup"), CC.translate("&eStarting in " + seconds + " second" + (seconds > 1 ? "s" : "")), 10, 10, 10);

            Bukkit.getOnlinePlayers().forEach(online -> {
                titleBuilder.send(online);

                online.playSound(online.getLocation(), Sound.NOTE_PLING, 1.0f, 1.0f);
            });

            Msg.sendMessage(Colors.WHITE + "The game is starting in " + Colors.SECONDARY + seconds + " second" + (seconds > 1 ? "s" : "") + Colors.WHITE + ".");
        } else if (GameManager.getInstance().getStartingIn() == 0) { //
            this.cancel();

            TitleBuilder titleBuilder = new TitleBuilder(CC.translate("&6&lMeetup"), CC.translate("&eGood luck!"), 10, 10, 10);

            Bukkit.getOnlinePlayers().forEach(online -> {
                titleBuilder.send(online);

                online.playSound(online.getLocation(), Sound.LEVEL_UP, 1.0f, 1.0f);
            });

            Msg.sendMessage(Colors.PRIMARY + "The game has started.", Sound.CLICK);
            GameManager.getInstance().startGame();
            return;
        }
    }
}
