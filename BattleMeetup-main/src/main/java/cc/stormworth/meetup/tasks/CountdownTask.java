package cc.stormworth.meetup.tasks;

import cc.stormworth.core.CorePlugin;
import cc.stormworth.core.util.chat.CC;
import cc.stormworth.core.util.onedoteight.TitleBuilder;
import cc.stormworth.meetup.Meetup;
import cc.stormworth.meetup.managers.GameManager;
import cc.stormworth.meetup.util.Colors;
import cc.stormworth.meetup.util.Msg;
import cc.stormworth.meetup.util.TaskUtil;
import cc.stormworth.meetup.util.TimeUtil;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.scheduler.BukkitRunnable;

public class CountdownTask extends BukkitRunnable {

    public CountdownTask() {

        if (TimeUtil.getBroadcastMinutes().contains(GameManager.getInstance().getScatteringIn())) {
            int minutes = GameManager.getInstance().getScatteringIn() / 60;
            TaskUtil.runLater(() -> Msg.sendMessage(Colors.WHITE + "The scatter is starting in " + Colors.SECONDARY + minutes + " minute" + (minutes > 1 ? "s" : "") + Colors.WHITE + "."), 1L);
        } else if (TimeUtil.getBroadcastSeconds().contains(GameManager.getInstance().getScatteringIn())) {
            int seconds = GameManager.getInstance().getScatteringIn();
            TaskUtil.runLater(() -> {
                Msg.sendMessage(Colors.WHITE + "The scatter is starting in " + Colors.SECONDARY + seconds + " second" + (seconds > 1 ? "s" : "") + Colors.WHITE + ".");

                TitleBuilder titleBuilder = new TitleBuilder(CC.translate("&6&lMeetup"), CC.translate("&eStarting!"), 10, 10, 10);

                Bukkit.getOnlinePlayers().forEach(online -> {
                    titleBuilder.send(online);

                    online.playSound(online.getLocation(), Sound.NOTE_PLING, 1.0f, 1.0f);
                });

            }, 1L);
        }

        TextComponent textComponent = new TextComponent(CC.translate("&fVote for a GameStyle&7(UHC or HCF)&f by click on this Message. &7(Click to Vote)"));

        textComponent.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/gamestyle"));
        textComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TextComponent[]{new TextComponent(CC.translate("&7Click to vote."))}));

        Bukkit.getOnlinePlayers().forEach(online -> online.spigot().sendMessage(textComponent));


        runTaskTimer(Meetup.getInstance(), 20L, 20L);
    }

    @Override
    public void run() {
        GameManager.getInstance().setScatteringIn(GameManager.getInstance().getScatteringIn() - 1);

        if (GameManager.getInstance().getScatteringIn() == 30) {
            CorePlugin.getInstance().getRedisManager().writeAlertClickable(CC.GRAY + "[" + CC.PRIMARY + "✪" + CC.GRAY + "] " + CC.B_PRIMARY + CorePlugin.getInstance().getServerId().toUpperCase() + CC.SECONDARY + " is starting in 30 seconds. " + CC.GRAY + CC.ITALIC + "(Click to join)", "/joinsv " + CorePlugin.getInstance().getServerId(), false);
        }

        if (GameManager.getInstance().getScatteringIn() <= 5) {
            Bukkit.getOnlinePlayers().forEach(online -> {
                if (online.getOpenInventory() != null && online.getOpenInventory().getTitle().contains("Editor")) online.closeInventory();
            });

            StartingTask.editorBlocked = true;
        }

        if (TimeUtil.getBroadcastMinutes().contains(GameManager.getInstance().getScatteringIn())) {
            int minutes = GameManager.getInstance().getScatteringIn() / 60;
            Msg.sendMessage(Colors.WHITE + "The scatter is starting in " + Colors.SECONDARY + minutes + " minute" + (minutes > 1 ? "s" : "") + Colors.WHITE + ".");
        } else if (TimeUtil.getBroadcastSeconds().contains(GameManager.getInstance().getScatteringIn())) {
            int seconds = GameManager.getInstance().getScatteringIn();
            Msg.sendMessage(Colors.WHITE + "The scatter is starting in " + Colors.SECONDARY + seconds + " second" + (seconds > 1 ? "s" : "") + Colors.WHITE + ".");
        } else if (GameManager.getInstance().getScatteringIn() == 0) {
            this.cancel();

            TitleBuilder titleBuilder = new TitleBuilder(CC.translate("&6&lMeetup"), CC.translate("&eStarting scatter!"), 10, 10, 10);

            Bukkit.getOnlinePlayers().forEach(online -> {
                titleBuilder.send(online);

                online.playSound(online.getLocation(), Sound.NOTE_PLING, 1.0f, 1.0f);
            });


            Msg.sendMessage(Colors.PRIMARY + "All players are now being scattered.", Sound.CLICK);
            GameManager.getInstance().startScatter();
        }
    }
}
