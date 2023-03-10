package cc.stormworth.meetup.ability.impl;

import cc.stormworth.core.util.chat.CC;
import cc.stormworth.core.util.cooldown.CooldownAPI;
import cc.stormworth.core.util.time.TimeUtil;
import cc.stormworth.meetup.ability.InteractAbility;
import cc.stormworth.meetup.profile.Profile;
import cc.stormworth.meetup.profile.struct.PearlLocation;
import cc.stormworth.meetup.profile.struct.Teleport;
import com.google.common.collect.Lists;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EnderPearl;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

import java.util.List;

public class LastPearl extends InteractAbility {

    public LastPearl() {
        super("LastPearl",
                "&6LastPearl",
                Lists.newArrayList(
                        "",
                        "&7Trying to escape? Use this ability and",
                        "&7as soon as you throw a pearl its location",
                        "&7will be saved and you will return to the place from where it was thrown.",
                        ""
                ),
                new ItemStack(Material.WATCH),
                TimeUtil.parseTimeLong("1m"));
    }

    @Override
    public void onInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        Profile profile = Profile.get(player);

        if (profile.getLastPearlLocation() == null || profile.getLastPearlLocation().isExpired()) {
            profile.setLastPearlLocation(null);
            player.sendMessage(CC.translate("&cYou must have a valid pearl to use this ability."));
            return;
        }

        Location location = profile.getLastPearlLocation().getLocation();

        if (location == null) {
            return;
        }

        player.sendMessage(CC.translate("&eYou will be teleported in 5 seconds"));
        CooldownAPI.setCooldown(player, getName(), getCooldown());

        Teleport teleport = new Teleport(player, player.getLocation(), location, 5);

        teleport.setCancelOnMove(false);
        teleport.setCancelledOnDamage(false);

        teleport.setOnTeleport((other) -> {
            profile.setLastPearlLocation(null);
        });

        teleport.setCancelledOnDamage(false);
        profile.setTeleport(teleport);

        profile.setCountdown(teleport.start());

        super.onInteract(event);
    }

    @EventHandler
    public void onPearlHitEvent(ProjectileLaunchEvent event) {
        if (!(event.getEntity() instanceof EnderPearl)) {
            return;
        }
        if (!(event.getEntity().getShooter() instanceof Player)) {
            return;
        }

        Player player = (Player) event.getEntity().getShooter();

        Profile profile = Profile.get(player);

        profile.setLastPearlLocation(
                new PearlLocation(System.currentTimeMillis() + TimeUtil.parseTimeLong("16s"),
                        player.getLocation()));
    }

    @Override
    public List<PotionEffect> getPotionEffects() {
        return null;
    }
}