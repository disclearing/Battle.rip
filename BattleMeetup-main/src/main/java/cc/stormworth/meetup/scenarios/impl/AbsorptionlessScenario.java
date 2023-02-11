package cc.stormworth.meetup.scenarios.impl;

import cc.stormworth.meetup.Meetup;
import cc.stormworth.meetup.scenarios.Scenario;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

public class AbsorptionlessScenario extends Scenario implements Listener {

    public AbsorptionlessScenario() {
        super("Absorptionless", new ItemStack(Material.GOLDEN_APPLE), false, "Absorption is disabled.");
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onEat(PlayerItemConsumeEvent e) {
        Player p = e.getPlayer();

        if (e.getItem() != null) {

            if (e.getItem().getType() == Material.GOLDEN_APPLE) {

                new BukkitRunnable() {

                    @Override
                    public void run() {
                        p.removePotionEffect(PotionEffectType.ABSORPTION);
                    }
                }.runTaskLater(Meetup.getInstance(), 1L);
            }
        }
    }
}
