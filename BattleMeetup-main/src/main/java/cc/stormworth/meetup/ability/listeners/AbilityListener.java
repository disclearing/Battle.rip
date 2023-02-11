package cc.stormworth.meetup.ability.listeners;

import cc.stormworth.core.util.chat.CC;
import cc.stormworth.core.util.cooldown.CooldownAPI;
import cc.stormworth.core.util.time.TimeUtil;
import cc.stormworth.meetup.ability.Ability;
import cc.stormworth.meetup.ability.DamageableAbility;
import cc.stormworth.meetup.ability.InteractAbility;
import cc.stormworth.meetup.ability.impl.BowTeleporter;
import cc.stormworth.meetup.managers.TeamManager;
import cc.stormworth.meetup.profile.Profile;
import cc.stormworth.meetup.team.Team;
import org.bukkit.ChatColor;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.EnderPearl;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;

public class AbilityListener implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        if (player.getWalkSpeed() < 0.2F) {
            player.setWalkSpeed(0.2F);
        }

    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerInteract(PlayerInteractEvent event) {

        if (event.getAction() == Action.LEFT_CLICK_BLOCK) {
            if (!event.hasItem() || !event.getItem().hasItemMeta()) {
                return;
            }

            Player player = event.getPlayer();

            Ability ability = Ability.getByItem(event.getItem());

            if (ability == null) {
                return;
            }

            if (CooldownAPI.hasCooldown(player, ability.getName())) {
                player.sendMessage(ChatColor.RED + "You have to wait " + TimeUtil.millisToRoundedTime(
                        CooldownAPI.getCooldown(player, ability.getName())) +
                        " to use it again");
                event.setCancelled(true);
                player.updateInventory();
                return;
            }
        }

        if (event.getAction() != Action.RIGHT_CLICK_AIR
                && event.getAction() != Action.RIGHT_CLICK_BLOCK) {
            return;
        }

        if (!event.hasItem() || !event.getItem().hasItemMeta()) {
            return;
        }

        Player player = event.getPlayer();

        if (player.getWorld().getName().equalsIgnoreCase("void")) {
            return;
        }

        if (Ability.getByItem(event.getItem()) == null) {
            return;
        }

        if (Ability.getByItem(event.getItem()) instanceof BowTeleporter) {
            return;
        }

        event.setCancelled(true);
        player.updateInventory();

        if (!(Ability.getByItem(event.getItem()) instanceof InteractAbility)) {
            return;
        }

        InteractAbility ability = (InteractAbility) Ability.getByItem(event.getItem());

        if (ability == null) {
            return;
        }

        if (!ability.isEnabled()) {
            player.sendMessage(ChatColor.RED + "This ability is currently disabled.");
            return;
        }

        if (CooldownAPI.hasCooldown(player, "Global")) {
            player.sendMessage(CC.translate("&cYou have to wait " + TimeUtil.millisToRoundedTime(
                    CooldownAPI.getCooldown(player, "Global")) +
                    " to use &lAnother Ability&c again"));
            return;
        }

        if (CooldownAPI.hasCooldown(player, ability.getName())) {
            player.sendMessage(ChatColor.RED + "You have to wait " + TimeUtil.millisToRoundedTime(
                    CooldownAPI.getCooldown(player, ability.getName())) +
                    " to use it again");
            return;
        }

        ability.onInteract(event);
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {

        Player damager = null;
        if (!(event.getDamager() instanceof Player)) {
            if (event.getDamager() instanceof Projectile) {
                Projectile projectile = (Projectile) event.getDamager();
                if (projectile.getShooter() instanceof Player) {
                    damager = (Player) projectile.getShooter();
                }
            }
        } else {
            damager = (Player) event.getDamager();
        }

        if (damager == null) {
            return;
        }

        if (!(event.getEntity() instanceof Player)) {
            return;
        }

        if (event.getDamager() instanceof EnderPearl) {
            return;
        }

        Player victim = (Player) event.getEntity();

        ItemStack item = damager.getItemInHand();

        DamageableAbility ability = null;

        if (event.getDamager() instanceof Arrow) {
            Arrow arrow = (Arrow) event.getDamager();

            if (arrow.getShooter() instanceof Player) {
                if (arrow.hasMetadata("BowTeleporter")) {
                    ability = (DamageableAbility) Ability.getByName("BowTeleporter");
                }
            }
        }

        if (ability == null) {
            if (item == null || !item.hasItemMeta()) {
                return;
            }

            if (damager.getWorld().getName().equalsIgnoreCase("void")) {
                return;
            }

            if (!(Ability.getByItem(item) instanceof DamageableAbility)) {
                return;
            }

            ability = (DamageableAbility) Ability.getByItem(item);

            if (ability == null) {
                return;
            }
        }

        if (!ability.isEnabled()) {
            damager.sendMessage(ChatColor.RED + "This ability is currently disabled.");
            return;
        }

        if (CooldownAPI.hasCooldown(damager, "Global")) {
            damager.sendMessage(ChatColor.RED + "You have to wait " + TimeUtil.millisToRoundedTime(
                    CooldownAPI.getCooldown(damager, "Global")) +
                    " to use other ability again");
            event.setCancelled(true);
            return;
        }

        if (CooldownAPI.hasCooldown(damager, ability.getName())) {
            damager.sendMessage(ChatColor.RED + "You have to wait " + TimeUtil.millisToRoundedTime(
                    CooldownAPI.getCooldown(damager, ability.getName())) +
                    " to use it again");
            event.setCancelled(true);
            return;
        }

        Team damagerFaction = TeamManager.getInstance().getTeam(damager);
        if (damagerFaction != null) {
            Team targetFaction = TeamManager.getInstance().getTeam(victim);
            if (damagerFaction == targetFaction) {
                damager.sendMessage(CC.RED + "You cannot use special items against your team members.");
                return;
            }
        }

        ability.onEntityDamageByEntity(event);
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player)) {
            return;
        }

        Player player = (Player) event.getEntity();

        Profile profile = Profile.get(player);

        if (event instanceof EntityDamageByEntityEvent) {
            EntityDamageByEntityEvent e = (EntityDamageByEntityEvent) event;
            if (e.getDamager() instanceof Player) {
                Player damager = (Player) e.getDamager();

                Team damagerFaction = TeamManager.getInstance().getTeam(damager);
                if (damagerFaction != null) {
                    Team targetFaction = TeamManager.getInstance().getTeam(player);
                    if (damagerFaction == targetFaction) {
                        return;
                    }
                }
            }
        }

        if (profile.getCountdown() != null) {

            if (profile.getTeleport() == null) {
                return;
            }

            if (!profile.getTeleport().isCancelledOnDamage()) {
                return;
            }

            profile.getCountdown().cancel();
            profile.setCountdown(null);

            player.sendMessage(CC.translate("&cYou teleport has been cancelled due to damage."));
        }

    }
}