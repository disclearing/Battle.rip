package cc.stormworth.meetup.ability.impl;

import cc.stormworth.core.util.time.TimeUtil;
import cc.stormworth.meetup.ability.InteractAbility;
import com.google.common.collect.Lists;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

import java.util.List;

public class Rocket extends InteractAbility {

    public Rocket() {
        super("Rocket", "&cRocket", Lists.newArrayList(""), new ItemStack(Material.FIREWORK),
                TimeUtil.parseTimeLong("30s"));
    }

    @Override
    public void onInteract(PlayerInteractEvent event) {

        Player player = event.getPlayer();

        player.setVelocity(player.getEyeLocation().toVector().normalize().setY(1.6));

        super.onInteract(event);
    }


    @Override
    public List<PotionEffect> getPotionEffects() {
        return null;
    }
}