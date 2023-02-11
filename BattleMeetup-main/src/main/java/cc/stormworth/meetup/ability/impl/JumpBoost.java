package cc.stormworth.meetup.ability.impl;

import cc.stormworth.core.util.time.TimeUtil;
import cc.stormworth.meetup.ability.InteractAbility;
import cc.stormworth.meetup.managers.TeamManager;
import cc.stormworth.meetup.team.Team;
import com.google.common.collect.Lists;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.List;

public class JumpBoost extends InteractAbility {

    public JumpBoost() {
        super("JumpBoost",
                "&bJump Boost VII",
                Lists.newArrayList(
                        "",
                        "&7Give yourself and your allies &bJump Boost VII",
                        "&7for 5 seconds in a radius of 20 blocks",
                        ""
                ),
                new ItemStack(Material.FEATHER),
                TimeUtil.parseTimeLong("30s"));
    }

    @Override
    public void onInteract(PlayerInteractEvent event) {

        Player player = event.getPlayer();

        getPotionEffects().forEach(player::addPotionEffect);

        Team team = TeamManager.getInstance().getTeam(player);

        if (team != null) {
            player.getNearbyEntities(20, 20, 20).stream()
                    .filter(entity -> entity instanceof Player)
                    .map(entity -> (Player) entity)
                    .forEach(other -> {
                        if (team.isMember(other.getUniqueId())) {
                            getPotionEffects().forEach(potionEffect -> other.addPotionEffect(potionEffect, true));
                        }
                    });
        }

        super.onInteract(event);
    }

    @Override
    public List<PotionEffect> getPotionEffects() {
        return Lists.newArrayList(new PotionEffect(PotionEffectType.JUMP, 20 * 5, 6));
    }
}