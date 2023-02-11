package cc.stormworth.meetup.listener;

import cc.stormworth.meetup.managers.GameManager;
import cc.stormworth.meetup.managers.UserManager;
import cc.stormworth.meetup.states.PlayerState;
import cc.stormworth.meetup.team.TeamInviteMenu;
import cc.stormworth.meetup.user.UserData;
import cc.stormworth.meetup.user.statistics.StatisticsMenu;
import cc.stormworth.meetup.util.Colors;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;

public class ItemListener implements Listener {

    @EventHandler
    public void onItemClick(PlayerInteractEvent event) {
        Player p = event.getPlayer();

        if (p.getGameMode() == GameMode.CREATIVE && p.isOp()) {
            return;
        }

        if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {

            ItemStack item = event.getItem();

            if (item == null || !item.hasItemMeta() || !item.getItemMeta().hasDisplayName()) {
                return;
            }

            switch (item.getItemMeta().getDisplayName()) {
                default:
                    break;
                case "§6Configuration":
                    p.performCommand("config");
                    break;
                case "§6Game Style":
                    p.performCommand("style");
                    break;
                case "§6Kit Editor":
                    if (event.getItem().getItemMeta().getDisplayName().equals(Colors.PRIMARY + "Kit Editor"))
                        p.performCommand("editkit");
                    break;
                case "§6Leaderboard":
                    p.performCommand("leaderboard");
                    break;
                case "§6Your Stats":
                    new StatisticsMenu(p).openInventory();
                    break;
                case "§6Team":
                    new TeamInviteMenu(p).openInventory();
                    break;
            }
        }
    }

    @EventHandler
    public void onPlayerClick(EntityDamageByEntityEvent e) {

        if (!GameManager.getInstance().isLobby())
            return;
        if (!(e.getDamager() instanceof Player))
            return;
        if (!(e.getEntity() instanceof Player))
            return;

        Player p = (Player) e.getDamager();
        Player target = (Player) e.getEntity();

        UserData targetUser = UserManager.getInstance().getUser(target.getUniqueId());

        if (targetUser.hasFullTeam())
            return;

        if (p.getItemInHand().getType() == Material.SKULL_ITEM)
            p.performCommand("team invite " + target.getName());
    }
}
