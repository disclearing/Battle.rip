package cc.stormworth.meetup.border.glass;

import cc.stormworth.meetup.Meetup;
import cc.stormworth.meetup.managers.GameManager;
import cc.stormworth.meetup.util.TaskUtil;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import eu.vortexdev.battlespigot.BattleSpigot;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Predicate;
import java.util.stream.IntStream;

public class GlassManager implements Listener {
    private static GlassManager instance;
    private final ExecutorService executor = Executors.newSingleThreadExecutor(TaskUtil.newThreadFactory("Glass Thread - %d"));
    private final Table glassCache = HashBasedTable.create();

    private GlassManager() {
        Bukkit.getPluginManager().registerEvents(this, Meetup.getInstance());
        BattleSpigot.INSTANCE.addPacketHandler(new GlassPacketHandler());
    }

    public static GlassManager getInstance() {

        if (instance == null) {
            instance = new GlassManager();
        }

        return instance;
    }

    public void generateGlassVisual(Player player, GlassInfo info) {
        if (this.glassCache.contains(player.getUniqueId(), info.getLocation()))
            return;
        int x = info.getLocation().getBlockX() >> 4;
        int z = info.getLocation().getBlockZ() >> 4;
        if (!info.getLocation().getWorld().isChunkLoaded(x, z))
            return;
        info.getLocation().getWorld().getChunkAtAsync(x, z, chunk -> {
            Material material = info.getLocation().getBlock().getType();
            if (material != Material.AIR && material.isOccluding())
                return;
            player.sendBlockChange(info.getLocation(), info.getMaterial(), info.getData());
            this.glassCache.put(player.getUniqueId(), info.getLocation(), info);
        });
    }

    public GlassInfo getGlassAt(Player player, Location location) {
        return (GlassInfo) this.glassCache.get(player.getUniqueId(), location);
    }

    public void clearGlassVisuals(Player player, GlassType type, Predicate<GlassInfo> predicate) {
        clearGlassVisuals(player, glassInfo -> (glassInfo.getType() == type && predicate.test(glassInfo)));
    }

    private void clearGlassVisuals(Player player, Predicate<GlassInfo> predicate) {
        synchronized (this.glassCache) {
            Iterator<Map.Entry<Location, GlassInfo>> iterator = this.glassCache.row(player.getUniqueId()).entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<Location, GlassInfo> entry = iterator.next();
                if (!predicate.test(entry.getValue()))
                    continue;
                Location location = entry.getKey();
                if (!location.getWorld().isChunkLoaded(location.getBlockX() >> 4, location.getBlockZ() >> 4)) {
                    iterator.remove();
                    continue;
                }
                player.sendBlockChange(entry.getKey(), location.getBlock().getType(), location.getBlock().getData());
                iterator.remove();
            }
        }
    }

    private void handlePlayerMove(Player player, Location to) {
        GlassType type = GlassType.BORDER_WALL;
        clearGlassVisuals(player, type, glassInfo -> {
            Location loc = glassInfo.getLocation();
            return (loc.getWorld().equals(to.getWorld()) && (Math.abs(loc.getBlockX() - to.getBlockX()) > 5 || Math.abs(loc.getBlockY() - to.getBlockY()) > 4 || Math.abs(loc.getBlockZ() - to.getBlockZ()) > 5));
        });
        if (player.getLocation().getWorld().getName().equals("game_world"))
            GameManager.getInstance().getBorder().getCuboid().edges(to).forEach(side -> {
                if (Math.abs(side.getBlockX() - to.getBlockX()) <= 10.0D && Math.abs(side.getBlockZ() - to.getBlockZ()) <= 10.0D)
                    IntStream.rangeClosed(-3, 4).forEach((y) -> {
                        Location location = side.clone();
                        location.setY(to.getBlockY() + y);
                        this.generateGlassVisual(player, new GlassInfo(type, location, Material.STAINED_GLASS, (byte) 14));
                    });

            });
    }

    public void handleMove(Player player, Location from, Location to) {
        this.executor.execute(() -> {
            if (from.getBlockX() != to.getBlockX() || from.getBlockY() != to.getBlockY() || from.getBlockZ() != to.getBlockZ())
                handlePlayerMove(player, to);
        });
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        TaskUtil.runAsyncLater(() -> handlePlayerMove(player, player.getLocation()), 5L);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        if (this.glassCache.containsRow(player.getUniqueId()))
            this.glassCache.row(player.getUniqueId()).clear();
    }

    public enum GlassType {
        BORDER_WALL
    }
}
