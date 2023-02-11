package cc.stormworth.meetup.listener;

import cc.stormworth.core.server.menus.MeetupSelectorMenu;
import cc.stormworth.meetup.managers.InventoryManager;
import cc.stormworth.meetup.managers.PlayerManager;
import cc.stormworth.meetup.managers.UserManager;
import cc.stormworth.meetup.states.PlayerState;
import cc.stormworth.meetup.user.UserData;
import cc.stormworth.meetup.util.Colors;
import cc.stormworth.meetup.util.PlayerUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class SpectatorListener implements Listener {

    @EventHandler
    public void onItemClick(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        UserData user = UserManager.getInstance().getUser(p.getUniqueId());

        if(user.getPlayerState() != PlayerState.SPECTATOR) {
            return;
        }

        if(e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {

            if(e.getItem() == null) {
                return;
            }

            switch(e.getItem().getType()) {

                default:
                    break;
                case ITEM_FRAME:
                    InventoryManager.getInstance().openSpectateMenuInventory(p);
                    break;
                case INK_SACK:
                    p.teleport(PlayerManager.CENTER_LOCATION);
                    p.sendMessage(Colors.SECONDARY + "You have been teleported to the center.");
                    break;
                case RECORD_12:
                    PlayerUtil.randomTeleport(p);
                    break;
                case PAPER:
                    new MeetupSelectorMenu().openMenu(e.getPlayer());
                    break;
            }
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();
        UserData user = UserManager.getInstance().getUser(p.getUniqueId());

        if(user.getPlayerState() != PlayerState.SPECTATOR) {
            return;
        }

        if(e.getClickedInventory() == null) {
            return;
        }

        if(e.getClickedInventory().getName().equals("Spectate Menu")) {

            if(e.getCurrentItem() == null) {
                return;
            }

            if(e.getCurrentItem().getItemMeta() == null) {
                return;
            }

            Player target = Bukkit.getPlayer(ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName()));

            if(target == null) {
                return;
            }

            p.teleport(target);
            p.sendMessage(Colors.SECONDARY + "You have been teleported to " + Colors.PRIMARY + target.getName() + Colors.SECONDARY + ".");
        }
    }

    @EventHandler
    public void onHit(EntityDamageByEntityEvent e) {

        if(e.getDamager() instanceof Player) {
            Player p = (Player) e.getDamager();
            UserData user = UserManager.getInstance().getUser(p.getUniqueId());

            if(!user.isAlive()) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onChestClick(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        UserData user = UserManager.getInstance().getUser(p.getUniqueId());
        Block block = e.getClickedBlock();

        if (p.getGameMode() == GameMode.CREATIVE && p.isOp() && user.getPlayerState() == PlayerState.LOBBY){
            return;
        }

        if(user.isAlive()) {
            return;
        }

        if(block == null) {
            return;
        }

        if(block.getType() == Material.CHEST && e.getAction() == Action.RIGHT_CLICK_BLOCK) {
            e.setCancelled(true);
        }
    }
}
