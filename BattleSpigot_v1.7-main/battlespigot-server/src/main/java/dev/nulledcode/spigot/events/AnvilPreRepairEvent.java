package dev.nulledcode.spigot.events;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.event.HandlerList;
import org.bukkit.event.inventory.InventoryEvent;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;

@Getter @Setter
public class AnvilPreRepairEvent extends InventoryEvent {
    private static final HandlerList handlers = new HandlerList();
    private final ItemStack item;
    private int cost;

    public AnvilPreRepairEvent(InventoryView transaction, ItemStack item, int cost) {
        super(transaction);

        this.item = item;
        this.cost = cost;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }
}
