package cc.stormworth.meetup.commands;

import cc.stormworth.core.util.chat.CC;
import cc.stormworth.meetup.Meetup;
import cc.stormworth.meetup.managers.GameManager;
import cc.stormworth.meetup.managers.InventoryManager;
import cc.stormworth.meetup.managers.UserManager;
import cc.stormworth.meetup.states.GameState;
import cc.stormworth.meetup.user.UserData;
import cc.stormworth.meetup.util.Colors;
import cc.stormworth.meetup.util.LocationUtil;
import cc.stormworth.meetup.util.Msg;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetRotativeHologramCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (!(commandSender instanceof Player)) {
            Msg.logConsole(CC.SECONDARY + "This" + CC.PRIMARY + " Meetup" + CC.SECONDARY + " command can only be performed by players.");
            return false;
        }

        if (!commandSender.hasPermission("uhcmeetup.setrotativehologram")) return true;

        Player p = (Player) commandSender;

        Meetup.getInstance().getLeaderboardHandler().moveHologram(p.getLocation());

        Meetup.getInstance().getConfig().set("Locations.Top-Location", LocationUtil.serializeLocation(p.getLocation()));
        Meetup.getInstance().getConfig().save();
        return false;
    }
}
