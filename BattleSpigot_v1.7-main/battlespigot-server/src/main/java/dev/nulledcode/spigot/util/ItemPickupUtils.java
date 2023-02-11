package dev.nulledcode.spigot.util;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.spigotmc.SpigotConfig;

public class ItemPickupUtils {
    
    public static ItemStack handleDrops(Player player, ItemStack item) {
        org.bukkit.inventory.ItemStack finaldrop =  item.clone();;
        org.bukkit.inventory.ItemStack tool = player.getItemInHand();
        /*int xpToDrop = ThreadLocalRandom.current().nextInt(3, 6);
        if (item.getType().name().contains("ORE")) {
            player.giveExp(xpToDrop);
        }*/
        if (tool != null) {
            if (item.getType() == org.bukkit.Material.GOLD_ORE) {
                finaldrop = new org.bukkit.inventory.ItemStack(org.bukkit.Material.GOLD_INGOT, item.getAmount() * SpigotConfig.oremultiplier);
            } else if (item.getType() == org.bukkit.Material.IRON_ORE) {
                finaldrop = new org.bukkit.inventory.ItemStack(org.bukkit.Material.IRON_INGOT, item.getAmount() * SpigotConfig.oremultiplier);
            } else if (item.getType() == org.bukkit.Material.EMERALD_ORE || item.getType() == org.bukkit.Material.EMERALD) {
                finaldrop = new org.bukkit.inventory.ItemStack(org.bukkit.Material.EMERALD, item.getAmount() * SpigotConfig.oremultiplier);
            } else if (item.getType() == org.bukkit.Material.DIAMOND_ORE || item.getType() == org.bukkit.Material.DIAMOND) {
                finaldrop = new org.bukkit.inventory.ItemStack(org.bukkit.Material.DIAMOND, item.getAmount() * SpigotConfig.oremultiplier);
            } else if (item.getType() == org.bukkit.Material.REDSTONE || item.getType() == org.bukkit.Material.REDSTONE_ORE || item.getType() == org.bukkit.Material.GLOWING_REDSTONE_ORE) {
                finaldrop = new org.bukkit.inventory.ItemStack(org.bukkit.Material.REDSTONE, item.getAmount() * SpigotConfig.oremultiplier);
            } else if (item.getType() == org.bukkit.Material.LAPIS_ORE || (item.getType() == org.bukkit.Material.INK_SACK && item.getDurability() == 4)) {
                finaldrop = new org.bukkit.inventory.ItemStack(org.bukkit.Material.INK_SACK, item.getAmount() * SpigotConfig.oremultiplier, (short) 4);
            } else if (item.getType() == org.bukkit.Material.COAL || item.getType() == org.bukkit.Material.COAL_ORE) {
                finaldrop = new org.bukkit.inventory.ItemStack(org.bukkit.Material.COAL, item.getAmount() * SpigotConfig.oremultiplier);
            }
        }

        return finaldrop;
    }
}