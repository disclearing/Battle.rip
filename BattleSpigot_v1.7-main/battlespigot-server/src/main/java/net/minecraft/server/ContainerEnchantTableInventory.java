package net.minecraft.server;

// CraftBukkit start

import org.bukkit.craftbukkit.entity.CraftHumanEntity;
import org.bukkit.entity.HumanEntity;

import java.util.List;
// CraftBukkit end

public class ContainerEnchantTableInventory extends InventorySubcontainer { // CraftBukkit -> public

    final ContainerEnchantTable enchantTable;

    // CraftBukkit start
    public List<HumanEntity> transaction = new java.util.ArrayList<HumanEntity>();
    public org.bukkit.entity.Player player;
    private int maxStack = MAX_STACK;

    ContainerEnchantTableInventory(ContainerEnchantTable containerenchanttable, String s, boolean flag, int i) {
        super(s, flag, i);
        this.enchantTable = containerenchanttable;
        this.setMaxStackSize(1); // CraftBukkit
    }

    public ItemStack[] getContents() {
        return this.items;
    }

    public void onOpen(CraftHumanEntity who) {
        transaction.add(who);
    }

    public void onClose(CraftHumanEntity who) {
        transaction.remove(who);
    }

    public List<HumanEntity> getViewers() {
        return transaction;
    }

    public org.bukkit.inventory.InventoryHolder getOwner() {
        return this.player;
    }
    // CraftBukkit end

    public int getMaxStackSize() {
        return maxStack; // CraftBukkit
    }

    public void setMaxStackSize(int size) {
        maxStack = size;
    }

    public void update() {
        super.update();
        this.enchantTable.a(this);
    }
}
