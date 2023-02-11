package dev.nulledcode.spigot.command;

import net.minecraft.server.EntityTracker;
import org.apache.commons.math3.util.FastMath;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.CraftWorld;

public class NoTrackCommand extends Command {

    public NoTrackCommand(String name) {
        super(name);
        this.usageMessage = "/" + name + " <world name> <view distance>";
        this.description = "Adjusts a world's no track distance";
        this.setPermission("battlespigot.command.notrack");
    }

    @Override
    public boolean execute(CommandSender sender, String arg1, String[] args) {
        if (!testPermission(sender)) {
            return true;
        }

        if (args != null && args.length == 2) {
            String worldName = args[0];
            String newNTR = args[1];
            int trackRange = -1;
            try {
                trackRange = Integer.parseInt(newNTR);
            } catch (NumberFormatException e) {
                sender.sendMessage("'" + newNTR + "' is not a valid integer.");
                return false;
            }
            trackRange = FastMath.max(trackRange, 0);
            World world = Bukkit.getWorld(worldName);
            if (world != null) {
                CraftWorld craftworld = (CraftWorld) world;
                EntityTracker entityTracker = craftworld.getHandle().getTracker();
                entityTracker.setNoTrackDistance(trackRange);
                sender.sendMessage("Track distance of world '" + worldName + "' was set to " + trackRange);
                //if (trackRange > 0) SpigotConfig.disableEntityCollisions = true;
            } else {
                sender.sendMessage("World '" + worldName + "' was not found!");
            }
            return true;
        } else {
            sender.sendMessage("[sSpigot] Command - notrack: " + this.description + "\nUsage: " + this.usageMessage);
        }
        return false;
    }
}