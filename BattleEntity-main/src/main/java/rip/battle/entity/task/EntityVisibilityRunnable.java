package rip.battle.entity.task;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import rip.battle.entity.Entity;
import rip.battle.entity.EntityManager;
import rip.battle.entity.hologram.lines.impl.TextLine;

public class EntityVisibilityRunnable implements Runnable{

    @Override
    public void run() {

        for(Player player : Bukkit.getOnlinePlayers()){
            for (Entity entity : EntityManager.getEntities().values()){

                if (entity instanceof TextLine){
                    TextLine textLine = (TextLine) entity;

                    if (textLine.isMultiLine()){
                        continue;
                    }

                }

                if (entity.getCurrentWatchersPlayers().contains(player) && !entity.isVisibleTo(player)){
                    entity.destroy(player);
                }else if (!entity.getCurrentWatchersPlayers().contains(player) && entity.isVisibleTo(player)){
                    entity.spawn(player);
                }
            }
        }

    }
}
