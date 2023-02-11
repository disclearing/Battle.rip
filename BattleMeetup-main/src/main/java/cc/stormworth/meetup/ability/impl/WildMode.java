package cc.stormworth.meetup.ability.impl;

import cc.stormworth.core.util.chat.CC;
import cc.stormworth.core.util.cooldown.CooldownAPI;
import cc.stormworth.core.util.time.TimeUtil;
import cc.stormworth.meetup.ability.DamageableAbility;
import cc.stormworth.meetup.profile.Profile;
import cc.stormworth.meetup.profile.struct.Hit;
import com.google.common.collect.Lists;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

import java.util.List;

public class WildMode extends DamageableAbility {

    public WildMode() {
        super("WildMode",
                "&4Wild Mode",
                Lists.newArrayList(
                        "",
                        "&7Hit &c3 times &7in a row your enemy to increase your damage by 20%.",
                        ""
                ),
                new ItemStack(Material.DOUBLE_PLANT), TimeUtil.parseTimeLong("2m"));
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player) {
            Player player = (Player) event.getDamager();
            if (CooldownAPI.hasCooldown(player, "IncreaseDamage")) {
                event.setDamage(event.getDamage() * 1.5);
            }
        }
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
            damager.sendMessage(CC.translate("&6&l[&e✷&6&l] &eYou have to hit your enemy &e&l" + (3 - hit.getHits()) + " more times."));
        }

        if (hit.getHits() < 3) {
            return;
        }

        hit.setHits(0);

        super.onEntityDamageByEntity(event);

        CooldownAPI.setCooldown(damager, "IncreaseDamage", TimeUtil.parseTimeLong("10s"));
        CooldownAPI.setCooldown(damager, getName(), getCooldown());
    }

    @Override
    public List<PotionEffect> getPotionEffects() {
        return null;
    }
}