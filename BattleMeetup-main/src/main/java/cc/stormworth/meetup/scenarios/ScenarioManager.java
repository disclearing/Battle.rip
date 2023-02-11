package cc.stormworth.meetup.scenarios;

import cc.stormworth.meetup.scenarios.impl.*;
import org.bukkit.enchantments.Enchantment;

public class ScenarioManager {

    public ScenarioManager() {
        new AbsorptionlessScenario();
        new BowlessScenario();
        new FirelessScenario();
        new GoldenRetrieverScenario();
        new HypixelHeadsScenario();
        new LongShotScenario();
        new NoCleanPlusScenario();
        new NoCleanScenario();
        new NoFallScenario();
        new OPKitsScenario();
        new SafeLootScenario();
        new SoupScenario();
        new TimebombScenario();
        new WebCageScenario();
        new HorseManiaScenario();
    }
}
