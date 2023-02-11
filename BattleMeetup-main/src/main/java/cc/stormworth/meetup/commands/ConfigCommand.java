package cc.stormworth.meetup.commands;

import cc.stormworth.core.util.chat.CC;
import cc.stormworth.meetup.managers.InventoryManager;
import cc.stormworth.meetup.util.Msg;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ConfigCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if (!(commandSender instanceof Player)) {
            Msg.logConsole(CC.SECONDARY + "This" + CC.PRIMARY + " Meetup" + CC.SECONDARY + " command can only be performed by players.");
            return false;
        }

        Player p = (Player) commandSender;
        InventoryManager.getInstance().openConfigInventory(p);
        return false;
    }
}
