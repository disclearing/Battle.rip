package dev.nulledcode.spigot.command;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.spigotmc.SpigotConfig;

public class FreezeEntitiesCommand extends Command {

    public FreezeEntitiesCommand(String name) {
        super(name);
        this.usageMessage = "/" + name;
        this.description = "freezes every entity around all the worlds";
        this.setPermission("battlespigot.freezeentities");
    }

    @Override
    public boolean execute(CommandSender sender, String arg1, String[] args) {
        if (!testPermission(sender)) {
            return true;
        }

        SpigotConfig.freezeentities = !SpigotConfig.freezeentities;
        if (SpigotConfig.freezeentities) {
            sender.sendMessage(ChatColor.YELLOW + "[BattleSpigot] Entities has been frozen.");
        } else {
            sender.sendMessage(ChatColor.YELLOW + "[BattleSpigot] Entities has been unfrozen.");
        }

        return false;
    }
}