package cc.stormworth.meetup.listener;

import cc.stormworth.meetup.managers.GameManager;
import cc.stormworth.meetup.managers.PlayerManager;
import cc.stormworth.meetup.managers.UserManager;
import cc.stormworth.meetup.states.PlayerState;
import cc.stormworth.meetup.user.UserData;

import org.bukkit.GameMode;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.*;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.event.hanging.HangingPlaceEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.*;

public class LobbyListener implements Listener {

    @EventHandler
    public void onEntityDamage(EntityDamageEvent e) {

        if (e.getCause() == EntityDamageEvent.DamageCause.VOID && e.getEntity() instanceof Player) {
            Player p = (Player) e.getEntity();
            p.teleport(PlayerManager.SPAWN_LOCATION);
        }

        if (!GameManager.getInstance().isIngame()) {
            e.setCancelled(true);
            return;
        }

        if (e.getEntity() instanceof Player) {
            Player p = (Player) e.getEntity();
            UserData user = UserManager.getInstance().getUser(p.getUniqueId());

            if (user.getPlayerState() != PlayerState.INGAME) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent e) {
        Player p = e.getPlayer();

        UserData user = UserManager.getInstance().getUser(p.getUniqueId());

        if (p.getGameMode() == GameMode.CREATIVE && p.isOp() && user.getPlayerState() == PlayerState.LOBBY){
            return;
        }

        if (!GameManager.getInstance().isIngame()) {
            e.setCancelled(true);
            return;
        }


        if (user.getPlayerState() != PlayerState.INGAME) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onItemPickup(PlayerPickupItemEvent e) {
        Player p = e.getPlayer();

        UserData user = UserManager.getInstance().getUser(p.getUniqueId());

        if (p.getGameMode() == GameMode.CREATIVE && p.isOp() && user.getPlayerState() == PlayerState.LOBBY){
            return;
        }

        if (!GameManager.getInstance().isIngame()) {
            e.setCancelled(true);
            return;
        }

        if (user.getPlayerState() != PlayerState.INGAME) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onBucketEmpty(PlayerBucketEmptyEvent e) {
        Player p = e.getPlayer();

        UserData user = UserManager.getInstance().getUser(p.getUniqueId());

        if (p.getGameMode() == GameMode.CREATIVE && p.isOp() && user.getPlayerState() == PlayerState.LOBBY){
            return;
        }

        if (!GameManager.getInstance().isIngame()) {
            e.setCancelled(true);
            return;
        }

        if (user.getPlayerState() != PlayerState.INGAME) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onRodThrow(PlayerFishEvent e) {
        Player p = e.getPlayer();

        UserData user = UserManager.getInstance().getUser(p.getUniqueId());

        if (p.getGameMode() == GameMode.CREATIVE && p.isOp() && user.getPlayerState() == PlayerState.LOBBY){
            return;
        }

        if (!GameManager.getInstance().isIngame()) {
            e.setCancelled(true);
            return;
        }

        if (user.getPlayerState() != PlayerState.INGAME) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onBowShoot(EntityShootBowEvent e) {

        if (e.getEntity() instanceof Player) {
            Player p = (Player) e.getEntity();

            if (!GameManager.getInstance().isIngame()) {
                e.setCancelled(true);
                return;
            }

            UserData user = UserManager.getInstance().getUser(p.getUniqueId());

            if (user.getPlayerState() != PlayerState.INGAME) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onProjectileThrow(ProjectileLaunchEvent e) {

        if(e.getEntity() instanceof EnderPearl || e.getEntity() instanceof ThrownPotion || e.getEntity() instanceof Snowball && e.getEntity().getShooter() instanceof Player) {
            Player player = (Player) e.getEntity().getShooter();

            if(!GameManager.getInstance().isIngame()) {
                e.setCancelled(true);
                player.updateInventory();
                return;
            }

            UserData user = UserManager.getInstance().getUser(player.getUniqueId());

            if(user.getPlayerState() != PlayerState.INGAME) {
                e.setCancelled(true);
                player.updateInventory();
            }
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();

        UserData user = UserManager.getInstance().getUser(p.getUniqueId());

        if (p.getGameMode() == GameMode.CREATIVE && p.isOp() && user.getPlayerState() == PlayerState.LOBBY){
            return;
        }

        if (e.getClickedInventory() == null) {
            return;
        }

        if (e.getClickedInventory().getName().equals("Configuration")
                || e.getClickedInventory().getName().equals("Scenarios")
                || e.getClickedInventory().getName().equals("Statistics")
                || e.getClickedInventory().getName().equals("Leaderboard")) {

            e.setCancelled(true);
            return;
        }

        if (GameManager.getInstance().isLobby()) {

            if (!e.getClickedInventory().getName().contains("Kit Editor")) {
                e.setCancelled(true);
            }

            return;
        }

        if (user.getPlayerState() == PlayerState.SCATTERED) {
            return;
        }

        if (user.getPlayerState() != PlayerState.INGAME) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onFoodLevelChange(FoodLevelChangeEvent e) {

        if (!GameManager.getInstance().isIngame()) {
            e.setCancelled(true);
            return;
        }

        if (e.getEntity() instanceof Player) {
            Player p = (Player) e.getEntity();
            UserData user = UserManager.getInstance().getUser(p.getUniqueId());

            if (user.getPlayerState() != PlayerState.INGAME) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) {
        Player p = e.getPlayer();

        UserData user = UserManager.getInstance().getUser(p.getUniqueId());

        if (p.getGameMode() == GameMode.CREATIVE && p.isOp() && user.getPlayerState() == PlayerState.LOBBY){
            return;
        }

        if (!GameManager.getInstance().isIngame()) {
            e.setCancelled(true);
            return;
        }

        if (user.getPlayerState() != PlayerState.INGAME) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent e) {
        Player p = e.getPlayer();

        UserData user = UserManager.getInstance().getUser(p.getUniqueId());

        if (p.getGameMode() == GameMode.CREATIVE && p.isOp() && user.getPlayerState() == PlayerState.LOBBY){
            return;
        }

        if (!GameManager.getInstance().isIngame()) {
            e.setCancelled(true);
            return;
        }

        if (user.getPlayerState() != PlayerState.INGAME) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onCreatureSpawnEvent(CreatureSpawnEvent e) {
        if (e.getEntityType() == EntityType.HORSE) return;
        if (e.getSpawnReason() == SpawnReason.CUSTOM) {
            e.setCancelled(false);
            return;
        }

        e.setCancelled(true);
    }

    @EventHandler
    public void onHangingPlace(HangingPlaceEvent e) {
        Player p = e.getPlayer();

        if (p.getGameMode() == GameMode.CREATIVE && p.isOp()){
            return;
        }

        if (e.getEntity() instanceof ItemFrame) {

            if (GameManager.getInstance().isLobby()) {
                e.setCancelled(true);
                return;
            }

            UserData user = UserManager.getInstance().getUser(p.getUniqueId());

            if (user.getPlayerState() != PlayerState.INGAME) {
                e.setCancelled(true);
            }
        }
    }
}
