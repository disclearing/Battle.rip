package dev.nulledcode.spigot.command;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class PvPArmorCommand extends org.bukkit.command.Command {
    public PvPArmorCommand(String name) {
        super(name);
        this.usageMessage = (ChatColor.DARK_AQUA + "Usage: /pvparmor");
        setPermission("battlespigot.pvparmor");
    }

    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        if (!testPermission(sender)) return true;
        ItemStack helmet = new ItemStack(Material.DIAMOND_HELMET);
        helmet.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2);
        ItemStack chest = new ItemStack(Material.DIAMOND_CHESTPLATE);
        chest.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2);
        ItemStack leg = new ItemStack(Material.DIAMOND_LEGGINGS);
        leg.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2);
        ItemStack boot = new ItemStack(Material.DIAMOND_BOOTS);
        boot.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2);
        ItemStack sword = new ItemStack(Material.DIAMOND_SWORD);
        sword.addEnchantment(Enchantment.DAMAGE_ALL, 3);
        sword.addEnchantment(Enchantment.FIRE_ASPECT, 2);
        ItemStack speed = new ItemStack(Material.POTION, 1, (short) 8226);
        ItemStack fres = new ItemStack(Material.POTION, 1, (short) 8259);
        ItemStack health = new ItemStack(Material.POTION, 1, (short) 16421);

        if (args.length == 0) {
            Player player = (Player) sender;
            player.getInventory().clear();
            player.setHealth(20);
            player.setFoodLevel(20);
            player.getInventory().setHelmet(helmet);
            player.getInventory().setChestplate(chest);
            player.getInventory().setLeggings(leg);
            player.getInventory().setBoots(boot);
            player.getInventory().setItem(0, sword);
            player.getInventory().setItem(1, new ItemStack(Material.ENDER_PEARL, 16));
            player.getInventory().setItem(8, new ItemStack(Material.COOKED_BEEF, 64));
            player.getInventory().setItem(2, speed);
            player.getInventory().setItem(3, fres);
            player.getInventory().setItem(26, speed);
            player.getInventory().setItem(35, speed);
            for (int i = 0; i < 30; i++) {
                player.getInventory().addItem(health);
            }
        } else {
            sender.sendMessage(usageMessage);
            return true;
        }

        return false;
    }
}