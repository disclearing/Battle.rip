package cc.stormworth.meetup.kits;

import cc.stormworth.core.util.chat.CC;
import cc.stormworth.meetup.Meetup;
import cc.stormworth.meetup.managers.KitManager;
import cc.stormworth.meetup.managers.UserManager;
import cc.stormworth.meetup.user.UserData;
import cc.stormworth.meetup.util.StringUtil;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.*;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class KitEditorMenu implements Listener {
    private final Map<UUID, Inventory> kitEditors;

    public KitEditorMenu() {
        this.kitEditors = new HashMap<>();
    }

    public void openKitEditor(Player player) {
        if (this.kitEditors.containsKey(player.getUniqueId())) {
            player.openInventory(this.kitEditors.get(player.getUniqueId()));
            return;
        }
        Inventory inventory = Bukkit.createInventory(null, InventoryType.PLAYER, "UHC Kit Editor");
        this.kitEditors.put(player.getUniqueId(), inventory);
        UserData meetupPlayer = UserManager.getInstance().getUser(player.getUniqueId());
        Map<Integer, ItemStack> stackMap = new HashMap<>(KitManager.getInstance().getKitSortation(meetupPlayer.getKitItemContainer(), player.getPlayer()));
        for (ItemStack value : stackMap.values()) {
            value.setAmount(1);
        }
        stackMap.keySet().forEach(slot -> inventory.setItem((slot < 9) ? (slot + 27) : (slot - 9), stackMap.get(slot)));
        player.openInventory(inventory);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onInventoryClose(InventoryCloseEvent event) {
        Player player = (Player) event.getPlayer();
        UserData user = UserManager.getInstance().getUser(player.getUniqueId());
        if (this.kitEditors.containsKey(player.getUniqueId())) {
            Inventory inventory = this.kitEditors.get(player.getUniqueId());
            ItemStack[] matrix = new ItemStack[inventory.getSize()];
            for (int slot = 0; slot < matrix.length; ++slot) {
                matrix[slot] = inventory.getItem((slot < 9) ? (slot + 27) : (slot - 9));
            }
            user.setSortation(KitInventorySortation.of(matrix));
            user.getStatistics().setInventory(StringUtil.inventoryToString(inventory));
            this.kitEditors.remove(player.getUniqueId());
            player.sendMessage(CC.SECONDARY + "Your kit layout for game style " + CC.PRIMARY + "UHC " + CC.SECONDARY + "has been saved.");
        }
    }

    @EventHandler
    public void onInventoryDrop(PlayerDropItemEvent event) {
        if (this.kitEditors.containsKey(event.getPlayer().getUniqueId())) {
            event.setCancelled(true);
            event.getPlayer().updateInventory();
        }
        if (event.getPlayer().getWorld().getName().equals("world")) {
            new BukkitRunnable() {
                public void run() {
                    ItemStack itemStack = event.getItemDrop().getItemStack();
                    if (itemStack.getItemMeta().getDisplayName() == null || !itemStack.getItemMeta().getDisplayName().startsWith("�") || itemStack.getType() == Material.GOLDEN_APPLE) {
                        event.getPlayer().getInventory().remove(itemStack);
                    }
                }
            }.runTaskLater(Meetup.getInstance(), 3L);
        }
    }

    @EventHandler
    public void onInventoryDropClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();

        if (this.kitEditors.containsKey(player.getUniqueId())) {
            if (event.getClickedInventory() == null) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onInventoryMove(InventoryInteractEvent event) {
        if (this.kitEditors.containsKey(event.getWhoClicked().getUniqueId()) && event.getInventory() != this.kitEditors.get(event.getWhoClicked().getUniqueId())) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onInventoryMove(InventoryMoveItemEvent event) {
        if (this.kitEditors.containsValue(event.getSource())) {
            Inventory inventory = null;
            for (Inventory inv : this.kitEditors.values()) {
                if (inv.getTitle().equals(event.getSource().getTitle())) {
                    inventory = inv;
                }
            }
            if (inventory != null && event.getDestination() != inventory) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onInventoryClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        if (this.kitEditors.containsKey(player.getUniqueId())) {
            if (event.getRawSlot() > 35 || !validClick(event.getClick())) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onInventoryMove(InventoryDragEvent event) {
        if (this.kitEditors.containsKey(event.getWhoClicked().getUniqueId()) && event.getInventory() != this.kitEditors.get(event.getWhoClicked().getUniqueId())) {
            event.setCancelled(true);
        }
    }

    private boolean validClick(ClickType click) {
        return click == ClickType.LEFT || click == ClickType.RIGHT;
    }
}
