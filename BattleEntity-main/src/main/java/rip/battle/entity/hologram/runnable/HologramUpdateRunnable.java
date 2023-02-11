package rip.battle.entity.hologram.runnable;

import lombok.RequiredArgsConstructor;
import rip.battle.entity.hologram.api.Hologram;
import rip.battle.entity.hologram.HologramManager;
import rip.battle.entity.hologram.impl.UpdatableHologram;

@RequiredArgsConstructor
public class HologramUpdateRunnable implements Runnable {

    private final HologramManager hologramManager;

    @Override
    public void run() {
        for (Hologram hologram : hologramManager.getHolograms().values()) {
            if (hologram instanceof UpdatableHologram){
                UpdatableHologram updatableHologram = (UpdatableHologram) hologram;

                if (updatableHologram.canUpdate() && !updatableHologram.getLines().isEmpty()){

                    updatableHologram.getCurrentWatchersPlayers().forEach(updatableHologram::update);

                    updatableHologram.setLastUpdate(System.currentTimeMillis());
                }
            }
        }
    }
}
