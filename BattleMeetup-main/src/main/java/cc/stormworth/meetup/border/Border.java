package cc.stormworth.meetup.border;

import cc.stormworth.meetup.Meetup;
import cc.stormworth.meetup.border.glass.Cuboid;
import cc.stormworth.meetup.managers.GameManager;
import cc.stormworth.meetup.scenarios.Scenario;
import cc.stormworth.meetup.util.LocationUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayDeque;
import java.util.Queue;

public class Border {

    private final World world;
    private final int size;
    private final Cuboid cuboid;
    public Border(World world, int size) {
        this.world = world;
        this.size = size;

        cuboid = new Cuboid(new Location(world, size, 0.0D, -size - 1), new Location(world, -size - 1, 0.0D, size));
    }

    public World getWorld() {
        return world;
    }

    public int getSize() {
        return size;
    }

    public Cuboid getCuboid() {
        return cuboid;
    }

    public int getNext() {

        if (size == 250) {
            return 175;
        }

        if (size == 175 || size == 125) {
            return 100;
        }

        if (size == 100 || size == 75) {
            return 50;
        }

        if (size == 50) {
            return 25;
        }

        return 0;
    }

    public void preShrink() {
        final int blocksPerTick = 15;

        int posX = size;
        int negX = -size - 1;
        int posZ = size;
        int negZ = -size - 1;

        final Queue<Location> locations1 = new ArrayDeque<>();
        final Queue<Location> locations2 = new ArrayDeque<>();
        final Queue<Location> locations3 = new ArrayDeque<>();
        final Queue<Location> locations4 = new ArrayDeque<>();
        final Queue<Location> locations5 = new ArrayDeque<>();
        final Queue<Location> locations6 = new ArrayDeque<>();
        final Queue<Location> locations7 = new ArrayDeque<>();
        final Queue<Location> locations8 = new ArrayDeque<>();

        for (int t = posX; t >= 0; t--) {
            int y = LocationUtil.getHighestBlockY(world, t, posZ) - 1;
            locations1.add(new Location(world, t, y, posZ));
        }

        for (int t = negX; t <= 0; t++) {
            int y = LocationUtil.getHighestBlockY(world, t, posZ) - 1;
            locations2.add(new Location(world, t, y, posZ));
        }

        for (int t = posX; t >= 0; t--) {
            int y = LocationUtil.getHighestBlockY(world, t, negZ) - 1;
            locations3.add(new Location(world, t, y, negZ));
        }

        for (int t = negX; t <= -0; t++) {
            int y = LocationUtil.getHighestBlockY(world, t, negZ) - 1;
            locations4.add(new Location(world, t, y, negZ));
        }

        for (int t = posZ; t >= 0; t--) {
            int y = LocationUtil.getHighestBlockY(world, posX, t) - 1;
            locations5.add(new Location(world, posX, y, t));
        }

        for (int t = negZ; t <= -0; t++) {
            int y = LocationUtil.getHighestBlockY(world, posX, t) - 1;
            locations6.add(new Location(world, posX, y, t));
        }

        for (int t = posZ; t >= 0; t--) {
            int y = LocationUtil.getHighestBlockY(world, negX, t) - 1;
            locations7.add(new Location(world, negX, y, t));
        }

        for (int t = negZ; t <= -0; t++) {
            int y = LocationUtil.getHighestBlockY(world, negX, t) - 1;
            locations8.add(new Location(world, negX, y, t));
        }

        new BukkitRunnable() {

            @Override
            public void run() {

                for (int x = 0; x < blocksPerTick; x++) {
                    if (!locations1.isEmpty()) {
                        locations1.poll().getBlock().setType(Material.BEDROCK);
                    }

                    if (!locations2.isEmpty()) {
                        locations2.poll().getBlock().setType(Material.BEDROCK);
                    }

                    if (!locations3.isEmpty()) {
                        locations3.poll().getBlock().setType(Material.BEDROCK);
                    }

                    if (!locations4.isEmpty()) {
                        locations4.poll().getBlock().setType(Material.BEDROCK);
                    }

                    if (!locations5.isEmpty()) {
                        locations5.poll().getBlock().setType(Material.BEDROCK);
                    }

                    if (!locations6.isEmpty()) {
                        locations6.poll().getBlock().setType(Material.BEDROCK);
                    }

                    if (!locations7.isEmpty()) {
                        locations7.poll().getBlock().setType(Material.BEDROCK);
                    }

                    if (!locations8.isEmpty()) {
                        locations8.poll().getBlock().setType(Material.BEDROCK);
                    } else {
                        this.cancel();
                    }
                }
            }
        }.runTaskTimer(Meetup.getInstance(), 0L, 1L);
    }

    public void shrink() {
        Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(),
                "wb " + world.getName() + " set " + size + " " + size + " 0 0");

        /*Bukkit.getOnlinePlayers().forEach(o -> {
            ClientAPI.removeBorder(o);
            ClientAPI.sendBorder(o, size);
        });*/

        final int blocksPerTick = 15;
        final int highestBlock = 4;

        int posX = size;
        int negX = -size - 1;
        int posZ = size;
        int negZ = -size - 1;

        final Queue<Location> locations1 = new ArrayDeque<>();
        final Queue<Location> locations2 = new ArrayDeque<>();
        final Queue<Location> locations3 = new ArrayDeque<>();
        final Queue<Location> locations4 = new ArrayDeque<>();
        final Queue<Location> locations5 = new ArrayDeque<>();
        final Queue<Location> locations6 = new ArrayDeque<>();
        final Queue<Location> locations7 = new ArrayDeque<>();
        final Queue<Location> locations8 = new ArrayDeque<>();

        for (int t = posX; t >= 0; t--) {
            int min = LocationUtil.getHighestBlockY(world, t, posZ);
            int max = min + highestBlock;

            if (max < 256) {

                for (int y = min; y < max; y++) {
                    locations1.add(new Location(world, t, y, posZ));
                }
            }
        }

        for (int t = negX; t <= 0; t++) {
            int min = LocationUtil.getHighestBlockY(world, t, posZ);
            int max = min + highestBlock;

            if (max < 256) {

                for (int y = min; y < max; y++) {
                    locations2.add(new Location(world, t, y, posZ));
                }
            }
        }

        for (int t = posX; t >= 0; t--) {
            int min = LocationUtil.getHighestBlockY(world, t, negZ);
            int max = min + highestBlock;

            if (max < 256) {

                for (int y = min; y < max; y++) {
                    locations3.add(new Location(world, t, y, negZ));
                }
            }
        }

        for (int t = negX; t <= -0; t++) {
            int min = LocationUtil.getHighestBlockY(world, t, negZ);
            int max = min + highestBlock;

            if (max < 256) {

                for (int y = min; y < max; y++) {
                    locations4.add(new Location(world, t, y, negZ));
                }
            }
        }

        for (int t = posZ; t >= 0; t--) {
            int min = LocationUtil.getHighestBlockY(world, posX, t);
            int max = min + highestBlock;

            if (max < 256) {

                for (int y = min; y < max; y++) {
                    locations5.add(new Location(world, posX, y, t));
                }
            }
        }

        for (int t = negZ; t <= -0; t++) {
            int min = LocationUtil.getHighestBlockY(world, posX, t);
            int max = min + highestBlock;

            if (max < 256) {

                for (int y = min; y < max; y++) {
                    locations6.add(new Location(world, posX, y, t));
                }
            }
        }

        for (int t = posZ; t >= 0; t--) {
            int min = LocationUtil.getHighestBlockY(world, negX, t);
            int max = min + highestBlock;

            if (max < 256) {

                for (int y = min; y < max; y++) {
                    locations7.add(new Location(world, negX, y, t));
                }
            }
        }

        for (int t = negZ; t <= -0; t++) {
            int min = LocationUtil.getHighestBlockY(world, negX, t);
            int max = min + highestBlock;

            if (max < 256) {

                for (int y = min; y < max; y++) {
                    locations8.add(new Location(world, negX, y, t));
                }
            }
        }

        new BukkitRunnable() {

            @Override
            public void run() {

                for (int x = 0; x < blocksPerTick; x++) {
                    if (!locations1.isEmpty()) {
                        locations1.poll().getBlock().setType(Material.BEDROCK);
                    }

                    if (!locations2.isEmpty()) {
                        locations2.poll().getBlock().setType(Material.BEDROCK);
                    }

                    if (!locations3.isEmpty()) {
                        locations3.poll().getBlock().setType(Material.BEDROCK);
                    }

                    if (!locations4.isEmpty()) {
                        locations4.poll().getBlock().setType(Material.BEDROCK);
                    }

                    if (!locations5.isEmpty()) {
                        locations5.poll().getBlock().setType(Material.BEDROCK);
                    }

                    if (!locations6.isEmpty()) {
                        locations6.poll().getBlock().setType(Material.BEDROCK);
                    }

                    if (!locations7.isEmpty()) {
                        locations7.poll().getBlock().setType(Material.BEDROCK);
                    }

                    if (!locations8.isEmpty()) {
                        locations8.poll().getBlock().setType(Material.BEDROCK);
                    } else {
                        this.cancel();
                    }
                }
            }
        }.runTaskTimer(Meetup.getInstance(), 0L, 1L);

        enableScenarios();
    }

    private void enableScenarios() {
        if (size == 25) {
            if (Scenario.getByName("NoClean+") != null && Scenario.getByName("NoClean+").isActive() || GameManager.getInstance().getTotalPlayers() < 6)
                return;
            if (Scenario.getByName("NoClean+") != null) Scenario.getByName("NoClean+").toggle(null, true);
        }
    }

    public boolean isInBorder(Location loc) {
        boolean checkX = loc.getX() > cuboid.getMinX() && loc.getX() > cuboid.getMaxX();
        boolean checkY = loc.getY() > cuboid.getMinY() && loc.getY() > cuboid.getMaxY();
        boolean checkZ = loc.getZ() > cuboid.getMinZ() && loc.getZ() > cuboid.getMaxZ();
        return checkX && checkY && checkZ;
    }
}
