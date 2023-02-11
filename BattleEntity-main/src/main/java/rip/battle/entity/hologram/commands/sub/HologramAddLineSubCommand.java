package rip.battle.entity.hologram.commands.sub;

import cc.stormworth.core.util.chat.CC;
import com.jonahseguin.drink.annotation.Command;
import com.jonahseguin.drink.annotation.Sender;
import com.jonahseguin.drink.annotation.Text;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import rip.battle.entity.hologram.HologramManager;
import rip.battle.entity.hologram.api.Hologram;

@RequiredArgsConstructor
public class HologramAddLineSubCommand {

    private final HologramManager hologramManager;

    @Command(name = "addline", desc = "Allows you to add a line to a hologram", usage = "<name> <line>")
    public void addline(@Sender Player player, String name, @Text String line) {

        Hologram hologram = hologramManager.getHologram(name);

        if (hologram == null) {
            player.sendMessage(CC.translate("&cA hologram with that name does not exist."));
            return;
        }

        hologram.addLine(line);
        player.sendMessage(CC.translate("&aYou have added a line to the hologram &e" + name + "&a."));
    }

}
