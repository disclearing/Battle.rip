package cc.stormworth.meetup.style.hcf.kit.impl.insanestrength;

import cc.stormworth.meetup.ability.Ability;
import cc.stormworth.meetup.style.hcf.enchantment.CustomEnchant;
import cc.stormworth.meetup.style.hcf.enchantment.Effect;
import cc.stormworth.meetup.style.hcf.kit.HCFKit;
import cc.stormworth.meetup.style.hcf.kit.HCFKitItem;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.Listener;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

public class InsaneStrengthBowKit extends HCFKit implements Listener {

    @Override
    protected void loadInventory() {
        this.add(new HCFKitItem(Material.DIAMOND_SWORD)
                .setName(HCFKitItem.PREFIX_EVIL + "Sword")
                .addEnchantment(Enchantment.DAMAGE_ALL, 2)
                .addEnchantment(Enchantment.FIRE_ASPECT, 2)
                .addEnchantment(Enchantment.DURABILITY, 3)
                .get());
        this.add(new HCFKitItem(Material.BOW)
                .setName(HCFKitItem.PREFIX_BLOOD + "Bow")
                .addEnchantment(Enchantment.ARROW_DAMAGE, 5)
                .addEnchantment(Enchantment.ARROW_FIRE, 3)
                .addEnchantment(Enchantment.DURABILITY, 3)
                .addEnchantment(Enchantment.ARROW_INFINITE, 1)
                .get());
        this.add(new HCFKitItem(Material.ENDER_PEARL)
                .setAmount(5)
                .get());

        for (int i = 0; i < ThreadLocalRandom.current().nextInt(3); i++) {
            this.add(new ArrayList<>(Ability.getAbilities()).get(ThreadLocalRandom.current().nextInt(Ability.getAbilities().size())).getItem());
        }

        this.add(HCFKitItem.POTION_POISON);
        this.add(HCFKitItem.POTION_SLOWNESS);
        this.add(HCFKitItem.POTION_WEAKNESS);
        this.add(HCFKitItem.ROD);
        this.add(HCFKitItem.FOOD);
        this.add(HCFKitItem.ARROW);

        this.setHelmet(new HCFKitItem(Material.DIAMOND_HELMET)
                .setName(HCFKitItem.PREFIX_BLOOD + "Helmet")
                .addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2)
                .addEnchantment(Enchantment.DURABILITY, 3)
                .get());
        this.setChestplate(new HCFKitItem(Material.DIAMOND_CHESTPLATE)
                .setName(HCFKitItem.PREFIX_KOTH + "Chestplate")
                .addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 3)
                .addEnchantment(Enchantment.DURABILITY, 4)
                .addCustomEnchant(new CustomEnchant("&6Fire Resistance I", new Effect(PotionEffectType.FIRE_RESISTANCE, 1)))
                .get());
        this.setLeggings(new HCFKitItem(Material.DIAMOND_LEGGINGS)
                .setName(HCFKitItem.PREFIX_COMMUNITY + "Leggings")
                .addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1)
                .addEnchantment(Enchantment.DURABILITY, 2)
                .get());
        this.setBoots(new HCFKitItem(Material.DIAMOND_BOOTS)
                .setName(HCFKitItem.PREFIX_KOTH + "Boots")
                .addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 3)
                .addEnchantment(Enchantment.DURABILITY, 4)
                .addEnchantment(Enchantment.PROTECTION_FALL, 4)
                .addCustomEnchant(new CustomEnchant("&bSpeed II", new Effect(PotionEffectType.SPEED, 2)))
                .get());
    }
}