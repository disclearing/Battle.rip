package rip.battle.entity.hologram.commands.sub;

import cc.stormworth.core.util.chat.CC;
import com.jonahseguin.drink.annotation.Command;
import com.jonahseguin.drink.annotation.Sender;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import rip.battle.entity.hologram.HologramManager;

@RequiredArgsConstructor
public class HologramCreateSubCommand {

    private final HologramManager hologramManager;

    @Command(name = "create", desc = "Allows you to create a hologram", usage = "<name>")
    public void create(@Sender Player player, String name) {

        if (hologramManager.getHologram(name) != null) {
            player.sendMessage(CC.translate("&cA hologram with that name already exists."));
            return;
        }

        hologramManager.createHologram(name, player.getLocation());
        player.sendMessage(CC.translate("&aYou have created a hologram with the name &e" + name + "&a."));
    }

}
