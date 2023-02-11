package cc.stormworth.meetup.listener;

import cc.stormworth.meetup.managers.GameManager;
import cc.stormworth.meetup.managers.InventoryManager;
import cc.stormworth.meetup.scenarios.Scenario;
import cc.stormworth.meetup.util.Colors;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class InventoryListener implements Listener {

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();

        if (e.getClickedInventory() == null) {
            return;
        }

        if (e.getClickedInventory().getName().equals("Configuration") || e.getClickedInventory().getName().equals("Scenarios")) {

            if (e.getCurrentItem() == null) {
                return;
            }

            if (e.getCurrentItem().getItemMeta() == null) {
                return;
            }

            if (e.getCurrentItem().getItemMeta().getDisplayName().equals(Colors.PRIMARY + "Game Style")) {
                p.closeInventory();
                p.performCommand("style");
            } else if (e.getCurrentItem().getItemMeta().getDisplayName().equals(Colors.PRIMARY + "Special Scenarios")) {

                if (GameManager.getInstance().isLobby()) {
                    p.closeInventory();
                    InventoryManager.getInstance().openSpecialScenariosInventory(p);
                } else {
                    p.sendMessage(Colors.RED + "The game has already started.");
                }
            }
        }
    }

    @EventHandler
    public void onSpecialScenariosInventoryClick(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();

        if (e.getClickedInventory() == null) {
            return;
        }

        if (e.getClickedInventory().getName().equals("Special Scenarios")) {

            if (e.getCurrentItem() == null) {
                return;
            }

            if (e.getCurrentItem().getItemMeta() == null) {
                return;
            }

            Scenario scenario = Scenario.getByName(ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName()));

            if (scenario == null) {
                return;
            }

            scenario.toggle(p, true);
            InventoryManager.getInstance().reloadSpecialScenariosInventory();
        }
    }
}
