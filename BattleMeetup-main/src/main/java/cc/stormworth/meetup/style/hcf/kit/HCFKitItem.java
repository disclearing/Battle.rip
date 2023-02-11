package cc.stormworth.meetup.style.hcf.kit;

import cc.stormworth.core.util.chat.CC;
import cc.stormworth.meetup.style.hcf.enchantment.CustomEnchant;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Getter
public class HCFKitItem {

    public static final String PREFIX_EVIL = "&d&lEvil &7⎜ &r";
    public static final String PREFIX_BLOOD = "&c&lBlood &7⎜ &r";
    public static final String PREFIX_KOTH = "&4&l&oKoth &7⎜ &r";
    public static final String PREFIX_COMMUNITY = "&e&lCommunity &7⎜ &r";

    public static final ItemStack ROD = new HCFKitItem(Material.FISHING_ROD).get();
    public static final ItemStack ARROW = new HCFKitItem(Material.ARROW).setAmount(64).get();
    public static final ItemStack FOOD = new HCFKitItem(Material.COOKED_BEEF).setAmount(16).get();

    public static final ItemStack POTION_POISON = new HCFKitItem(Material.POTION).setDurability(16388).get();
    public static final ItemStack POTION_SLOWNESS = new HCFKitItem(Material.POTION).setDurability(16426).get();
    public static final ItemStack POTION_WEAKNESS = new HCFKitItem(Material.POTION).setDurability(16424).get();
    public static final ItemStack POTION_HARMING = new HCFKitItem(Material.POTION).setDurability(16428).get();

    private final ItemStack item;
    private final List<CustomEnchant> abilities = new ArrayList<>();

    public HCFKitItem(Material material) {
        this.item = new ItemStack(material);
    }

    public HCFKitItem setName(String name) {
        ItemMeta meta = this.item.getItemMeta();
        meta.setDisplayName(CC.translate(name));
        this.item.setItemMeta(meta);
        return this;
    }

    public HCFKitItem addLoreLine(String name) {
        ItemMeta meta = this.item.getItemMeta();
        List<String> lore = meta.getLore();
        if (lore == null)
            lore = new ArrayList<>();
        lore.add(CC.translate(name));
        meta.setLore(lore);
        this.item.setItemMeta(meta);
        return this;
    }

    public HCFKitItem setLore(List<String> lore) {
        List<String> toSet = new ArrayList<>();
        ItemMeta meta = this.item.getItemMeta();
        lore.stream().filter(Objects::nonNull).forEach((string) -> toSet.add(CC.translate(string)));
        meta.setLore(toSet);
        this.item.setItemMeta(meta);
        return this;
    }

    public HCFKitItem setLore(String... lore) {
        List<String> toSet = new ArrayList<>();
        ItemMeta meta = this.item.getItemMeta();
        Arrays.asList(lore).forEach((string) -> toSet.add(CC.translate(string)));
        meta.setLore(toSet);
        this.item.setItemMeta(meta);
        return this;
    }

    public HCFKitItem setLore(List<String>... lores) {
        List<String> toSet = new ArrayList<>();
        ItemMeta meta = this.item.getItemMeta();
        Arrays.asList(lores).forEach((string) -> toSet.addAll(CC.translate(string)));
        meta.setLore(toSet);
        this.item.setItemMeta(meta);
        return this;
    }

    public HCFKitItem setAmount(int amount) {
        this.item.setAmount(amount);
        return this;
    }

    public HCFKitItem setDurability(int durability) {
        if (durability != -1)
            this.item.setDurability((short) durability);
        return this;
    }

    public HCFKitItem addEnchantment(Enchantment enchantment, int level) {
        if (enchantment != null)
            this.item.addUnsafeEnchantment(enchantment, level);
        return this;
    }

    public HCFKitItem addEnchantment(Enchantment enchantment) {
        if (enchantment != null)
            this.item.addUnsafeEnchantment(enchantment, 1);
        return this;
    }

    public HCFKitItem addCustomEnchant(CustomEnchant enchant) {
        this.abilities.add(enchant);
        this.addLoreLine(enchant.getName());
        return this;
    }

    public ItemStack get() {
        return this.item;
    }
}
