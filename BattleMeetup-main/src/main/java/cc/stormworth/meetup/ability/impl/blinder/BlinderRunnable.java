package cc.stormworth.meetup.ability.impl.blinder;

import cc.stormworth.core.util.chat.CC;
import cc.stormworth.meetup.managers.GameManager;
import com.google.common.collect.Lists;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.List;

public class BlinderRunnable implements Runnable {

    @Getter
    private static final List<Projectile> entities = Lists.newArrayList();

    @Override
    public void run() {

        if (entities.isEmpty()) {
            return;
        }

        entities.forEach(entity -> {

            if (entity.isDead()) {
                return;
            }

            Player shooter = (Player) entity.getShooter();

            entity.getNearbyEntities(5, 5, 5).forEach(nearby -> {
                if (nearby instanceof Player && nearby != entity.getShooter()) {
                    if (GameManager.getInstance().getSpectators().contains(nearby.getUniqueId())) return;

                    Player target = (Player) nearby;

                    if (target.hasPotionEffect(PotionEffectType.BLINDNESS)) {
                        return;
                    }

                    target.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 20 * 5, 1));

                    shooter.sendMessage(CC.translate("&eYou blinded &7" + ((Player) nearby).getName()));

                    target.sendMessage(
                            CC.translate("&6&l[&e✷&6&l] &eYou have been blinded by &6&l" + shooter.getName()));
                    target.sendMessage(
                            CC.translate("&8[&c⚠&8] &cYou will not be able to see anything for 5 seconds."));
                }
            });
        });
    }
}