package cc.stormworth.meetup.commands;

import cc.stormworth.core.rank.Rank;
import cc.stormworth.core.util.chat.CC;
import cc.stormworth.meetup.managers.GameManager;
import cc.stormworth.meetup.states.GameState;
import cc.stormworth.meetup.states.Mode;
import cc.stormworth.meetup.util.Msg;
import cc.stormworth.meetup.util.PlayerUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ForceStartCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {

        if (!(commandSender instanceof Player)) {
            Msg.logConsole(CC.SECONDARY + "This" + CC.PRIMARY + " Meetup" + CC.SECONDARY + " command can only be performed by players.");
            return false;
        }

        Player p = (Player) commandSender;

        if (!PlayerUtil.testPermission(p, Rank.WARRIOR)) {
            p.sendMessage(CC.RED + "This command is limited to " + Rank.WARRIOR.getName() + " or above.");
            return false;
        }

        if (!GameManager.getInstance().isLobby()) {
            p.sendMessage(CC.RED + "You cannot use this command right now.");
            return false;
        }

        if (Bukkit.getOnlinePlayers().size() < (GameManager.getInstance().getMode() == Mode.TO2 ? 4 : 2)) {
            p.sendMessage(CC.RED + "There are not enough players to force start the game.");
            return false;
        }

        if (GameManager.getInstance().getGameState() == GameState.STARTING) {

            if (GameManager.getInstance().getScatteringIn() <= 15) {
                p.sendMessage(CC.RED + "The game is already starting.");
                return false;
            }

            Msg.sendMessage(CC.SECONDARY + "The scatter countdown has been reduced to " + CC.PRIMARY + "15 seconds" + CC.SECONDARY + ".");
            GameManager.getInstance().setScatteringIn(15);
            return false;
        }

        Msg.sendMessage(CC.SECONDARY + "The game has been force started.");
        GameManager.getInstance().setScatteringIn(15);
        GameManager.getInstance().startCountdown();
        return false;
    }
}
