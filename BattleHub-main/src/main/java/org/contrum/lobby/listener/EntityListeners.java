package org.contrum.lobby.listener;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;

public class EntityListeners implements Listener {

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        event.setCancelled(true);

        if(!(event instanceof Player)) return;
        if(!event.getCause().equals(EntityDamageEvent.DamageCause.VOID)) return;
        event.getEntity().teleport(Bukkit.getWorlds().get(0).getSpawnLocation().clone().add(0.5, 0, 0.5));
    }

    @EventHandler
    public void onFoodChange(FoodLevelChangeEvent event) {
        event.setFoodLevel(20);
    }

}
