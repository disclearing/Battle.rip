package cc.stormworth.meetup.style.hcf.enchantment;

import cc.stormworth.meetup.managers.UserManager;
import cc.stormworth.meetup.user.UserData;
import cc.stormworth.meetup.util.ItemUtil;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
public class CustomEnchant {

    private static final Map<String, CustomEnchant> enchants = new HashMap<>();
    private final String name;
    private final Effect effect;

    public CustomEnchant(String name, Effect effect) {
        this.name = name;
        this.effect = effect;

        // Distinct
        if (!enchants.containsKey(name))
            enchants.put(name, this);
    }

    public static CustomEnchant getByName(String name) {
        return enchants.get(name);
    }

    public static void updateEffects(Player player) {
        UserData user = UserManager.getInstance().getUser(player.getUniqueId());
        user.getActiveEnchants().forEach(enchant -> player.removePotionEffect(enchant.getEffect().getType()));
        user.getActiveEnchants().clear();

        for (ItemStack armor : player.getInventory().getArmorContents()) {
            List<CustomEnchant> enchants = ItemUtil.getCustomEnchants(armor);
            if (enchants.isEmpty()) continue;

            for (CustomEnchant enchant : enchants) {
                PotionEffect effect = new PotionEffect(enchant.getEffect().getType(), Integer.MAX_VALUE, enchant.getEffect().getAmplifier() - 1);
                player.addPotionEffect(effect);
                user.getActiveEnchants().add(enchant);
            }
        }
    }
}
