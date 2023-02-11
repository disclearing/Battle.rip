package cc.stormworth.meetup.scenarios.impl;

import cc.stormworth.meetup.scenarios.Scenario;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;

public class NoFallScenario extends Scenario implements Listener {
    public NoFallScenario() {
        super("NoFall", new ItemStack(Material.FEATHER), false, "You cannot take any fall damage.");
    }

    @EventHandler(ignoreCancelled = true)
    public void onEntityDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player) {
            if (notPlaying((Player) event.getEntity()))
                return;
            if (event.getCause() == EntityDamageEvent.DamageCause.FALL)
                event.setCancelled(true);
        }
    }
}
