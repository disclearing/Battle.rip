package cc.stormworth.meetup.util.menu;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;

public class MenuListeners implements Listener {

    @EventHandler(ignoreCancelled = true)
    public void onInventoryClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        Menu menu = Menu.getByUuid(player.getUniqueId());

        if (menu != null) {
            event.setCancelled(true);

            if (event.getClickedInventory() != null && event.getClickedInventory().getTitle().equals(menu.getInventory().getTitle())) {
                if (event.getCurrentItem() == null || event.getCurrentItem().getType() == Material.AIR) {
                    return;
                }

                menu.onClickItem(player, event.getCurrentItem(), event.isRightClick());

                if (menu.isUpdateOnClick() && menu.getPlayer() != null && menu.getInventory() != null) {
                    menu.updateInventory(player);
                }
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onInventoryClose(InventoryCloseEvent event) {
        Player player = (Player) event.getPlayer();
        Menu menu = Menu.getByUuid(player.getUniqueId());

        if (menu != null) {
            if (event.getInventory().getTitle().equals(menu.getInventory().getTitle())) {
                menu.onClose();
            }

            menu.destroy();
        }
    }
}