package cc.stormworth.meetup.border.glass;

import org.bukkit.Location;
import org.bukkit.Material;

import java.beans.ConstructorProperties;

public class GlassInfo {
    private final GlassManager.GlassType type;

    private final Location location;

    private final Material material;

    private final byte data;

    @ConstructorProperties({"type", "location", "material", "data"})
    public GlassInfo(GlassManager.GlassType type, Location location, Material material, byte data) {
        this.type = type;
        this.location = location;
        this.material = material;
        this.data = data;
    }

    public GlassManager.GlassType getType() {
        return this.type;
    }

    public Location getLocation() {
        return this.location;
    }

    public Material getMaterial() {
        return this.material;
    }

    public byte getData() {
        return this.data;
    }
}
