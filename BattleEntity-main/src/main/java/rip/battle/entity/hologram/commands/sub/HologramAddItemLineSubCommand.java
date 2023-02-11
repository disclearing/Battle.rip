package rip.battle.entity.hologram.commands.sub;

import cc.stormworth.core.util.chat.CC;
import com.jonahseguin.drink.annotation.Command;
import com.jonahseguin.drink.annotation.Sender;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import rip.battle.entity.hologram.HologramManager;
import rip.battle.entity.hologram.api.Hologram;

@RequiredArgsConstructor
public class HologramAddItemLineSubCommand {

    private final HologramManager hologramManager;

    @Command(name = "additemline", desc = "Allows you to add an item line to a hologram", usage = "<name> <item>")
    public void addline(@Sender Player player, String name) {

        Hologram hologram = hologramManager.getHologram(name);

        if (hologram == null) {
            player.sendMessage(CC.translate("&cA hologram with that name does not exist."));
            return;
        }

        if (player.getItemInHand() == null) {
            player.sendMessage(CC.translate("&cYou must be holding an item in your hand."));
            return;
        }

        hologram.addLine(player.getItemInHand());
        player.sendMessage(CC.translate("&aYou have added an item line to the hologram &e" + name + "&a."));
    }

}
