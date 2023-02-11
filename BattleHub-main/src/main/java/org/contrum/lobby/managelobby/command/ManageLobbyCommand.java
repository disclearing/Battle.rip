package org.contrum.lobby.managelobby.command;

import cc.stormworth.core.util.chat.CC;
import org.contrum.lobby.LobbyPlugin;
import org.contrum.lobby.inventory.inventories.MainMenuInventory;
import com.jonahseguin.drink.annotation.Command;
import com.jonahseguin.drink.annotation.Sender;
import org.bukkit.craftbukkit.Main;
import org.bukkit.entity.Player;

/**
 * @author UKry
 * Created: 18/11/2022
 * Project BattleHub
 **/

public class ManageLobbyCommand {

    @Command(name = "", desc = "")
    public void onCommand(@Sender Player player) {
        player.sendMessage(CC.translate("&7/managelobby setnpc <value>"));
        player.sendMessage(CC.translate("&7/managelobby holo <welcome>"));
        MainMenuInventory inv = LobbyPlugin.getPlugin(LobbyPlugin.class).getInventoryManager().getMainMenuInventory();
        inv.open(player);
    }
}