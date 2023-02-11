package cc.stormworth.meetup.util;

import cc.stormworth.meetup.kits.KitItem;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

public class KitUtil {

    // Swords:
    public static final KitItem SHARP_4_DIAMOND_SWORD;
    public static final KitItem SHARP_4_IRON_SWORD;
    public static final KitItem SHARP_3_FIRE_1_IRON_SWORD;
    public static final KitItem SHARP_3_DIAMOND_SWORD;
    public static final KitItem SHARP_2_DIAMOND_SWORD;
    public static final KitItem SHARP_3_IRON_SWORD;
    public static final KitItem SHARP_2_FIRE_1_IRON_SWORD;

    // Armor:
    public static final KitItem PROT_3_DIAMOND_HELMET;
    public static final KitItem PROT_3_DIAMOND_CHESTPLATE;
    public static final KitItem PROT_3_DIAMOND_LEGGINGS;
    public static final KitItem PROT_3_DIAMOND_BOOTS;
    public static final KitItem PROT_2_DIAMOND_HELMET;
    public static final KitItem PROT_2_DIAMOND_CHESTPLATE;
    public static final KitItem PROT_2_DIAMOND_LEGGINGS;
    public static final KitItem PROT_2_DIAMOND_BOOTS;
    public static final KitItem PROT_1_DIAMOND_HELMET;
    public static final KitItem PROT_1_DIAMOND_CHESTPLATE;
    public static final KitItem PROT_1_DIAMOND_LEGGINGS;
    public static final KitItem PROT_1_DIAMOND_BOOTS;
    public static final KitItem PROJ_1_DIAMOND_HELMET;
    public static final KitItem PROJ_1_DIAMOND_CHESTPLATE;
    public static final KitItem PROJ_1_DIAMOND_LEGGINGS;
    public static final KitItem PROJ_1_DIAMOND_BOOTS;

    public static final KitItem PROT_1_IRON_HELMET;
    public static final KitItem PROT_1_IRON_CHESTPLATE;
    public static final KitItem PROT_1_IRON_LEGGINGS;
    public static final KitItem PROT_1_IRON_BOOTS;
    public static final KitItem PROJ_1_IRON_HELMET;
    public static final KitItem PROJ_1_IRON_CHESTPLATE;
    public static final KitItem PROJ_1_IRON_LEGGINGS;
    public static final KitItem PROJ_1_IRON_BOOTS;
    public static final KitItem IRON_HELMET;
    public static final KitItem IRON_CHESTPLATE;
    public static final KitItem IRON_LEGGINGS;
    public static final KitItem IRON_BOOTS;

    // Bows:
    public static final KitItem POWER_3_BOW;
    public static final KitItem POWER_2_BOW;
    public static final KitItem POWER_1_BOW;
    public static final KitItem POWER_1_FLAME_1_BOW;
    public static final KitItem POWER_2_FLAME_1_BOW;
    public static final KitItem POWER_1_PUNCH_1_BOW;
    public static final KitItem PUNCH_2_BOW;
    public static final KitItem NO_BOW;

    // Potions:
    public static final KitItem SPEED_1_POTION;
    public static final KitItem FIRERESISTANCE_POTION;

    // Secondaries:
    public static final KitItem COBWEB;
    public static final KitItem FNS;
    public static final KitItem HORSE;

    static {
        // Swords:
        SHARP_4_DIAMOND_SWORD = new KitItem(new ItemBuilder(Material.DIAMOND_SWORD).addEnchant(Enchantment.DAMAGE_ALL, 4).build());
        SHARP_4_IRON_SWORD = new KitItem(new ItemBuilder(Material.IRON_SWORD).addEnchant(Enchantment.DAMAGE_ALL, 4).build());
        SHARP_3_FIRE_1_IRON_SWORD = new KitItem(new ItemBuilder(Material.IRON_SWORD).addEnchant(Enchantment.DAMAGE_ALL, 3).addEnchant(Enchantment.FIRE_ASPECT, 1).build());
        SHARP_3_DIAMOND_SWORD = new KitItem(new ItemBuilder(Material.DIAMOND_SWORD).addEnchant(Enchantment.DAMAGE_ALL, 3).build());
        SHARP_2_DIAMOND_SWORD = new KitItem(new ItemBuilder(Material.DIAMOND_SWORD).addEnchant(Enchantment.DAMAGE_ALL, 2).build());
        SHARP_3_IRON_SWORD = new KitItem(new ItemBuilder(Material.IRON_SWORD).addEnchant(Enchantment.DAMAGE_ALL, 3).build());
        SHARP_2_FIRE_1_IRON_SWORD = new KitItem(new ItemBuilder(Material.IRON_SWORD).addEnchant(Enchantment.DAMAGE_ALL, 2).addEnchant(Enchantment.FIRE_ASPECT, 1).build());

        // Armor:
        PROT_3_DIAMOND_HELMET = new KitItem(new ItemBuilder(Material.DIAMOND_HELMET).addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 3).build());
        PROT_3_DIAMOND_CHESTPLATE = new KitItem(new ItemBuilder(Material.DIAMOND_CHESTPLATE).addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 3).build());
        PROT_3_DIAMOND_LEGGINGS = new KitItem(new ItemBuilder(Material.DIAMOND_LEGGINGS).addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 3).build());
        PROT_3_DIAMOND_BOOTS = new KitItem(new ItemBuilder(Material.DIAMOND_BOOTS).addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 3).build());
        PROT_2_DIAMOND_HELMET = new KitItem(new ItemBuilder(Material.DIAMOND_HELMET).addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 2).build());
        PROT_2_DIAMOND_CHESTPLATE = new KitItem(new ItemBuilder(Material.DIAMOND_CHESTPLATE).addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 2).build());
        PROT_2_DIAMOND_LEGGINGS = new KitItem(new ItemBuilder(Material.DIAMOND_LEGGINGS).addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 2).build());
        PROT_2_DIAMOND_BOOTS = new KitItem(new ItemBuilder(Material.DIAMOND_BOOTS).addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 2).build());
        PROT_1_DIAMOND_HELMET = new KitItem(new ItemBuilder(Material.DIAMOND_HELMET).addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1).build());
        PROT_1_DIAMOND_CHESTPLATE = new KitItem(new ItemBuilder(Material.DIAMOND_CHESTPLATE).addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1).build());
        PROT_1_DIAMOND_LEGGINGS = new KitItem(new ItemBuilder(Material.DIAMOND_LEGGINGS).addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1).build());
        PROT_1_DIAMOND_BOOTS = new KitItem(new ItemBuilder(Material.DIAMOND_BOOTS).addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1).build());
        PROJ_1_DIAMOND_HELMET = new KitItem(new ItemBuilder(Material.DIAMOND_HELMET).addEnchant(Enchantment.PROTECTION_PROJECTILE, 1).build());
        PROJ_1_DIAMOND_CHESTPLATE = new KitItem(new ItemBuilder(Material.DIAMOND_CHESTPLATE).addEnchant(Enchantment.PROTECTION_PROJECTILE, 1).build());
        PROJ_1_DIAMOND_LEGGINGS = new KitItem(new ItemBuilder(Material.DIAMOND_LEGGINGS).addEnchant(Enchantment.PROTECTION_PROJECTILE, 1).build());
        PROJ_1_DIAMOND_BOOTS = new KitItem(new ItemBuilder(Material.DIAMOND_BOOTS).addEnchant(Enchantment.PROTECTION_PROJECTILE, 1).build());

        PROT_1_IRON_HELMET = new KitItem(new ItemBuilder(Material.IRON_HELMET).addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1).build());
        PROT_1_IRON_CHESTPLATE = new KitItem(new ItemBuilder(Material.IRON_CHESTPLATE).addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1).build());
        PROT_1_IRON_LEGGINGS = new KitItem(new ItemBuilder(Material.IRON_LEGGINGS).addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1).build());
        PROT_1_IRON_BOOTS = new KitItem(new ItemBuilder(Material.IRON_BOOTS).addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1).build());
        PROJ_1_IRON_HELMET = new KitItem(new ItemBuilder(Material.IRON_HELMET).addEnchant(Enchantment.PROTECTION_PROJECTILE, 1).build());
        PROJ_1_IRON_CHESTPLATE = new KitItem(new ItemBuilder(Material.IRON_CHESTPLATE).addEnchant(Enchantment.PROTECTION_PROJECTILE, 1).build());
        PROJ_1_IRON_LEGGINGS = new KitItem(new ItemBuilder(Material.IRON_LEGGINGS).addEnchant(Enchantment.PROTECTION_PROJECTILE, 1).build());
        PROJ_1_IRON_BOOTS = new KitItem(new ItemBuilder(Material.IRON_BOOTS).addEnchant(Enchantment.PROTECTION_PROJECTILE, 1).build());
        IRON_HELMET = new KitItem(new ItemBuilder(Material.IRON_HELMET).build());
        IRON_CHESTPLATE = new KitItem(new ItemBuilder(Material.IRON_CHESTPLATE).build());
        IRON_LEGGINGS = new KitItem(new ItemBuilder(Material.IRON_LEGGINGS).build());
        IRON_BOOTS = new KitItem(new ItemBuilder(Material.IRON_BOOTS).build());

        // Bows:
        POWER_3_BOW = new KitItem(new ItemBuilder(Material.BOW).addEnchant(Enchantment.ARROW_DAMAGE, 3).build());
        POWER_2_BOW = new KitItem(new ItemBuilder(Material.BOW).addEnchant(Enchantment.ARROW_DAMAGE, 2).build());
        POWER_1_BOW = new KitItem(new ItemBuilder(Material.BOW).addEnchant(Enchantment.ARROW_DAMAGE, 1).build());
        POWER_1_FLAME_1_BOW = new KitItem(new ItemBuilder(Material.BOW).addEnchant(Enchantment.ARROW_DAMAGE, 1).addEnchant(Enchantment.ARROW_FIRE, 1).build());
        POWER_2_FLAME_1_BOW = new KitItem(new ItemBuilder(Material.BOW).addEnchant(Enchantment.ARROW_DAMAGE, 2).addEnchant(Enchantment.ARROW_FIRE, 1).build());
        POWER_1_PUNCH_1_BOW = new KitItem(new ItemBuilder(Material.BOW).addEnchant(Enchantment.ARROW_DAMAGE, 1).addEnchant(Enchantment.ARROW_KNOCKBACK, 1).build());
        PUNCH_2_BOW = new KitItem(new ItemBuilder(Material.BOW).addEnchant(Enchantment.ARROW_KNOCKBACK, 2).build());
        NO_BOW = new KitItem(new ItemBuilder(Material.AIR).build());

        // Potions:
        SPEED_1_POTION = new KitItem(new ItemBuilder(Material.POTION).setDurability((short) 8194).build());
        FIRERESISTANCE_POTION = new KitItem(new ItemBuilder(Material.POTION).setDurability((short) 8195).build());

        // Secondaries:
        COBWEB = new KitItem(new ItemBuilder(Material.WEB).setAmount(6).build());
        FNS = new KitItem(new ItemBuilder(Material.FLINT_AND_STEEL).build());
        HORSE = new KitItem(new ItemBuilder(Material.MONSTER_EGG).setDurability((short) 100).build());
    }
}
