package cc.stormworth.meetup.tasks;

import cc.stormworth.meetup.Meetup;
import cc.stormworth.meetup.border.Border;
import cc.stormworth.meetup.managers.GameManager;
import cc.stormworth.meetup.states.GameState;
import cc.stormworth.meetup.util.Colors;
import cc.stormworth.meetup.util.Msg;
import cc.stormworth.meetup.util.TimeUtil;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.scheduler.BukkitRunnable;

public class GameTask extends BukkitRunnable {

    private static int shrink;

    public GameTask() {

        if (GameManager.getInstance().getBorder().getNext() != 0) {
            shrink = GameManager.getInstance().getGameTime();

            if (shrink == GameManager.getInstance().getBorderTime()) {
                shrink = 0;
            }

            int borderTimeDifference = GameManager.getInstance().getBorderTime() - shrink;

            if (TimeUtil.getBroadcastMinutes().contains(borderTimeDifference)) {
                int minutes = borderTimeDifference / 60;
                Msg.sendMessage(Colors.WHITE + "The border is shrinking to " + Colors.SECONDARY
                        + GameManager.getInstance().getBorder().getNext() + Colors.WHITE + " in " + Colors.SECONDARY
                        + minutes + " minute" + (minutes > 1 ? "s" : "") + Colors.WHITE + ".");
            } else if (TimeUtil.getBroadcastSeconds().contains(borderTimeDifference)) {
                int seconds = borderTimeDifference;
                Msg.sendMessage(Colors.WHITE + "The border is shrinking to " + Colors.SECONDARY
                        + GameManager.getInstance().getBorder().getNext() + Colors.WHITE + " in " + Colors.SECONDARY
                        + seconds + " second" + (seconds > 1 ? "s" : "") + Colors.WHITE + ".");
            }
        }

        runTaskTimer(Meetup.getInstance(), 20L, 20L);
    }

    public static int getShrink() {
        return shrink;
    }

    @Override
    public void run() {
        if (GameManager.getInstance().getGameState() == GameState.ENDING) {
            this.cancel();

            return;
        }

        GameManager.getInstance().setGameTime(GameManager.getInstance().getGameTime() + 1);

        if (GameManager.getInstance().getBorder().getNext() != 0) {
            shrink++;

            if (shrink > GameManager.getInstance().getBorderTime()) {
                shrink = 1;
            }

            int borderTimeDifference = GameManager.getInstance().getBorderTime() - shrink;

            if (TimeUtil.getBroadcastMinutes().contains(borderTimeDifference)) {
                int minutes = borderTimeDifference / 60;
                Msg.sendMessage(Colors.WHITE + "The border is shrinking to " + Colors.SECONDARY
                        + GameManager.getInstance().getBorder().getNext() + Colors.WHITE + " in " + Colors.SECONDARY
                        + minutes + " minute" + (minutes > 1 ? "s" : "") + Colors.WHITE + ".");
            } else if (TimeUtil.getBroadcastSeconds().contains(borderTimeDifference)) {
                int seconds = borderTimeDifference;
                Msg.sendMessage(Colors.WHITE + "The border is shrinking to " + Colors.SECONDARY
                        + GameManager.getInstance().getBorder().getNext() + Colors.WHITE + " in " + Colors.SECONDARY
                        + seconds + " second" + (seconds > 1 ? "s" : "") + Colors.WHITE + ".");
            } else if (borderTimeDifference == 0) {
                Msg.sendMessage(
                        Colors.WHITE + "The border has shrunk to " + Colors.SECONDARY
                                + GameManager.getInstance().getBorder().getNext() + Colors.WHITE + ".",
                        Sound.CLICK);
                Border border = new Border(Bukkit.getWorld("game_world"),
                        GameManager.getInstance().getBorder().getNext());

                teleportPlayersInBorder(border);

                border.shrink();
                GameManager.getInstance().setBorder(border);

                if (border.getNext() != 0) {

                    if (TimeUtil.getBroadcastMinutes().contains(GameManager.getInstance().getBorderTime())) {
                        int minutes = GameManager.getInstance().getBorderTime() / 60;
                        Msg.sendMessage(Colors.WHITE + "The border is shrinking to " + Colors.SECONDARY
                                + GameManager.getInstance().getBorder().getNext() + Colors.WHITE + " in "
                                + Colors.SECONDARY + minutes + " minute" + (minutes > 1 ? "s" : "") + Colors.WHITE
                                + ".");
                    } else if (TimeUtil.getBroadcastSeconds().contains(GameManager.getInstance().getBorderTime())) {
                        int seconds = GameManager.getInstance().getBorderTime();
                        Msg.sendMessage(Colors.WHITE + "The border is shrinking to " + Colors.SECONDARY
                                + GameManager.getInstance().getBorder().getNext() + Colors.WHITE + " in "
                                + Colors.SECONDARY + seconds + " second" + (seconds > 1 ? "s" : "") + Colors.WHITE
                                + ".");
                    }
                }
            }
        }
    }

    public void teleportPlayersInBorder(Border border) {
        /*if (GameManager.getInstance().getMode() == Mode.TO2) {
            for (Team t : TeamManager.getTeamsAlive()) {

                Location loc = LocationUtil.getRandomScatterLocation(Bukkit.getWorld("game_world"),
                        GameManager.getInstance().getBorder().getSize());

                Iterator<UUID> iterator = t.getAlivePlayers().iterator();

                while (iterator.hasNext()) {
                    UUID u = iterator.next();
                    Player p = Bukkit.getPlayer(u);

                    if (p == null) {
                        CombatLogger cl = GameManager.getInstance().getCombatLogger(u);

                        if (!border.isInBorder(cl.getLocation())) {
                            cl.teleport(loc);
                        }

                        continue;
                    }

                    if (!border.isInBorder(p.getLocation())) {
                        p.teleport(loc);
                    }

                    continue;

                }
            }

            return;
        }

        for (UUID u : GameManager.getInstance().getAlivePlayers()) {
            Player p = Bukkit.getPlayer(u);

            if (!border.isInBorder(p.getLocation())) {
                p.teleport(LocationUtil.getRandomScatterLocation(Bukkit.getWorld("game_world"),
                        GameManager.getInstance().getBorder().getSize()));
            }
        }*/

    }
}
