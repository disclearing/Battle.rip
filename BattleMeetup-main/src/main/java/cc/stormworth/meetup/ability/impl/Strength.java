package cc.stormworth.meetup.ability.impl;

import cc.stormworth.core.kt.util.ItemBuilder;
import cc.stormworth.core.util.time.TimeUtil;
import cc.stormworth.meetup.ability.InteractAbility;
import cc.stormworth.meetup.managers.TeamManager;
import cc.stormworth.meetup.team.Team;
import com.google.common.collect.Lists;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.List;

public class Strength extends InteractAbility {

    public Strength() {
        super("Strength",
                "&cStrength II",
                Lists.newArrayList(
                        "",
                        "&7Give yourself and your allies &cStrength II",
                        "&7for 5 seconds in a radius of 20 blocks.",
                        ""
                ),
                new ItemBuilder(Material.BLAZE_POWDER).build(),
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
        return Lists.newArrayList(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 20 * 5, 1));
    }
}