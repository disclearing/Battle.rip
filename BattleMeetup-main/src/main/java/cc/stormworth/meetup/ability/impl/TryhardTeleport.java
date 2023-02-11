package cc.stormworth.meetup.ability.impl;

import cc.stormworth.core.util.chat.CC;
import cc.stormworth.core.util.cooldown.CooldownAPI;
import cc.stormworth.core.util.time.TimeUtil;
import cc.stormworth.meetup.Meetup;
import cc.stormworth.meetup.ability.InteractAbility;
import com.google.common.collect.Lists;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Egg;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.List;

public class TryhardTeleport extends InteractAbility {

    public TryhardTeleport() {
        super("TryhardTeleport", "&eTryhardTeleport",
                Lists.newArrayList(
                        "&7Do you usually play aggressively? Throw this to",
                        "&7your enemy to teleport to him and receive strength,",
                        "&7he will receive antitrapper",
                        ""
                ),
                new ItemStack(Material.EGG),
                TimeUtil.parseTimeLong("32s"));
    }

    @Override
    public void onInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        event.setCancelled(true);

        Egg egg = player.launchProjectile(Egg.class);
        egg.setShooter(player);
        egg.setMetadata("EggPort", new FixedMetadataValue(Meetup.getInstance(), true));

        sendActivation(event.getPlayer());
        event.setCancelled(true);
        consume(event.getPlayer());
        CooldownAPI.setCooldown(event.getPlayer(), "Global", TimeUtil.parseTimeLong("10s"));
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Egg)) {
            return;
        }

        Egg egg = (Egg) event.getDamager();

        if (!egg.hasMetadata("EggPort")) {
            return;
        }

        Player damager = (Player) egg.getShooter();
        Player damaged = (Player) event.getEntity();

        CooldownAPI.removeCooldown(damager, getName());

        if (damager.getLocation().distance(damaged.getLocation()) > 12) {
            damager.sendMessage(ChatColor.RED + "You are too far away to use this ability");
            CooldownAPI.setCooldown(damager, getName(), TimeUtil.parseTimeLong("12s"));
            return;
        }

        damager.teleport(damaged.getLocation());
        getPotionEffects().forEach(damager::addPotionEffect);

        CooldownAPI.setCooldown(damager, getName(), getCooldown());
        CooldownAPI.setCooldown(damaged, "Trapped", TimeUtil.parseTimeLong("5s"));

        damaged.sendMessage(
                CC.translate("&cYou have been trapped, you can not place blocks or put it for 5 seconds"));
    }

    @Override
    public List<PotionEffect> getPotionEffects() {
        return Lists.newArrayList(
                new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 20 * 5, 1)
        );
    }
}