package cc.stormworth.meetup.listener;

import cc.stormworth.core.util.chat.CC;
import cc.stormworth.core.util.cooldown.CooldownAPI;
import cc.stormworth.core.util.time.TimeUtil;
import cc.stormworth.meetup.managers.GameManager;
import cc.stormworth.meetup.style.Style;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.EnderPearl;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class EnderPearlCooldown implements Listener {

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getItem() == null) {
            return;
        }
        if (event.getItem().getType() != Material.ENDER_PEARL) {
            return;
        }

        Player player = event.getPlayer();

        if (!CooldownAPI.hasCooldown(player, "enderpearl")) {
            return;
        }

        player.sendMessage(CC.translate("&c&lYou still have a &6&lEnderpearl &c&lcooldown for " + TimeUtil.millisToRoundedTime(
                CooldownAPI.getCooldown(player, "enderpearl"))));
        event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onProjectileLaunch(final ProjectileLaunchEvent event) {
        if (!(event.getEntity() instanceof EnderPearl)) {
            return;
        }
        if (!(event.getEntity().getShooter() instanceof Player)) {
            return;
        }
        final Player shooter = (Player) event.getEntity().getShooter();

        if (CooldownAPI.hasCooldown(shooter, "enderpearl")) {
            shooter.sendMessage(ChatColor.RED + "You have to wait " + TimeUtil.millisToRoundedTime(
                    CooldownAPI.getCooldown(shooter, "enderpearl")) +
                    " to use ender pearl again");
            event.setCancelled(true);
            return;
        }

        if (GameManager.getInstance().getStyle() == Style.HCF) {
            CooldownAPI.setCooldown(shooter, "enderpearl", TimeUtil.parseTimeLong("16s"));
        }
    }

}
