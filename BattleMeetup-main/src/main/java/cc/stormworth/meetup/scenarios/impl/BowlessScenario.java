package cc.stormworth.meetup.scenarios.impl;

import cc.stormworth.meetup.scenarios.Scenario;
import cc.stormworth.meetup.util.Colors;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class BowlessScenario extends Scenario implements Listener {
    public BowlessScenario() {
        super("Bowless", new ItemStack(Material.BOW), false, "Bows cannot be crafted or used.");
    }

    @EventHandler
    public void onCraftItem(CraftItemEvent event) {
        if (notPlaying((Player) event.getView().getPlayer()))
            return;
        if (event.getRecipe().getResult().getType() == Material.BOW) {
            event.getInventory().setResult(new ItemStack(Material.AIR));
            ((Player) event.getView().getPlayer()).sendMessage(Colors.RED + "You cannot craft bows while Bowless is enabled.");
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getItem() != null && event.getItem().getType() == Material.BOW) {
            if (notPlaying(event.getPlayer()))
                return;
            event.getPlayer().setItemInHand(null);
            event.getPlayer().updateInventory();
            event.getPlayer().sendMessage(Colors.RED + "You cannot use bows while Bowless is enabled.");
        }
    }
}
