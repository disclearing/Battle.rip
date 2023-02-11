package cc.stormworth.meetup.commands;

import cc.stormworth.core.util.chat.CC;
import cc.stormworth.meetup.managers.GameManager;
import cc.stormworth.meetup.style.menu.EditorSelectorMenu;
import cc.stormworth.meetup.util.Colors;
import cc.stormworth.meetup.util.Msg;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class KitCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {

        if (!(commandSender instanceof Player)) {
            Msg.logConsole(CC.SECONDARY + "This" + CC.PRIMARY + " Meetup" + CC.SECONDARY + " command can only be performed by players.");
            return false;
        }

        Player p = (Player) commandSender;

        if (GameManager.getInstance().isLobby()) {
            new EditorSelectorMenu(p).openInventory();
        } else {
            p.sendMessage(Colors.RED + "The game has already started.");
        }

        return false;
    }
}
