package cc.stormworth.meetup.style.hcf.kit.menu;

import cc.stormworth.meetup.managers.UserManager;
import cc.stormworth.meetup.style.hcf.kit.HCFKitItem;
import cc.stormworth.meetup.style.hcf.kit.HCFKitSortation;
import cc.stormworth.meetup.util.ItemBuilder;
import cc.stormworth.meetup.util.NumberUtil;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.stream.IntStream;

public class HCFEditorMenu {

    @Getter
    private static final Map<UUID, Inventory> editors = new HashMap<>();

    private final Player player;
    private final Inventory inventory = Bukkit.createInventory(null, 36, "HCF Kit Editor");

    public HCFEditorMenu(Player player) {
        this.player = player;

        editors.put(this.player.getUniqueId(), this.inventory);
    }

    public void openInventory() {
        HCFKitSortation kitSortation = UserManager.getInstance().getUser(player.getUniqueId()).getHcfSortation();

        this.inventory.clear();
        this.inventory.setItem(NumberUtil.convertEditorSlot(kitSortation.getAbilitySlot()), new ItemBuilder(Material.NETHER_STAR).setName("&6Ability Item").addEnchant(Enchantment.DURABILITY, 10).build());
        this.inventory.setItem(NumberUtil.convertEditorSlot(kitSortation.getSwordSlot()), new ItemBuilder(Material.DIAMOND_SWORD).build());
        this.inventory.setItem(NumberUtil.convertEditorSlot(kitSortation.getPearlSlot()), new ItemBuilder(Material.ENDER_PEARL).build());
        this.inventory.setItem(NumberUtil.convertEditorSlot(kitSortation.getRodSlot()), new ItemBuilder(Material.FISHING_ROD).build());
        this.inventory.setItem(NumberUtil.convertEditorSlot(kitSortation.getBowSlot()), new ItemBuilder(Material.BOW).build());
        this.inventory.setItem(NumberUtil.convertEditorSlot(kitSortation.getFoodSlot()), new ItemBuilder(Material.COOKED_BEEF).build());
        this.inventory.setItem(NumberUtil.convertEditorSlot(kitSortation.getArrowSlot()), new ItemBuilder(Material.ARROW).build());
        this.inventory.setItem(NumberUtil.convertEditorSlot(kitSortation.getWebSlot()), new ItemBuilder(Material.WEB).build());
        this.inventory.setItem(NumberUtil.convertEditorSlot(kitSortation.getFirstPotionSlot()), HCFKitItem.POTION_POISON);
        this.inventory.setItem(NumberUtil.convertEditorSlot(kitSortation.getSecondPotionSlot()), HCFKitItem.POTION_SLOWNESS);
        this.inventory.setItem(NumberUtil.convertEditorSlot(kitSortation.getThirdPotionSlot()), HCFKitItem.POTION_WEAKNESS);
        this.inventory.setItem(NumberUtil.convertEditorSlot(kitSortation.getFourthPotionSlot()), HCFKitItem.POTION_HARMING);
        //this.fill(new ItemBuilder(Material.POTION).setDurability(16421).build());
        this.player.openInventory(this.inventory);
    }

    private void fill(ItemStack item) {
        IntStream.range(0, 36).filter(slot -> this.inventory.getItem(slot) == null).forEach(slot ->
                this.inventory.setItem(slot, item));
    }
}
