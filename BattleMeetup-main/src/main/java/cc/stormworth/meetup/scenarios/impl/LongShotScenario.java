package cc.stormworth.meetup.scenarios.impl;

import cc.stormworth.core.CorePluginAPI;
import cc.stormworth.meetup.scenarios.Scenario;
import cc.stormworth.meetup.util.Colors;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

public class LongShotScenario extends Scenario implements Listener {
    public LongShotScenario() {
        super("Long Shots", new ItemStack(Material.ARROW), true, "If you land a shot from more than 50 blocks,", "you will be healed by one heart and", "you do 1.5 times the damage you normally would.");
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof Player))
            return;
        if (!(event.getDamager() instanceof Arrow))
            return;
        Arrow arrow = (Arrow) event.getDamager();
        if (!(arrow.getShooter() instanceof Player))
            return;
        Player shooter = (Player) arrow.getShooter();
        Player damaged = (Player) event.getEntity();
        Location shooterLocation = shooter.getLocation();
        Location damagedLocation = damaged.getLocation();
        int blocks = (int) shooterLocation.distance(damagedLocation);
        if (blocks >= 50) {
            event.setDamage(event.getDamage() * 1.5D);
            if (shooter.getHealth() > 18.0D) {
                shooter.setHealth(20.0D);
            } else {
                shooter.setHealth(shooter.getHealth() + 2.0D);
            }
            shooter.sendMessage(Colors.SECONDARY + "You have landed a long shot from " + Colors.PRIMARY + blocks + " blocks" + Colors.SECONDARY + ".");
            damaged.sendMessage(CorePluginAPI.getProfile(shooter.getUniqueId()).getColoredUsername() + Colors.SECONDARY + " has landed a long shot on you from " + Colors.PRIMARY + blocks + " blocks" + Colors.SECONDARY + ".");
        }
    }
}
