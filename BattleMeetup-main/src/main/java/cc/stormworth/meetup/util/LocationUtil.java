package cc.stormworth.meetup.util;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Skull;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Stream;

public class LocationUtil {

    private static final Material[] forbiddenBlocks = new Material[]{Material.WATER, Material.STATIONARY_WATER, Material.LAVA, Material.STATIONARY_LAVA, Material.CACTUS};
    private static final List<Material> blockedWallBlocks = Arrays.asList(Material.LOG, Material.LOG_2, Material.LEAVES, Material.LEAVES_2,
            Material.AIR, Material.WATER, Material.STATIONARY_WATER, Material.LAVA, Material.STATIONARY_LAVA,
            Material.HUGE_MUSHROOM_1, Material.HUGE_MUSHROOM_2, Material.DOUBLE_PLANT, Material.LONG_GRASS,
            Material.VINE, Material.YELLOW_FLOWER, Material.RED_ROSE, Material.CACTUS, Material.DEAD_BUSH,
            Material.SUGAR_CANE_BLOCK, Material.ICE, Material.SNOW, Material.BEDROCK);

    public static Location getRandomScatterLocation(World world, int radius) {
        boolean caveMeetup = world.hasMetadata("worldType");
        if(caveMeetup) {
            caveMeetup = world.getMetadata("worldType").get(0).asString().equalsIgnoreCase("CAVE");
        }

        ThreadLocalRandom random = ThreadLocalRandom.current();
        int x = random.nextInt(-radius + 5, radius - 5);
        int z = random.nextInt(-radius + 5, radius - 5);
        int y = -1;

        if (world.getEnvironment() == World.Environment.NETHER) {
            for (int i = 0; i < 100; i++) {
                Block block1 = world.getBlockAt(x, i, z);
                Block block2 = world.getBlockAt(x, (i + 1), z);
                Block block3 = world.getBlockAt(x, (i + 2), z);

                Block blockBelow = block1.getRelative(BlockFace.DOWN);
                if(Stream.of(Material.LAVA, Material.AIR).anyMatch(t -> blockBelow.getType() == t)) continue;

                if (!block1.getType().isSolid()) continue;
                if (block2.getType() != Material.AIR) continue;
                if (block3.getType() != Material.AIR) continue;
                if (block1.getType() == Material.BEDROCK) continue;

                y = i;
                break;
            }
        } else {
            if (caveMeetup) {
                y = random.nextInt(5, world.getHighestBlockYAt(x,z) - 10);
            } else {
                y = world.getHighestBlockYAt(x, z);
            }
        }

        if (world.getEnvironment() == World.Environment.NETHER) {
            if (Stream.of(
                    world.getBlockAt(x, y, z),
                    world.getBlockAt(x - 1, y, z),
                    world.getBlockAt(x, y, z + 1),
                    world.getBlockAt(x, y, z - 1),
                    world.getBlockAt(x + 1, y, z)
            ).anyMatch(block -> block.getType() == Material.AIR)) {
                return getRandomScatterLocation(world, radius);
            }
        }

        boolean gType = world.hasMetadata("worldType");
        if(gType) {
            gType = world.getMetadata("gameType").get(0).asString().equals("CAVE");
        }
        if (gType) {
            if (Stream.of(
                    world.getBlockAt(x, y, z),
                    world.getBlockAt(x, y + 1, z)
            ).anyMatch(block -> block.getType() == Material.AIR)) {
                return getRandomScatterLocation(world, radius);
            }
        }

        Location location = new Location(world, x, y, z);
        Location centeredLocation = location.getBlock().getLocation().add((location.getBlock().getLocation().getX() < 0.0) ? -0.5 : 0.5, 0.275, (location.getBlock().getLocation().getZ() < 0.0) ? -0.5 : 0.5);

        if (y == -1) return getRandomScatterLocation(world, radius);

        if (centeredLocation.getWorld().getEnvironment() == World.Environment.NETHER) {
            if (centeredLocation.getBlock().getRelative(BlockFace.DOWN).getType() == Material.BEDROCK) {
                return getRandomScatterLocation(world, radius);
            }
        }

        for (Material forbiddenBlock : forbiddenBlocks) {
            if (centeredLocation.getBlock().getRelative(BlockFace.DOWN).getType() == forbiddenBlock) {
                return getRandomScatterLocation(world, radius);
            }
        }

        if ((centeredLocation.getBlock().getType() == Material.AIR && centeredLocation.clone().add(0.0D, 1.0D, 0.0D).getBlock().getType() == Material.AIR) || (centeredLocation.getBlock() == null && centeredLocation.clone().add(0.0D, 1.0D, 0.0D).getBlock() == null)) {
            return centeredLocation;
        }

        return getRandomScatterLocation(world, radius);
    }

    public static int getHighestBlockY(World world, int x, int z) {
        Block block = world.getHighestBlockAt(x, z);
        int y = block.getY();

        while (blockedWallBlocks.contains(block.getRelative(BlockFace.DOWN).getType())) {
            block = block.getRelative(BlockFace.DOWN);
            y--;
        }

        return y;
    }

    public static void spawnHead(LivingEntity entity) {
        entity.getLocation().getBlock().setType(Material.FENCE);
        entity.getWorld().getBlockAt(entity.getLocation().add(0.0D, 1.0D, 0.0D)).setType(Material.SKULL);
        Skull skull = (Skull) entity.getLocation().add(0.0D, 1.0D, 0.0D).getBlock().getState();

        if (entity instanceof Player) {
            Player player = (Player) entity;
            skull.setOwner(player.getName());
        } else {
            skull.setOwner(ChatColor.stripColor(entity.getCustomName()));
        }

        skull.update();
        Block block = entity.getLocation().add(0.0D, 1.0D, 0.0D).getBlock();
        block.setData((byte) 1);
    }

    public static Location deserializeLocation(String input) {
        String[] attributes = input.split(":");
        World world = null;
        Double x = null;
        Double y = null;
        Double z = null;
        Float pitch = null;
        Float yaw = null;
        for (String attribute : attributes) {
            String[] split = attribute.split(";");
            if (split[0].equalsIgnoreCase("#w")) {
                world = Bukkit.getWorld(split[1]);
            } else if (split[0].equalsIgnoreCase("#x")) {
                x = Double.valueOf(Double.parseDouble(split[1]));
            } else if (split[0].equalsIgnoreCase("#y")) {
                y = Double.valueOf(Double.parseDouble(split[1]));
            } else if (split[0].equalsIgnoreCase("#z")) {
                z = Double.valueOf(Double.parseDouble(split[1]));
            } else if (split[0].equalsIgnoreCase("#p")) {
                pitch = Float.valueOf(Float.parseFloat(split[1]));
            } else if (split[0].equalsIgnoreCase("#yaw")) {
                yaw = Float.valueOf(Float.parseFloat(split[1]));
            }
        }
        if (world == null || x == null || y == null || z == null || pitch == null || yaw == null)
            return null;
        return new Location(world, x.doubleValue(), y.doubleValue(), z.doubleValue(), yaw.floatValue(), pitch.floatValue());
    }

    public static String serializeLocation(Location location) {
        return "#w;" + location.getWorld().getName() + ":#x;" + location
                .getX() + ":#y;" + location
                .getY() + ":#z;" + location
                .getZ() + ":#p;" + location
                .getPitch() + ":#yaw;" + location
                .getYaw();
    }
}
