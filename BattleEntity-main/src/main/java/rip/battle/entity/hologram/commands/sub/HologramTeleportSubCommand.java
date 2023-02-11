package rip.battle.entity.hologram.commands.sub;

import cc.stormworth.core.util.chat.CC;
import com.jonahseguin.drink.annotation.Command;
import com.jonahseguin.drink.annotation.Sender;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import rip.battle.entity.hologram.HologramManager;
import rip.battle.entity.hologram.api.Hologram;

@RequiredArgsConstructor
public class HologramTeleportSubCommand {

    private final HologramManager hologramManager;

    @Command(name = "teleport", desc = "Allows you to teleport to a hologram", usage = "<name>", aliases = {"tp"})
    public void teleport(@Sender Player player, String name) {

        Hologram hologram = hologramManager.getHologram(name);

        if (hologram == null) {
            player.sendMessage(CC.translate("&cA hologram with that name does not exist."));
            return;
        }

        player.teleport(hologram.getLocation());
        player.sendMessage(CC.translate("&aYou have teleported to the hologram &e" + name + "&a."));
    }

}
