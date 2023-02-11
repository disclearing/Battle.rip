package dev.nulledcode.spigot.command;

import dev.nulledcode.spigot.BattleSpigot;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.Arrays;

public class SetSlotsCommand extends Command {

    public SetSlotsCommand() {
        super("setmaxplayers");
        this.setAliases(Arrays.asList("setslots"));
        this.usageMessage = "/setslots <amount>";
        this.description = "Adjusts the slots on the server";
        this.setPermission("battlespigot.command.setslots");
    }

    @Override
    public boolean execute(CommandSender sender, String arg1, String[] args) {
        if (!testPermission(sender)) {
            return true;
        }

        if (args.length != 1) {
            sender.sendMessage(this.usageMessage);
            return true;
        }
        if (!BattleSpigot.isInteger(args[0])) {
            sender.sendMessage(this.usageMessage);
            return true;
        }
        final int slots = Integer.parseInt(args[0]);
        Bukkit.getServer().setMaxPlayers(slots);
        sender.sendMessage("§6Max players updated to §l" + slots + ".");
        return false;
    }
}