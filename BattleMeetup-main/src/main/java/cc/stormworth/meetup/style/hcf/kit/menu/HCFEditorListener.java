package cc.stormworth.meetup.style.hcf.kit.menu;

import cc.stormworth.core.util.chat.CC;
import cc.stormworth.meetup.Meetup;
import cc.stormworth.meetup.database.MeetupMongo;
import cc.stormworth.meetup.managers.UserManager;
import cc.stormworth.meetup.style.hcf.kit.HCFKitSortation;
import cc.stormworth.meetup.user.UserData;
import cc.stormworth.meetup.util.NumberUtil;
import cc.stormworth.meetup.util.TaskUtil;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.*;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.stream.IntStream;

public class HCFEditorListener implements Listener {

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        Player player = (Player) event.getPlayer();

        if (HCFEditorMenu.getEditors().containsKey(player.getUniqueId())) {
            player.sendMessage(CC.SECONDARY + "Your kit layout for game style " + CC.PRIMARY + "HCF " + CC.SECONDARY + "has been saved.");

            Inventory editor = HCFEditorMenu.getEditors().get(player.getUniqueId());
            UserData user = UserManager.getInstance().getUser(player.getUniqueId());
            HCFKitSortation sortation = user.getHcfSortation();

            IntStream.range(0, 36).filter(slot -> editor.getItem(slot) != null).forEach(slot -> {
                switch (editor.getItem(slot).getType()) {
                    default:
                        break;
                    case NETHER_STAR:
                        sortation.setAbilitySlot(NumberUtil.convertEditorSlot(slot));
                        break;
                    case DIAMOND_SWORD:
                        sortation.setSwordSlot(NumberUtil.convertEditorSlot(slot));
                        break;
                    case ENDER_PEARL:
                        sortation.setPearlSlot(NumberUtil.convertEditorSlot(slot));
                        break;
                    case FISHING_ROD:
                        sortation.setRodSlot(NumberUtil.convertEditorSlot(slot));
                        break;
                    case BOW:
                        sortation.setBowSlot(NumberUtil.convertEditorSlot(slot));
                        break;
                    case COOKED_BEEF:
                        sortation.setFoodSlot(NumberUtil.convertEditorSlot(slot));
                        break;
                    case WEB:
                        sortation.setWebSlot(NumberUtil.convertEditorSlot(slot));
                        break;
                    case ARROW:
                        sortation.setArrowSlot(NumberUtil.convertEditorSlot(slot));
                        break;
                    case POTION:
                        short data = editor.getItem(slot).getDurability();
                        if (data == 16388) sortation.setFirstPotionSlot(NumberUtil.convertEditorSlot(slot));
                        else if (data == 16426) sortation.setSecondPotionSlot(NumberUtil.convertEditorSlot(slot));
                        else if (data == 16424) sortation.setThirdPotionSlot(NumberUtil.convertEditorSlot(slot));
                        else if (data == 16428) sortation.setFourthPotionSlot(NumberUtil.convertEditorSlot(slot));
                        break;
                }
            });

            user.setHcfSortation(sortation);

            TaskUtil.runAsync(() -> MeetupMongo.getInstance().storeStatistics(
                    player.getName(),
                    player.getUniqueId(),
                    user.getStatistics(),
                    user.getHcfSortation().serialize()
            ));

            HCFEditorMenu.getEditors().remove(player.getUniqueId());
        }
    }

    @EventHandler
    public void onInventoryDrop(final PlayerDropItemEvent event) {
        if (HCFEditorMenu.getEditors().containsKey(event.getPlayer().getUniqueId())) {
            event.setCancelled(true);
            event.getPlayer().updateInventory();
        }
        if (event.getPlayer().getWorld().getName().equals("world")) {
            new BukkitRunnable() {
                public void run() {
                    final ItemStack itemStack = event.getItemDrop().getItemStack();
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

        if (HCFEditorMenu.getEditors().containsKey(player.getUniqueId())) {
            if (event.getClickedInventory() == null) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onInventoryMove(final InventoryInteractEvent event) {
        if (HCFEditorMenu.getEditors().containsKey(event.getWhoClicked().getUniqueId()) && event.getInventory() != HCFEditorMenu.getEditors().get(event.getWhoClicked().getUniqueId())) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onInventoryMove(final InventoryMoveItemEvent event) {
        if (HCFEditorMenu.getEditors().containsValue(event.getSource())) {
            Inventory inventory = null;
            for (final Inventory inv : HCFEditorMenu.getEditors().values()) {
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
    public void onInventoryClick(final InventoryClickEvent event) {
        final Player player = (Player) event.getWhoClicked();
        if (HCFEditorMenu.getEditors().containsKey(player.getUniqueId())) {
            if (event.getRawSlot() > 35 || !validClick(event.getClick())) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onInventoryMove(final InventoryDragEvent event) {
        if (HCFEditorMenu.getEditors().containsKey(event.getWhoClicked().getUniqueId()) && event.getInventory() != HCFEditorMenu.getEditors().get(event.getWhoClicked().getUniqueId())) {
            event.setCancelled(true);
        }
    }

    private boolean validClick(ClickType click) {
        return click == ClickType.LEFT || click == ClickType.RIGHT;
    }
}
