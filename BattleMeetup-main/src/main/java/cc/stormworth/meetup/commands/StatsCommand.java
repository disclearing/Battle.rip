package cc.stormworth.meetup.commands;

import cc.stormworth.core.CorePluginAPI;
import cc.stormworth.core.util.chat.CC;
import cc.stormworth.meetup.user.statistics.StatisticsMenu;
import cc.stormworth.meetup.util.Msg;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class StatsCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {

        if (!(commandSender instanceof Player)) {
            Msg.logConsole(CC.SECONDARY + "This" + CC.PRIMARY + " Meetup" + CC.SECONDARY + " command can only be performed by players.");
            return false;
        }

        Player p = (Player) commandSender;

        if (strings.length == 0) {
            new StatisticsMenu(p).openInventory();
            p.sendMessage(CC.SECONDARY + "You are now viewing your own statistics.");
            return false;
        }

        Player target = Bukkit.getPlayer(strings[0]);

        if (target == null) {
            commandSender.sendMessage(CC.RED + "This player is offline.");
            return false;
        }

        new StatisticsMenu(p, target.getUniqueId()).openInventory();
        p.sendMessage(CC.SECONDARY + "You are now viewing " + CorePluginAPI.getProfile(target.getUniqueId()).getColoredUsername() + CC.SECONDARY + "'s statistics.");
        return false;
    }
}
