package org.contrum.lobby.inventory;

import cc.stormworth.core.server.ServerType;
import cc.stormworth.core.util.inventory.ItemBuilder;
import org.contrum.lobby.LobbyPlugin;
import org.contrum.lobby.inventory.inventories.MainMenuInventory;
import org.contrum.lobby.inventory.inventories.MinigameInventory;
import org.contrum.lobby.server.ServerManager;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class InventoryManager {

    private final MainMenuInventory mainMenuInventory;
    private final Map<ServerType, ServerInventory> navigationInventoryMap;
    private final ItemStack lobbyItem = new ItemBuilder(Material.COMPASS)
            .setName("&eServer Selector")
            .toItemStack();

    public InventoryManager(LobbyPlugin lobbyPlugin, ServerManager serverManager) {
        navigationInventoryMap = new ConcurrentHashMap<>();

        mainMenuInventory = new MainMenuInventory(serverManager, this);

        for (ServerType serverInstanceType : ServerType.values())
            navigationInventoryMap.put(serverInstanceType, new MinigameInventory(serverManager, this, serverInstanceType));

        new BukkitRunnable() {
            @Override
            public void run() {
                mainMenuInventory.update();

                for (ServerInventory serverInventory : navigationInventoryMap.values()) {
                    serverInventory.update();
                }

            }
        }.runTaskTimer(lobbyPlugin, 20, 20);
    }

    public void giveLobbyLoadout(Player player) {
        player.getInventory().clear();
        player.getInventory().setArmorContents(null);
        player.getInventory().setItem(4, lobbyItem);
    }

    public MainMenuInventory getMainMenuInventory() {
        return mainMenuInventory;
    }

    public ServerInventory getInventory(ServerType serverInstanceType) {
        return navigationInventoryMap.get(serverInstanceType);
    }
}
