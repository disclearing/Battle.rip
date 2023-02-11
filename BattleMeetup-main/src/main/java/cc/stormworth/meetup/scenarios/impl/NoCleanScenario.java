package cc.stormworth.meetup.scenarios.impl;

import cc.stormworth.core.CorePlugin;
import cc.stormworth.core.util.chat.CC;
import cc.stormworth.meetup.managers.UserManager;
import cc.stormworth.meetup.scenarios.Scenario;
import cc.stormworth.meetup.user.UserData;
import cc.stormworth.meetup.util.Cooldown;
import cc.stormworth.meetup.util.PlayerUtil;
import cc.stormworth.meetup.util.TaskUtil;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.inventory.ItemStack;

public class NoCleanScenario extends Scenario implements Listener {

    public NoCleanScenario() {
        super("NoClean", new ItemStack(Material.DIAMOND_SWORD), false, "Upon killing a player, you are", "invincible for 30 seconds.", "You forfeit your invincibility by", "directly or indirectly attacking someone.");
    }

    public static void start() {
    }

    public static void cleanup() {
        UserManager.getInstance().getUsers().forEach(data -> {
            if (data.isInvincible()) {
                data.setInvincibilityTimer(new Cooldown(0));
                data.setInvincible(false);
            }
        });
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerDeathEvent(PlayerDeathEvent event) {

        if (Scenario.getByName("NoClean+").isActive())
            return;

        if (!(event.getEntity().getKiller() != null || event.getEntity().getKiller() instanceof Player)) return;

        Player player = event.getEntity().getKiller();
        UserData uhcPlayer = UserManager.getInstance().getUser(player.getUniqueId());

        if (!uhcPlayer.isAlive()) {
            return;
        }

        Cooldown cooldown = new Cooldown(30);
        uhcPlayer.setInvincibilityTimer(cooldown);
        uhcPlayer.setInvincible(true);

        player.sendMessage(CC.SECONDARY + "You are now invincible for " + CC.PRIMARY + "30 seconds" + CC.SECONDARY + ".");

        CorePlugin.getInstance().getNametagEngine().reloadPlayer(player);

        TaskUtil.runLater(() -> {

            if (player != null) {

                if (uhcPlayer.isInvincible()) {
                    player.sendMessage(CC.SECONDARY + "You are no longer invincible.");
                    uhcPlayer.setInvincibilityTimer(new Cooldown(0));
                    uhcPlayer.setInvincible(false);
                    CorePlugin.getInstance().getNametagEngine().reloadPlayer(player);
                }
            }
        }, 30 * 20L);
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onEntityDamage(EntityDamageEvent event) {
        Entity entity = event.getEntity();

        if (Scenario.getByName("NoClean+").isActive())
            return;

        if (!(entity instanceof Player)) return;
        if (event.getCause() == EntityDamageEvent.DamageCause.SUICIDE) return;

        final Player player = (Player) event.getEntity();
        UserData uhcPlayer = UserManager.getInstance().getUser(player.getUniqueId());

        if (!uhcPlayer.isAlive()) {
            return;
        }

        Player target = PlayerUtil.getAttacker(event, true);

        if (target != null) {
            UserData uhcPlayerTarget = UserManager.getInstance().getUser(target.getUniqueId());

            if (uhcPlayerTarget.isInvincible() && uhcPlayerTarget.isAlive()) {
                uhcPlayerTarget.setInvincibilityTimer(new Cooldown(0));
                uhcPlayerTarget.setInvincible(false);
                target.sendMessage(CC.SECONDARY + "You are no longer invincible.");
                CorePlugin.getInstance().getNametagEngine().reloadPlayer(player);
            }
        }

        if (uhcPlayer.isInvincible()) {

            if (target != null) {
                target.sendMessage(CC.RED + "This player has an invincibility timer.");
            }

            event.setCancelled(true);
            return;
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerBucketEmptyEvent(PlayerBucketEmptyEvent event) {
        Player player = event.getPlayer();
        UserData uhcPlayer = UserManager.getInstance().getUser(player.getUniqueId());

        if (Scenario.getByName("NoClean+").isActive())
            return;

        if (!uhcPlayer.isAlive()) {
            return;
        }

        if (event.getBucket() == Material.LAVA_BUCKET && uhcPlayer.isInvincible()) {
            uhcPlayer.setInvincibilityTimer(new Cooldown(0));
            uhcPlayer.setInvincible(false);
            player.sendMessage(CC.SECONDARY + "You are no longer invincible.");
            CorePlugin.getInstance().getNametagEngine().reloadPlayer(player);
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        UserData uhcPlayer = UserManager.getInstance().getUser(player.getUniqueId());

        if (Scenario.getByName("NoClean+").isActive())
            return;

        if (!uhcPlayer.isAlive()) {
            return;
        }

        if (event.getBlock().getType() == Material.FIRE || event.getBlock().getType() == Material.TNT) {

            if (uhcPlayer.isInvincible()) {
                uhcPlayer.setInvincibilityTimer(new Cooldown(0));
                uhcPlayer.setInvincible(false);
                player.sendMessage(CC.SECONDARY + "You are no longer invincible.");
                CorePlugin.getInstance().getNametagEngine().reloadPlayer(player);
            }
        }
    }
}
