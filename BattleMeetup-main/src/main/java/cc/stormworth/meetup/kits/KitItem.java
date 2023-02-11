package cc.stormworth.meetup.kits;

import org.bukkit.inventory.ItemStack;

public class KitItem {

    private final ItemStack item;

    public KitItem(final ItemStack item) {
        this.item = item;
    }

    public ItemStack getItem() {
        return this.item;
    }
}
