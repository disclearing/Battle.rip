package cc.stormworth.meetup.commands;

import cc.stormworth.core.util.chat.CC;
import cc.stormworth.meetup.managers.GameManager;
import cc.stormworth.meetup.managers.InventoryManager;
import cc.stormworth.meetup.managers.UserManager;
import cc.stormworth.meetup.states.GameState;
import cc.stormworth.meetup.user.UserData;
import cc.stormworth.meetup.util.Colors;
import cc.stormworth.meetup.util.Msg;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class InvSeeCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {

        if (!(commandSender instanceof Player)) {
            Msg.logConsole(CC.SECONDARY + "This" + CC.PRIMARY + " Meetup" + CC.SECONDARY + " command can only be performed by players.");
            return false;
        }

        Player p = (Player) commandSender;
        UserData user = UserManager.getInstance().getUser(p.getUniqueId());

        if (user.isAlive()) {
            p.sendMessage(Colors.RED + "You cannot use this command right now.");
            return false;
        }

        if (GameManager.getInstance().getGameState() != GameState.ENDING) {
            p.sendMessage(Colors.RED + "You cannot use this command right now.");
            return false;
        }

        if (strings.length == 0) {
            p.sendMessage(Colors.RED + "Usage: /invsee <player>");
            return false;
        }

        Player target = Bukkit.getPlayer(strings[0]);

        if (target == null) {
            p.sendMessage(Colors.RED + "This inventory could not be found.");
            return false;
        }

        p.openInventory(InventoryManager.getInstance().getPlayerInventory(target));

        return false;
    }
}
