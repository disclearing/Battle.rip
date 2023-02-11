package rip.battle.entity.hologram.commands.sub;

import cc.stormworth.core.util.chat.CC;
import com.jonahseguin.drink.annotation.Command;
import com.jonahseguin.drink.annotation.Sender;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import rip.battle.entity.hologram.HologramManager;
import rip.battle.entity.hologram.api.Hologram;

@RequiredArgsConstructor
public class HologramSetLineSubCommand {

    private final HologramManager hologramManager;

    @Command(name = "setline", desc = "Allows you to set a line of a hologram", usage = "<name> <line> <text>")
    public void setline(@Sender Player player, String name, int line, String text) {

        Hologram hologram = hologramManager.getHologram(name);

        if (hologram == null) {
            player.sendMessage(CC.translate("&cA hologram with that name does not exist."));
            return;
        }

        hologram.setLine(line, text);

        player.sendMessage(CC.translate("&aYou have set a line of the hologram &e" + name + "&a."));
    }

}
