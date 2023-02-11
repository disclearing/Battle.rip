package rip.battle.entity.hologram.impl;

import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import rip.battle.entity.hologram.CraftHologram;
import rip.battle.entity.hologram.lines.HologramLine;

@Setter
public class UpdatableHologram extends CraftHologram {

    private long lastUpdate = System.currentTimeMillis();

    public UpdatableHologram(String name, String line, Location location) {
        super(name, line, location);
    }

    public UpdatableHologram(String name, ItemStack line, Location location) {
        super(name, line, location);
    }

    public UpdatableHologram(String name, Location location) {
        super(name, location);
    }

    public void update(Player player){
        for (HologramLine<?> line : getLines()){
            line.update(player);
        }
    }

    public int getUpdateInterval(){
        return 1;
    }

    public boolean canUpdate() {
        return System.currentTimeMillis() - lastUpdate > getUpdateInterval();
    }
}
