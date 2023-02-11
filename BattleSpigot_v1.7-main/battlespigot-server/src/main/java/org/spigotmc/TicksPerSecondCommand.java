package org.spigotmc;

import net.minecraft.server.MinecraftServer;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.math3.util.FastMath;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.text.DecimalFormat;

public class TicksPerSecondCommand extends Command {

    private int[] steps = new int[]{20, 19, 18, 14, 9, 0};
    private StringBuilder[] builders;
    public static final DecimalFormat df = new DecimalFormat("#.#");
    private static DecimalFormat readableFileSizeFormatter = new DecimalFormat("#,##0.#");
    long startTime;

    public TicksPerSecondCommand(String name) {
        super( name );
        this.startTime = System.currentTimeMillis();
        this.builders = new StringBuilder[this.steps.length];
        for (int i = 0; i < this.builders.length; i++) {
            this.builders[i] = new StringBuilder();
        }
        this.description = "Gets the current ticks per second for the server";
        this.usageMessage = "/tps";
        this.setPermission( "bukkit.command.tps" );
    }

    @Override
    public boolean execute(CommandSender sender, String currentAlias, String[] args) {
        if (!testPermission(sender)) {
            return true;
        }
        if (sender.hasPermission("bukkit.command.tps.advanced")) {
            double[] tps = Bukkit.spigot().getTPS();
            String[] tpsAvg = new String[tps.length];

            for (int i = 0; i < tps.length; i++) {
                tpsAvg[i] = format(tps[i]);
            }

            int entities = MinecraftServer.getServer().entities;
            int activeEntities = MinecraftServer.getServer().activeEntities;
            double activePercent = FastMath.round(10000.0 * activeEntities / entities) / 100.0;
            final Runtime runtime = Runtime.getRuntime();
            final long used = runtime.totalMemory() - runtime.freeMemory();

            sender.sendMessage("" + ChatColor.GRAY + ChatColor.STRIKETHROUGH + "----------------------------------------------");
            sender.sendMessage(ChatColor.GOLD + "TPS from last 1m, 5m, 15m: " + StringUtils.join(tpsAvg, ", "));
            sender.sendMessage(ChatColor.GOLD + "Online players: " + ChatColor.GREEN + String.format("%s/%s", sender.getServer().getOnlinePlayers().size(), sender.getServer().getMaxPlayers()));
            sender.sendMessage(ChatColor.GOLD + "Full tick: " + formatTickTime(MinecraftServer.getServer().lastTickTime) + " ms" + " (" + "Threads: " + Thread.getAllStackTraces().keySet().parallelStream().filter(Thread::isAlive).count() + "/" + Thread.getAllStackTraces().keySet().parallelStream().count() + ", Daemon: " + Thread.getAllStackTraces().keySet().parallelStream().filter(Thread::isDaemon).count() + ")");
            sender.sendMessage(ChatColor.GOLD + "Entities: " + ChatColor.GREEN + activeEntities + "/" + entities + " (" + activePercent + "%)");
            sender.sendMessage(ChatColor.GOLD + "Memory: " + ChatColor.GREEN + readableFileSize(used) + "/" + readableFileSize(runtime.totalMemory()) + " (Max: " + readableFileSize(runtime.maxMemory()) + ")");
            sender.sendMessage(ChatColor.GOLD + "Uptime: " + ChatColor.GREEN + formatFullMilis(Long.valueOf(System.currentTimeMillis() - this.startTime)));
            if (sender instanceof Player) {
                sender.sendMessage(ChatColor.GOLD + "Chunks: " + ChatColor.GREEN + Bukkit.getWorld(((Player) sender).getLocation().getWorld().getName()).getLoadedChunks().length + " §7(" + ((Player) sender).getLocation().getWorld().getName() + "§7)");
            }
            sender.sendMessage("" + ChatColor.GRAY + ChatColor.STRIKETHROUGH + "----------------------------------------------");
        }
        return true;
    }

    public static String formatFullMilis(final Long milis) {
        final double seconds = FastMath.max(0L, milis) / 1000.0;
        final double minutes = seconds / 60.0;
        final double hours = minutes / 60.0;
        final double days = hours / 24.0;
        final double weeks = days / 7.0;
        final double months = days / 31.0;
        final double years = months / 12.0;
        if (years >= 1.0) {
            return String.valueOf(TicksPerSecondCommand.df.format(years)) + " year" + ((years != 1.0) ? "s" : "");
        }
        if (months >= 1.0) {
            return String.valueOf(TicksPerSecondCommand.df.format(months)) + " month" + ((months != 1.0) ? "s" : "");
        }
        if (weeks >= 1.0) {
            return String.valueOf(TicksPerSecondCommand.df.format(weeks)) + " week" + ((weeks != 1.0) ? "s" : "");
        }
        if (days >= 1.0) {
            return String.valueOf(TicksPerSecondCommand.df.format(days)) + " day" + ((days != 1.0) ? "s" : "");
        }
        if (hours >= 1.0) {
            return String.valueOf(TicksPerSecondCommand.df.format(hours)) + " hour" + ((hours != 1.0) ? "s" : "");
        }
        if (minutes >= 1.0) {
            return String.valueOf(TicksPerSecondCommand.df.format(minutes)) + " minute" + ((minutes != 1.0) ? "s" : "");
        }
        return String.valueOf(TicksPerSecondCommand.df.format(seconds)) + " second" + ((seconds != 1.0) ? "s" : "");
    }

    private static String readableFileSize(final long size) {
        if (size <= 0L) {
            return "0";
        }
        final String[] units = { "B", "kB", "MB", "GB", "TB" };
        final int digitGroups = (int)(FastMath.log10(size) / FastMath.log10(1024.0));
        return String.valueOf(TicksPerSecondCommand.readableFileSizeFormatter.format(size / FastMath.pow(1024.0, digitGroups))) + ' ' + units[digitGroups];
    }

    private static String format(double tps) {
        return (tps > 18.0 ? ChatColor.GREEN : tps > 16.0 ? ChatColor.YELLOW : ChatColor.RED).toString() + ((tps > 20.0) ? "*" : "") + FastMath.min(FastMath.round(tps * 100.0D) / 100.0, 20.0);
    }

    private static String formatTickTime(double time) {
        return (time < 40.0D ? ChatColor.GREEN : time < 60.0D ? ChatColor.YELLOW : ChatColor.RED).toString() + FastMath.round(time * 10.0D) / 10.0D;
    }
}