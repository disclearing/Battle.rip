package rip.battle.entity.listeners;

import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import rip.battle.entity.Entity;
import rip.battle.entity.EntityManager;
import rip.battle.entity.hologram.CraftHologram;
import rip.battle.entity.hologram.HologramManager;
import rip.battle.entity.npc.NPCManager;

@RequiredArgsConstructor
public class PlayerListener implements Listener {
    @EventHandler
    public void onQuit(PlayerQuitEvent event){
        Player player = event.getPlayer();

        for (Entity entity : EntityManager.getEntities().values()){
            entity.destroy(player);
        }
    }

}
