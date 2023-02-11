package rip.battle.entity.hologram.commands.sub;

import cc.stormworth.core.util.chat.CC;
import com.jonahseguin.drink.annotation.Command;
import com.jonahseguin.drink.annotation.Sender;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import rip.battle.entity.hologram.HologramManager;
import rip.battle.entity.hologram.api.Hologram;

@RequiredArgsConstructor
public class HologramMoveHereSubCommand {

    private final HologramManager hologramManager;

    @Command(name = "movehere", desc = "Allows you to move a hologram to your location", usage = "<name>", aliases = {"move", "moveto", "teleporto", "s"})
    public void movehere(@Sender Player player, String name) {

        Hologram hologram = hologramManager.getHologram(name);

        if (hologram == null) {
            player.sendMessage(CC.translate("&cA hologram with that name does not exist."));
            return;
        }

        hologram.teleport(player.getLocation());
        player.sendMessage(CC.translate("&aYou have moved the hologram &e" + name + "&a to your location."));
    }

}
