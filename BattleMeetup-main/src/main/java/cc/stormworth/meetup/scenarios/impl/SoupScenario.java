package cc.stormworth.meetup.scenarios.impl;

import cc.stormworth.meetup.scenarios.Scenario;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class SoupScenario extends Scenario implements Listener {

    public SoupScenario() {
        super("Soup", new ItemStack(Material.MUSHROOM_SOUP), true, "Upon right clicking a soup,", "you will instantly regain 2 hearts.");
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        Player p = e.getPlayer();

        if (p.getHealth() < 20.0D) {

            if (((e.getAction() == Action.RIGHT_CLICK_AIR) || (e.getAction() == Action.RIGHT_CLICK_BLOCK)) && (e.getItem() != null) && (e.getItem().getType() == Material.MUSHROOM_SOUP)) {
                e.setCancelled(true);
                p.getItemInHand().setType(Material.BOWL);
                p.setHealth(Math.min(p.getHealth() + 4.0D, p.getMaxHealth()));
            }
        }
    }
}
