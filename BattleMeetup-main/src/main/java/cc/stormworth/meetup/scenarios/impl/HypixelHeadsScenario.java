package cc.stormworth.meetup.scenarios.impl;

import cc.stormworth.meetup.scenarios.Scenario;
import cc.stormworth.meetup.util.Colors;
import cc.stormworth.meetup.util.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Arrays;

public class HypixelHeadsScenario extends Scenario implements Listener {

    public HypixelHeadsScenario() {
        super("Hypixel Heads", new ItemStack(Material.SKULL_ITEM), true, "When you right click a head you will ", "get Regeneration III for 4 seconds ", "and Speed II for 20 seconds.");
    }

    public static ItemStack getSkullItem(String name) {
        return (new ItemBuilder(Material.SKULL_ITEM))
                .setName(Colors.GOLD + name)
                .setSkullOwner(name)
                .setDurability(1)
                .setLore(Arrays.asList(Colors.GRAY + "Right click to activate.", Colors.GRAY + "You'll get Regeneration 3 for 4s", Colors.GRAY + "and Speed 2 for 20s!")).build();
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerDeath(PlayerDeathEvent event) {
        if (notPlaying(event.getEntity()))
            return;
        event.getDrops().add(getSkullItem(event.getEntity().getName()));
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (notPlaying(player))
            return;
        ItemStack stack = player.getItemInHand();
        if (event.getAction().name().charAt(0) != 'R' || stack == null || stack.getType() != Material.SKULL_ITEM)
            return;
        event.setCancelled(true);
        if (player.hasPotionEffect(PotionEffectType.SPEED))
            player.removePotionEffect(PotionEffectType.SPEED);
        if (player.hasPotionEffect(PotionEffectType.REGENERATION))
            player.removePotionEffect(PotionEffectType.REGENERATION);
        player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 500, 1));
        player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 100, 2));
        if (player.getItemInHand().getAmount() == 1) {
            player.setItemInHand(new ItemStack(Material.AIR));
        } else {
            player.getItemInHand().setAmount(player.getItemInHand().getAmount() - 1);
        }
        player.updateInventory();
        player.sendMessage(Colors.SECONDARY + "You have eaten a Hypixel Head.");
    }
}
