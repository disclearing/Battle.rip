package cc.stormworth.meetup.scenarios.impl;

import cc.stormworth.meetup.scenarios.Scenario;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class WebCageScenario extends Scenario implements Listener {
    public WebCageScenario() {
        super("WebCage", new ItemStack(Material.WEB), true, "Upon killing a player, a sphere", "of cobwebs will surround their corpse.");
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerDeath(PlayerDeathEvent event) {
        if (notPlaying(event.getEntity()))
            return;
        if (event.getEntity().getKiller() == null)
            return;
        List<Location> locations = getSphere(event.getEntity().getLocation());
        for (Location blocks : locations) {
            if (blocks.getBlock().getType() == Material.AIR || blocks.getBlock().getType() == Material.LONG_GRASS || blocks.getBlock().getType() == Material.DOUBLE_PLANT)
                blocks.getBlock().setType(Material.WEB);
        }
    }

    private List<Location> getSphere(Location centerBlock) {
        List<Location> circleBlocks = new ArrayList<>();
        int bx = centerBlock.getBlockX();
        int by = centerBlock.getBlockY();
        int bz = centerBlock.getBlockZ();
        for (int x = bx - 5; x <= bx + 5; x++) {
            for (int y = by - 5; y <= by + 5; y++) {
                for (int z = bz - 5; z <= bz + 5; z++) {
                    double distance = ((bx - x) * (bx - x) + (bz - z) * (bz - z) + (by - y) * (by - y));
                    if (distance < 25.0D && distance >= 16.0D) {
                        Location l = new Location(centerBlock.getWorld(), x, y, z);
                        circleBlocks.add(l);
                    }
                }
            }
        }
        return circleBlocks;
    }
}
