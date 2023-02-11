package cc.stormworth.meetup.ability.impl;

import cc.stormworth.core.util.time.TimeUtil;
import cc.stormworth.meetup.ability.InteractAbility;
import com.google.common.collect.Lists;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.List;

public class MedKit extends InteractAbility {

    public MedKit() {
        super("MedKit",
                "&cMedKit",
                Lists.newArrayList(
                        "",
                        "&7About to die? Use it to receive effects",
                        "&7such as: Resistance, Regeneration and Absorption.",
                        ""
                ),
                new ItemStack(Material.PAPER),
                TimeUtil.parseTimeLong("1m30s"));
    }

    @Override
    public void onInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        getPotionEffects().forEach(player::addPotionEffect);

        super.onInteract(event);
    }

    @Override
    public List<PotionEffect> getPotionEffects() {
        return Lists.newArrayList(
                new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 20 * 5, 3),
                new PotionEffect(PotionEffectType.REGENERATION, 20 * 5, 3),
                new PotionEffect(PotionEffectType.ABSORPTION, 20 * 5, 1)
        );
    }
}