package org.contrum.lobby.managelobby.command.sub;

import org.contrum.lobby.LobbyPlugin;
import com.jonahseguin.drink.annotation.Command;
import com.jonahseguin.drink.annotation.Sender;
import lombok.AllArgsConstructor;
import org.bukkit.entity.Player;
import rip.battle.entity.hologram.api.Hologram;

/**
 * @author UKry
 * Created: 18/11/2022
 * Project BattleHub
 **/

@AllArgsConstructor
public class LobbyHoloSubCommand {

    private LobbyPlugin plugin;

    @Command(name = "setholo", desc = "")
    public void onCommand(@Sender Player player, String value) {
        Hologram holo = plugin.getLobbyManager().getHolograms().get(value.toLowerCase());
        if(holo == null) {
            player.sendMessage("That holo not exist!");
            return;
        }
        holo.spawn();
        holo.teleport(player.getLocation());
        player.sendMessage("Successful add the holo " + value);
    }
}