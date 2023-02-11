package cc.stormworth.meetup.style.hcf.enchantment.task;

import cc.stormworth.meetup.Meetup;
import cc.stormworth.meetup.managers.UserManager;
import cc.stormworth.meetup.style.hcf.enchantment.CustomEnchant;
import cc.stormworth.meetup.style.hcf.enchantment.Effect;
import org.bukkit.Bukkit;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

public class CustomEnchantsTask extends BukkitRunnable {

    public CustomEnchantsTask() {
        new CustomEnchant("&bSpeed II", new Effect(PotionEffectType.SPEED, 2));
        new CustomEnchant("&6Fire Resistance I", new Effect(PotionEffectType.FIRE_RESISTANCE, 1));
        this.runTaskTimer(Meetup.getInstance(), 0L, 20L);
    }

    @Override
    public void run() {
      Bukkit.getOnlinePlayers().forEach(CustomEnchant::updateEffects);
    }
}
