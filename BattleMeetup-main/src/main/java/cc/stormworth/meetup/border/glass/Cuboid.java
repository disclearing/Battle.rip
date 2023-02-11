package cc.stormworth.meetup.border.glass;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import java.util.ArrayList;
import java.util.List;

public class Cuboid {

    private final String worldName;

    private final int minX;

    private final int minY;

    private final int minZ;

    private final int maxX;

    private final int maxY;

    private final int maxZ;

    public Cuboid(Cuboid other) {
        this(other.getWorld().getName(), other.minX, other.minY, other.minZ, other.maxX, other.maxY, other.maxZ);
    }

    public Cuboid(String worldName, int x1, int y1, int z1, int x2, int y2, int z2) {
        this.worldName = worldName;
        this.minX = Math.min(x1, x2);
        this.maxX = Math.max(x1, x2);
        this.minY = Math.min(y1, y2);
        this.maxY = Math.max(y1, y2);
        this.minZ = Math.min(z1, z2);
        this.maxZ = Math.max(z1, z2);
    }

    public Cuboid(Location first, Location second) {
        this.worldName = first.getWorld().getName();
        this.minX = Math.min(first.getBlockX(), second.getBlockX());
        this.minY = Math.min(first.getBlockY(), second.getBlockY());
        this.minZ = Math.min(first.getBlockZ(), second.getBlockZ());
        this.maxX = Math.max(first.getBlockX(), second.getBlockX());
        this.maxY = Math.max(first.getBlockY(), second.getBlockY());
        this.maxZ = Math.max(first.getBlockZ(), second.getBlockZ());
    }

    public String getWorldName() {
        return this.worldName;
    }

    public int getMinX() {
        return this.minX;
    }

    public int getMinY() {
        return this.minY;
    }

    public int getMinZ() {
        return this.minZ;
    }

    public int getMaxX() {
        return this.maxX;
    }

    public int getMaxY() {
        return this.maxY;
    }

    public int getMaxZ() {
        return this.maxZ;
    }

    public List<Location> edges(Location loc) {
        List<Location> closestEdges = new ArrayList<>();
        int closestX = closestX(loc);
        int closestZ = closestZ(loc);
        for (int x = Math.max(this.minX, loc.getBlockX() - 5); x <= Math.min(this.maxX, loc.getBlockX() + 5); x++)
            closestEdges.add(new Location(loc.getWorld(), x, loc.getBlockY(), closestZ));
        for (int z = Math.max(this.minZ, loc.getBlockZ() - 5); z <= Math.min(this.maxZ, loc.getBlockZ() + 5); z++)
            closestEdges.add(new Location(loc.getWorld(), closestX, loc.getBlockY(), z));
        return closestEdges;
    }

    public World getWorld() {
        return Bukkit.getWorld(this.worldName);
    }

    private int closestX(Location loc) {
        return (Math.abs(loc.getBlockX() - this.minX) < Math.abs(loc.getBlockX() - this.maxX)) ? this.minX : this.maxX;
    }

    private int closestZ(Location loc) {
        return (Math.abs(loc.getBlockZ() - this.minZ) < Math.abs(loc.getBlockZ() - this.maxZ)) ? this.minZ : this.maxZ;
    }

    public Cuboid inset(CuboidDirection dir, int amount) {
        return outset(dir, -amount);
    }

    public Cuboid outset(CuboidDirection dir, int amount) {
        Cuboid c;
        switch (dir) {
            case HORIZONTAL:
                c = expand(CuboidDirection.NORTH, amount).expand(CuboidDirection.SOUTH, amount).expand(CuboidDirection.EAST, amount).expand(CuboidDirection.WEST, amount);
                return c;
            case VERTICAL:
                c = expand(CuboidDirection.DOWN, amount).expand(CuboidDirection.UP, amount);
                return c;
            case BOTH:
                c = outset(CuboidDirection.HORIZONTAL, amount).outset(CuboidDirection.VERTICAL, amount);
                return c;
        }
        throw new IllegalArgumentException("invalid direction " + dir);
    }

    public Cuboid expand(CuboidDirection dir, int amount) {
        switch (dir) {
            case NORTH:
                return new Cuboid(this.worldName, this.minX - amount, this.minY, this.minZ, this.maxX, this.maxY, this.maxZ);
            case SOUTH:
                return new Cuboid(this.worldName, this.minX, this.minY, this.minZ, this.maxX + amount, this.maxY, this.maxZ);
            case EAST:
                return new Cuboid(this.worldName, this.minX, this.minY, this.minZ - amount, this.maxX, this.maxY, this.maxZ);
            case WEST:
                return new Cuboid(this.worldName, this.minX, this.minY, this.minZ, this.maxX, this.maxY, this.maxZ + amount);
            case DOWN:
                return new Cuboid(this.worldName, this.minX, this.minY - amount, this.minZ, this.maxX, this.maxY, this.maxZ);
            case UP:
                return new Cuboid(this.worldName, this.minX, this.minY, this.minZ, this.maxX, this.maxY + amount, this.maxZ);
        }
        throw new IllegalArgumentException("invalid direction " + dir);
    }

    public Cuboid clone() {
        return new Cuboid(this);
    }
}
