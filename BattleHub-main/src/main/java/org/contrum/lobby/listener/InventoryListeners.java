package org.contrum.lobby.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.BeaconInventory;
import org.bukkit.inventory.EnchantingInventory;

public class InventoryListeners implements Listener {

    @EventHandler
    public void onInventoryOpen(InventoryOpenEvent event) {
        if (
                event.getInventory() instanceof EnchantingInventory ||
                event.getInventory() instanceof AnvilInventory ||
                event.getInventory() instanceof BeaconInventory
        ) event.setCancelled(true);
    }
}
