package cc.stormworth.meetup.commands;

import cc.stormworth.core.CorePluginAPI;
import cc.stormworth.core.util.chat.CC;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public class HealthCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {

        if (args.length == 0) {

            if (sender instanceof ConsoleCommandSender) {
                sender.sendMessage(CC.RED + "Usage: /health <player>");
            } else {
                sender.sendMessage(CC.SECONDARY + "Your health: " + CC.RED + (Math.ceil(((Player) sender).getHealth()) / 2.0D) + '❤');
            }

            return false;
        }

        Player target = Bukkit.getPlayer(args[0]);

        if (target == null) {
            sender.sendMessage(CC.RED + "This player is offline.");
            return false;
        }

        sender.sendMessage(CorePluginAPI.getProfile(target.getUniqueId()).getColoredUsername() + CC.SECONDARY + "'s health: " + CC.RED + (Math.ceil(target.getHealth()) / 2.0D) + '❤');
        return false;
    }
}
