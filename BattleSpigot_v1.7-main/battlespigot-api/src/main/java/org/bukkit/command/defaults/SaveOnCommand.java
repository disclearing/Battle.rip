package org.bukkit.command.defaults;

import com.google.common.collect.ImmutableList;
import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.List;

public class SaveOnCommand extends VanillaCommand {
    public SaveOnCommand() {
        super("save-on");
        this.description = "Enables server autosaving";
        this.usageMessage = "/save-on";
        this.setPermission("bukkit.command.save.enable");
    }

    @Override
    public boolean execute(CommandSender sender, String currentAlias, String[] args) {
        if (!testPermission(sender)) return true;

        for (World world : Bukkit.getWorlds()) {
            world.setAutoSave(true);
        }

        Command.broadcastCommandMessage(sender, "Enabled level saving..");
        return true;
    }
}