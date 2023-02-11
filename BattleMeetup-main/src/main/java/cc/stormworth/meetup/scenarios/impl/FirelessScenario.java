package cc.stormworth.meetup.scenarios.impl;

import cc.stormworth.meetup.scenarios.Scenario;
import cc.stormworth.meetup.util.Colors;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.inventory.ItemStack;

public class FirelessScenario extends Scenario implements Listener {
    public FirelessScenario() {
        super("Fireless", new ItemStack(Material.FLINT_AND_STEEL), false, "You cannot take damage from lava or fire.");
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerChangedWorld(PlayerChangedWorldEvent event) {
        Player player = event.getPlayer();
        if (player.getWorld().getName().equalsIgnoreCase("uhc_nether")) {
            player.sendMessage(Colors.RED + "Note: Fireless does not work in nether.");
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onEntityDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player) {
            if (notPlaying((Player) event.getEntity()) || event.getEntity().getWorld().getName().equals("uhc_nether"))
                return;
            if (event.getCause() == EntityDamageEvent.DamageCause.FIRE || event.getCause() == EntityDamageEvent.DamageCause.FIRE_TICK || event.getCause() == EntityDamageEvent.DamageCause.LAVA)
                event.setCancelled(true);
        }
    }
}
