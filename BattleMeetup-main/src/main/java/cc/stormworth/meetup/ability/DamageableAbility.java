package cc.stormworth.meetup.ability;

import cc.stormworth.core.util.chat.CC;
import cc.stormworth.core.util.cooldown.CooldownAPI;
import cc.stormworth.core.util.time.TimeUtil;
import net.minecraft.util.org.apache.commons.lang3.time.DurationFormatUtils;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;

import java.util.List;

public abstract class DamageableAbility extends Ability {

    public DamageableAbility(String name, String displayName, List<String> description,
                             ItemStack item, long cooldown) {
        super(name, displayName, description, item, cooldown);
    }

    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        Player damager = null;
        if (!(event.getDamager() instanceof Player)) {
            if (event.getDamager() instanceof Projectile) {
                Projectile projectile = (Projectile) event.getDamager();
                if (projectile.getShooter() instanceof Player) {
                    damager = (Player) projectile.getShooter();
                }
            }
        } else {
            damager = (Player) event.getDamager();
        }

        if (damager == null) {
            return;
        }

        Player victim = (Player) event.getEntity();
        sendAttackMessage(damager, victim);
        consume(damager);
        CooldownAPI.setCooldown(damager, "Global", TimeUtil.parseTimeLong("10s"));
    }

    public void sendAttackMessage(final Player attacker, final Player victim) {
        final String left = DurationFormatUtils.formatDurationWords(getCooldown(), true, true);
        final String victimName =
                victim.hasPotionEffect(PotionEffectType.INVISIBILITY) ? "???" : victim.getName();
        attacker.sendMessage(CC.translate(""));
        attacker.sendMessage(CC.translate("&eYou have successfully hit &d" + victimName));
        attacker.sendMessage(CC.translate("&eNow on cooldown for &d" + left));
        attacker.sendMessage(CC.translate(""));

        victim.sendMessage(CC.translate(""));
        victim.sendMessage(
                CC.translate("&cYou have been hit with &f" + this.getDisplayName()));

        if (getVictimMessage() != null) {
            for (String msg : getVictimMessage()) {
                victim.sendMessage(CC.translate(msg));
            }
        }

        victim.sendMessage(CC.translate(""));
    }

    public String[] getVictimMessage() {
        return null;
    }
}