package cc.stormworth.meetup.scenarios.impl;

import cc.stormworth.meetup.managers.GameManager;
import cc.stormworth.meetup.scenarios.Scenario;
import cc.stormworth.meetup.states.GameState;
import cc.stormworth.meetup.util.ItemUtil;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;

public class GoldenRetrieverScenario extends Scenario implements Listener {
    public GoldenRetrieverScenario() {
        super("Golden Retriever", new ItemStack(Material.GOLDEN_APPLE), false, "Players drop a golden head on death.");
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {

        if (Scenario.getByName("SafeLoot").isActive() || Scenario.getByName("Timebomb").isActive() || Scenario.getByName("NoClean+").isActive())
            return;

        if (GameManager.getInstance().getGameState() == GameState.STARTED)
            event.getDrops().add(ItemUtil.getGoldenHead());
    }
}
