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

public class Speed extends InteractAbility {

    public Speed() {
        super(
                "Speed",
                "&bSpeed III",
                Lists.newArrayList(
                        "",
                        "&7Give yourself and your allies &bSpeed III",
                        "&7for 5 seconds in a radius of 20 blocks.",
                        ""
                ),
                new ItemStack(Material.SUGAR),
                TimeUtil.parseTimeLong("30s"));
    }

    @Override
    public void onInteract(PlayerInteractEvent event) {

        Player player = event.getPlayer();

        getPotionEffects().forEach(effect -> player.addPotionEffect(effect, true));

        Team team = TeamManager.getInstance().getTeam(player);

        if (team != null) {
            player.getNearbyEntities(20, 20, 20).stream()
                    .filter(entity -> entity instanceof Player)
                    .map(entity -> (Player) entity)
                    .forEach(other -> {
                        if (team.isMember(other.getUniqueId())) {
                            getPotionEffects().forEach(effect -> other.addPotionEffect(effect, true));
                        }
                    });
        }

        super.onInteract(event);
    }

    @Override
    public List<PotionEffect> getPotionEffects() {
        return Lists.newArrayList(new PotionEffect(PotionEffectType.SPEED, 20 * 5, 2));
    }
}