package cc.stormworth.meetup.scenarios.impl;

import cc.stormworth.meetup.scenarios.Scenario;
import cc.stormworth.meetup.util.ItemBuilder;
import org.bukkit.Material;

public class HorseManiaScenario extends Scenario {
    public HorseManiaScenario() {
        //super("HorseMania", new ItemBuilder(Material.EGG).setDurability(100));
        super("Horse Mania", new ItemBuilder(Material.EGG).setDurability(100).build(), true,
                "Everyone will be spawned mounted to a horse",
                "when the game starts");
    }


}
