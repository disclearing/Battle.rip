package org.contrum.lobby.managelobby.command.sub;

import org.contrum.lobby.LobbyPlugin;
import com.jonahseguin.drink.annotation.Command;
import com.jonahseguin.drink.annotation.Sender;
import lombok.AllArgsConstructor;
import org.bukkit.entity.Player;
import rip.battle.entity.npc.api.NPC;

/**
 * @author UKry
 * Created: 18/11/2022
 * Project BattleHub
 **/

@AllArgsConstructor
public class LobbyNPCSubCommand {

    private LobbyPlugin plugin;

    @Command(name = "setnpc", desc = "")
    public void onCommand(@Sender Player player, String value) {
        NPC npc = plugin.getLobbyManager().getNpcs().get(value.toLowerCase());
        if(npc == null) {
            player.sendMessage("That npc not exist!");
            return;
        }
        npc.spawn();
        npc.teleport(player.getLocation());
        player.sendMessage("Successful add the npc " + value);
    }
}