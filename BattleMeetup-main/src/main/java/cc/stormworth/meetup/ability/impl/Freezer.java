package cc.stormworth.meetup.ability.impl;

import cc.stormworth.core.util.chat.CC;
import cc.stormworth.core.util.cooldown.CooldownAPI;
import cc.stormworth.core.util.general.TaskUtil;
import cc.stormworth.core.util.time.TimeUtil;
import cc.stormworth.meetup.Meetup;
import cc.stormworth.meetup.ability.DamageableAbility;
import cc.stormworth.meetup.profile.Profile;
import cc.stormworth.meetup.profile.struct.Hit;
import cc.stormworth.meetup.utils.PlayerUtils;
import com.google.common.collect.Lists;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerVelocityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

import java.util.List;

public class Freezer extends DamageableAbility {

    public Freezer() {
        super("Freezer",
                "&bFreezer",
                Lists.newArrayList(
                        "",
                        "&7Hit a player 3 times in a",
                        "&7row to &bfreeze &7him for 5 seconds.",
                        ""
                ),
                new ItemStack(Material.ICE),
                TimeUtil.parseTimeLong("1m30s"));
    }

    @Override
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        Player damager = (Player) event.getDamager();
        Player damaged = (Player) event.getEntity();

        Profile profileDamager = Profile.get(damager);

        Hit hit = profileDamager.getHit();

        if (hit == null || hit.getUuid() != damaged.getUniqueId() || !isItem(hit.getItemStack())) {
            hit = new Hit(damaged.getUniqueId(), getItem());
            profileDamager.setHit(hit);
        }

        hit.setHits(hit.getHits() + 1);

        if ((3 - hit.getHits()) > 0) {
            damager.sendMessage(
                    CC.translate("&6&l[&e✷&6&l] &eYou have to hit your enemy &e&l" + (3 - hit.getHits())
                            + " more times."));
        }

        if (hit.getHits() < 3) {
            return;
        }

        CooldownAPI.setCooldown(damager, getName(), getCooldown());

        PlayerUtils.denyMovement(damaged);
        CooldownAPI.setCooldown(damaged, "NoMove", TimeUtil.parseTimeLong("5s"));
        TaskUtil.runLater(Meetup.getInstance(), () -> PlayerUtils.allowMovement(damaged), 20 * 5);

        hit.setHits(0);

        damaged.sendMessage("");
        damaged.sendMessage(CC.translate("&cYou have been freeze, you can not move for 5 seconds"));
        damaged.sendMessage("");
        super.onEntityDamageByEntity(event);
    }

    @EventHandler
    public void onInteractPearl(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        if (player.getItemInHand() == null) {
            return;
        }

        if (player.getItemInHand().getType() != Material.ENDER_PEARL) {
            return;
        }

        if (CooldownAPI.hasCooldown(player, "NoMove")) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onVelocity(PlayerVelocityEvent event) {
        Player player = event.getPlayer();

        if (CooldownAPI.hasCooldown(player, "NoMove")) {
            event.setCancelled(true);
        }
    }

    @Override
    public List<PotionEffect> getPotionEffects() {
        return null;
    }
}