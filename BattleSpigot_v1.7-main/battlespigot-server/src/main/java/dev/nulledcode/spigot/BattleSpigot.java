package dev.nulledcode.spigot;

import dev.nulledcode.spigot.handler.MovementHandler;
import dev.nulledcode.spigot.handler.PacketHandler;
import net.minecraft.server.World;
import org.apache.commons.math3.util.FastMath;
import org.bukkit.Bukkit;

import java.util.HashSet;
import java.util.Set;

public enum BattleSpigot {

    INSTANCE;

    private final Set<PacketHandler> packetHandlers = new HashSet<>();
    private final Set<MovementHandler> movementHandlers = new HashSet<>();

    private static BattleThread thread;

    public static BattleSpigot getInstance() {
        return INSTANCE;
    }

    public static void init() {
        (thread = new BattleThread(Runtime.getRuntime().availableProcessors() * 2)).loadAsyncThreads();
    }

    public static boolean isPositionOfTileEntityInUse(World world, double d0, double d1, double d2) {
        int i = (int) FastMath.floor(d0);
        int j = (int) FastMath.floor(d1);
        int k = (int) FastMath.floor(d2);
        return (world.isLoaded(i, j, k) && !unloadQueueContains(world, i, j, k));
    }

    public static boolean unloadQueueContains(World world, int x, int y, int z) {
        return world != null &&
            world.chunkProviderServer != null &&
            world.chunkProviderServer.unloadQueue != null &&
            world.chunkProviderServer.unloadQueue.contains(x >> 4, z >> 4);
    }

    public static boolean isInteger(String value) {
        try {
            Integer.parseInt(value);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public BattleThread getThread() {
        return thread;
    }

    public Set<PacketHandler> getPacketHandlers() {
        return this.packetHandlers;
    }

    public Set<MovementHandler> getMovementHandlers() {
        return this.movementHandlers;
    }

    public void addPacketHandler(PacketHandler handler) {
        Bukkit.getLogger().info("[BattleSpigot] Adding packet handler: " + handler.getClass().getPackage().getName() + "." + handler.getClass().getName());
        this.packetHandlers.add(handler);
    }

    public void addMovementHandler(MovementHandler handler) {
        Bukkit.getLogger().info("[BattleSpigot] Adding movement handler: " + handler.getClass().getPackage().getName() + "." + handler.getClass().getName());
        this.movementHandlers.add(handler);
    }
}