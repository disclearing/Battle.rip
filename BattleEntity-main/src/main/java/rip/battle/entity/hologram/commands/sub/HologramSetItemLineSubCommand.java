package rip.battle.entity.hologram.commands.sub;

import cc.stormworth.core.util.chat.CC;
import com.jonahseguin.drink.annotation.Command;
import com.jonahseguin.drink.annotation.Sender;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import rip.battle.entity.hologram.HologramManager;
import rip.battle.entity.hologram.api.Hologram;

@RequiredArgsConstructor
public class HologramSetItemLineSubCommand {

    private final HologramManager hologramManager;

    @Command(name = "setitemline", desc = "Allows you to set a item line of a hologram", usage = "<name> <line>")
    public void setItemLine(@Sender Player player, String name, int line) {
        Hologram hologram = hologramManager.getHologram(name);

        if (hologram == null) {
            player.sendMessage(CC.translate("&cA hologram with that name does not exist."));
            return;
        }

        ItemStack item = player.getItemInHand();

        if (item == null) {
            player.sendMessage(CC.translate("&cYou must be holding an item in your hand."));
            return;
        }

        hologram.setLine(line, item);

        player.sendMessage(CC.translate("&aYou have set a item line of the hologram &e" + name + "&a."));
    }

}
