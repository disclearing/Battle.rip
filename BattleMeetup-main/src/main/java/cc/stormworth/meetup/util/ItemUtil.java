package cc.stormworth.meetup.util;

import cc.stormworth.core.util.chat.CC;
import cc.stormworth.meetup.style.hcf.enchantment.CustomEnchant;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ItemUtil {

    private static final List<Material> allowedMaterials = Arrays.asList(
            Material.COBBLESTONE,
            Material.OBSIDIAN,
            Material.LEAVES,
            Material.LEAVES_2,
            Material.LONG_GRASS,
            Material.DOUBLE_PLANT,
            Material.CACTUS,
            Material.DEAD_BUSH,
            Material.YELLOW_FLOWER,
            Material.RED_ROSE,
            Material.WEB,
            Material.WATER,
            Material.STATIONARY_WATER,
            Material.LAVA,
            Material.STATIONARY_LAVA,
            Material.ANVIL,
            Material.FENCE,
            Material.SKULL,
            Material.SKULL_ITEM,
            Material.WORKBENCH
    );

    public static ItemStack getGoldenHead() {
        return (new ItemBuilder(Material.GOLDEN_APPLE)).setDurability(0).setName(Colors.GOLD + Colors.BOLD + "Golden Head").build();
    }

    public static ItemStack getGoldenHeads(int amount) {
        return (new ItemBuilder(Material.GOLDEN_APPLE)).setDurability(0).setName(Colors.GOLD + Colors.BOLD + "Golden Head").setAmount(amount).build();
    }

    public static List<Material> getAllowedMaterials() {
        return allowedMaterials;
    }

    public static boolean isHealPot(ItemStack item) {
        return item.getType() == Material.POTION && item.getDurability() == 16421;
    }

    public static List<CustomEnchant> getCustomEnchants(ItemStack item) {
        List<CustomEnchant> enchants = new ArrayList<>();

        if (item.getItemMeta() != null && item.getItemMeta().hasLore()) {
            for (String line : item.getItemMeta().getLore()) {
                CustomEnchant enchant = CustomEnchant.getByName(line.replace("§", "&"));
                if (enchant == null)
                    continue;
                enchants.add(enchant);
            }
        }
        return enchants;
    }

    public static void addCustomEnchant(ItemStack itemStack, CustomEnchant enchant) {
        ItemMeta meta = itemStack.getItemMeta();
        List<String> lore = meta.getLore();
        if (lore == null)
            lore = new ArrayList<>();
        lore.add(CC.translate(enchant.getName()));
        meta.setLore(lore);
        itemStack.setItemMeta(meta);
    }
}
