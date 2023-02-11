package rip.battle.entity.hologram.commands.sub;

import cc.stormworth.core.util.chat.CC;
import com.jonahseguin.drink.annotation.Command;
import com.jonahseguin.drink.annotation.Sender;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import rip.battle.entity.hologram.HologramManager;
import rip.battle.entity.hologram.api.Hologram;

@RequiredArgsConstructor
public class HologramRemoveLineSubCommand {

    private final HologramManager hologramManager;

    @Command(name = "removeline", desc = "Allows you to remove a line of a hologram", usage = "<name> <line>")
    public void removeLine(@Sender Player player, String name, int line) {

        Hologram hologram = hologramManager.getHologram(name);

        if (hologram == null) {
            player.sendMessage(CC.translate("&cA hologram with that name does not exist."));
            return;
        }

        hologram.removeLine(line);

        player.sendMessage(CC.translate("&aYou have removed a line of the hologram &e" + name + "&a."));
    }


}
