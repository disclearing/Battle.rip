package cc.stormworth.meetup.style.hcf.enchantment.command;

import cc.stormworth.core.util.command.annotations.Command;
import cc.stormworth.meetup.style.hcf.enchantment.CustomEnchant;
import cc.stormworth.meetup.style.hcf.enchantment.Effect;
import cc.stormworth.meetup.util.ItemUtil;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;

public class CustomEnchantCommand {

    @Command(names = "customenchant add speed", permission = "op")
    public static void add(Player player) {
        ItemStack hand = player.getInventory().getItemInHand();

        if (hand == null) {
            player.sendMessage("You must be holding an item to add a custom enchant.");
            return;
        }

        ItemUtil.addCustomEnchant(hand, new CustomEnchant("&bSpeed II", new Effect(PotionEffectType.SPEED, 2)));
    }

    @Command(names = "customenchant add fireresistance", permission = "op")
    public static void addFire(Player player) {
        ItemStack hand = player.getInventory().getItemInHand();

        if (hand == null) {
            player.sendMessage("You must be holding an item to add a custom enchant.");
            return;
        }

        ItemUtil.addCustomEnchant(hand, new CustomEnchant("&6Fire Resistance I", new Effect(PotionEffectType.FIRE_RESISTANCE, 1)));
    }

}
