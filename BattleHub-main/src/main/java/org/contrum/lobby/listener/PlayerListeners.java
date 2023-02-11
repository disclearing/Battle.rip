package org.contrum.lobby.listener;

import cc.stormworth.core.util.Utilities;
import org.contrum.lobby.LobbyPlugin;
import org.contrum.lobby.inventory.InventoryManager;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.entity.Boat;
import org.bukkit.entity.Player;
import org.bukkit.entity.Vehicle;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.*;
import org.bukkit.event.vehicle.VehicleCreateEvent;
import org.bukkit.event.vehicle.VehicleDestroyEvent;
import org.bukkit.event.vehicle.VehicleExitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.StringJoiner;

public class PlayerListeners implements Listener {

    private final LobbyPlugin lobbyPlugin;
    private final InventoryManager inventoryManager;

    private final Location spawnLocation;

    public PlayerListeners(LobbyPlugin lobbyPlugin, InventoryManager inventoryManager) {
        this.lobbyPlugin = lobbyPlugin;

        this.inventoryManager = inventoryManager;

        this.spawnLocation = Bukkit.getWorld("spawn").getSpawnLocation().clone();
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        event.setJoinMessage(null);

        Player player = event.getPlayer();

        player.teleport(spawnLocation);
        inventoryManager.giveLobbyLoadout(player);
        player.setGameMode(GameMode.ADVENTURE);
        player.setHealth(20);
        player.setFoodLevel(20);

        Utilities.sendHeaderFooter(event.getPlayer(),
                new StringJoiner("\n")
                        .add("")
                        .add(ChatColor.YELLOW.toString() + ChatColor.BOLD + "BATTLE NETWORK " + ChatColor.GRAY + ChatColor.BOLD + "\u007c " + ChatColor.WHITE + ChatColor.BOLD + "BATTLE.RIP")
                        .add("")
                        .toString(),
                new StringJoiner("\n")
                        .add("")
                        .add(ChatColor.YELLOW.toString() + ChatColor.BOLD + "  Website: " + ChatColor.WHITE + "https://battle.rip")
                        .add(ChatColor.YELLOW.toString() + ChatColor.BOLD + "  Store: " + ChatColor.WHITE + "https://battle.rip/store")
                        .add(ChatColor.YELLOW.toString() + ChatColor.BOLD + "  Discord: " + ChatColor.WHITE + "https://battle.rip/discord")
                        .add("")
                        .toString());

        if (player.hasPermission("core.fly")) {
            player.setAllowFlight(true);
            player.setFlying(true);
        }

    }

    @EventHandler
    public void onQuit(PlayerQuitEvent playerQuitEvent) {
        playerQuitEvent.setQuitMessage(null);

        Player player = playerQuitEvent.getPlayer();

        player.setFlying(false);
        player.setAllowFlight(false);

        if(player.getVehicle() == null) return;
        Vehicle vehicle = (Vehicle) player.getVehicle();
        player.leaveVehicle();
        vehicle.remove();
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Action action = event.getAction();

        if (action.equals(Action.PHYSICAL) && event.getClickedBlock().getType().equals(Material.SOIL)) {
            event.setCancelled(true);
            return;
        }

        Player player = event.getPlayer();

        if (action.equals(Action.PHYSICAL) && event.getClickedBlock().getType().equals(Material.IRON_PLATE)) {
            event.setCancelled(true);
            return;
        }

        if (action == Action.RIGHT_CLICK_BLOCK && event.getClickedBlock() != null && (event.getClickedBlock().getType() == Material.WALL_SIGN || event.getClickedBlock().getType() == Material.SIGN_POST)) {
            Sign sign = (Sign) event.getClickedBlock().getState();

            if (sign.getLine(2).equalsIgnoreCase(ChatColor.BOLD + "Free Boat")) {
                player.getInventory().setItem(0, new ItemStack(Material.BOAT));
            }
        }

        if (action != Action.RIGHT_CLICK_AIR)
            return;

        if (event.getItem() == null)
            return;

        if (!event.getItem().hasItemMeta())
            return;

        if (event.getItem().getType() == Material.COMPASS && event.getItem().getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.YELLOW + "Server Selector"))
            player.openInventory(inventoryManager.getMainMenuInventory().getInventory());

    }

    @EventHandler
    public void onPlayerSign(SignChangeEvent event) {
        if (!event.getLine(0).equalsIgnoreCase("[boat]")) return;
        event.setLine(0, "");
        event.setLine(1, ChatColor.BOLD + "Right Click");
        event.setLine(2, ChatColor.BOLD + "Free Boat");
        event.setLine(3, "");
    }

    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onPlayerPickupItem(PlayerPickupItemEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onLeaveVehicle(VehicleExitEvent event) {
        if (event.getVehicle() instanceof Boat)
            event.getVehicle().remove();
    }

    @EventHandler
    public void onVehicleCreate(VehicleCreateEvent event) {

        new BukkitRunnable() {
            @Override
            public void run() {
                if (event.getVehicle() != null)
                    if (event.getVehicle().getPassenger() == null)
                        event.getVehicle().remove();
            }
        }.runTaskLater(lobbyPlugin, 20 * 10);

    }

    @EventHandler
    public void onVehicleBreak(VehicleDestroyEvent event) {
        event.setCancelled(true);
        event.getVehicle().remove();
    }


}