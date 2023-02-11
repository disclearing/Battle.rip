package cc.stormworth.meetup.scenarios.impl;

import cc.stormworth.meetup.scenarios.Scenario;
import cc.stormworth.meetup.util.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;

public class OPKitsScenario extends Scenario {

    public OPKitsScenario() {
        super("OP Kits", new ItemBuilder(Material.DIAMOND_CHESTPLATE).addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 3).build(), true, "All kits given at the start of", "the game contain better gear.");
    }
}
