package cc.stormworth.meetup.style.hcf.enchantment;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.potion.PotionEffectType;

@Getter
@AllArgsConstructor
public class Effect {

    private final PotionEffectType type;
    private final int amplifier;
}
