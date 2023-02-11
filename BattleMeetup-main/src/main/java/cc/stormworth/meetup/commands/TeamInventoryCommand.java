package cc.stormworth.meetup.commands;

import cc.stormworth.core.util.chat.CC;
import cc.stormworth.meetup.managers.GameManager;
import cc.stormworth.meetup.managers.TeamManager;
import cc.stormworth.meetup.managers.UserManager;
import cc.stormworth.meetup.states.Mode;
import cc.stormworth.meetup.team.Team;
import cc.stormworth.meetup.util.Colors;
import cc.stormworth.meetup.util.Msg;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TeamInventoryCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {

        if (!(commandSender instanceof Player)) {
            Msg.logConsole(CC.SECONDARY + "This" + CC.PRIMARY + " Meetup" + CC.SECONDARY + " command can only be performed by players.");
            return false;
        }

        Player p = (Player) commandSender;

        if (!GameManager.getInstance().isIngame()) {
            p.sendMessage(Colors.RED + "You cannot use this command right now.");
            return false;
        }

        if (GameManager.getInstance().getMode() != Mode.TO2) {
            p.sendMessage(Colors.RED + "Teams are disabled.");
            return false;
        }

        Team team = TeamManager.getInstance().getTeam(UserManager.getInstance().getUser(p.getUniqueId()).getTeamNumber());
        p.openInventory(team.getBackpack());
        return true;
    }
}