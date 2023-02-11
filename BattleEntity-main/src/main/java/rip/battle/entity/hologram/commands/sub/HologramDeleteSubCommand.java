package rip.battle.entity.hologram.commands.sub;

import cc.stormworth.core.util.chat.CC;
import com.jonahseguin.drink.annotation.Command;
import com.jonahseguin.drink.annotation.Sender;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import rip.battle.entity.hologram.HologramManager;
import rip.battle.entity.hologram.api.Hologram;

@RequiredArgsConstructor
public class HologramDeleteSubCommand {

    private final HologramManager hologramManager;

    @Command(name = "delete", desc = "Allows you to delete a hologram", usage = "<name>")
    public void delete(@Sender Player player,  String name) {
        Hologram hologram = hologramManager.getHologram(name);

        if (hologram == null) {
            player.sendMessage(CC.translate("&cA hologram with that name does not exist."));
            return;
        }

        hologramManager.removeHologram(name);
        player.sendMessage(CC.translate("&aYou have deleted the hologram &e" + name + "&a."));
    }

}
