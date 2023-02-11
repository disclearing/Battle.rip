package cc.stormworth.meetup.style.hcf.kit;

import cc.stormworth.meetup.managers.UserManager;
import cc.stormworth.meetup.user.UserData;
import lombok.AccessLevel;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public abstract class HCFKit {

    public static final List<HCFKit> kits = new ArrayList<>();
    private final Inventory inventory = Bukkit.createInventory(null, InventoryType.PLAYER);
    @Setter(AccessLevel.PROTECTED)
    private ItemStack helmet, chestplate, leggings, boots;

    public HCFKit() {
        this.loadInventory();
        this.fill(new HCFKitItem(Material.POTION)
                .setDurability(16421)
                .get());

        kits.add(this);
    }

    public static HCFKit findAny() {
        return kits.get(ThreadLocalRandom.current().nextInt(kits.size()));
    }

    protected void add(ItemStack item) {
        this.inventory.addItem(item);
    }

    protected void set(ItemStack item, int slot) {
        this.inventory.setItem(slot, item);
    }

    protected void fill(ItemStack item) {
        IntStream.range(0, 36).filter(slot -> this.inventory.getItem(slot) == null).forEach(slot ->
                this.inventory.setItem(slot, item));
    }

    public void give(Player player) {
        UserData user = UserManager.getInstance().getUser(player.getUniqueId());
        HCFKitSortation sortation = user.getHcfSortation();
        List<ItemStack> otherItems = Arrays.stream(this.inventory.getContents()).collect(Collectors.toList());

        // Add items to sortation slots
        for (ItemStack item : this.inventory.getContents()) {
            int slot = sortation.getSlot(item);

            if (slot == -1) continue;

            player.getInventory().setItem(slot, item);
            otherItems.remove(item);
        }

        // Add other items
        otherItems.forEach(item -> player.getInventory().addItem(item));
        player.getInventory().setHelmet(this.helmet);
        player.getInventory().setChestplate(this.chestplate);
        player.getInventory().setLeggings(this.leggings);
        player.getInventory().setBoots(this.boots);
        player.updateInventory();
    }

    protected abstract void loadInventory();
}
