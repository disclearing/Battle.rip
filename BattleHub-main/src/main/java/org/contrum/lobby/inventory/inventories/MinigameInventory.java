package org.contrum.lobby.inventory.inventories;

import cc.stormworth.core.server.ServerInstance;
import cc.stormworth.core.server.ServerType;
import cc.stormworth.core.util.inventory.ItemBuilder;
import org.contrum.lobby.inventory.InventoryManager;
import org.contrum.lobby.inventory.ServerInventory;
import org.contrum.lobby.inventory.ServerStatusItem;
import org.contrum.lobby.server.ServerManager;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class MinigameInventory extends ServerInventory {

    private final ServerManager serverManager;
    private final InventoryManager inventoryManager;
    private final ServerType serverInstanceType;

    public MinigameInventory(ServerManager serverManager, InventoryManager inventoryManager, ServerType serverInstanceType) {
        super(ChatColor.DARK_GRAY + "\u00bb " + ChatColor.RED + serverInstanceType.getInventoryName(), 27);

        this.serverManager = serverManager;
        this.inventoryManager = inventoryManager;
        this.serverInstanceType = serverInstanceType;
    }

    @Override
    public void update() {

        Inventory inventory = getInventory();
        inventory.clear();

        int start = 10;

        for (ServerInstance serverInstance : serverManager.getInstances(serverInstanceType)) {
            if (start == 17) start = 19;
            setItem(start, new ServerStatusItem(serverInstance).getItem(), event -> {
                String serverName = ChatColor.stripColor(event.getCurrentItem().getItemMeta().getDisplayName());
                event.getWhoClicked().closeInventory();
                serverManager.connectToServer((Player) event.getWhoClicked(), serverName);
            });
            start++;
        }

        setItem(26, new ItemBuilder(Material.ARROW)
                .setName(ChatColor.RED + "Go Back")
                .toItemStack(), event -> inventoryManager.getMainMenuInventory().open((Player) event.getWhoClicked())
                );

    }

}
