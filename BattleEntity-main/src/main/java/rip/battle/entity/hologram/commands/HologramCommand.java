package rip.battle.entity.hologram.commands;

import cc.stormworth.core.util.chat.CC;
import com.jonahseguin.drink.annotation.Command;
import com.jonahseguin.drink.annotation.Require;
import com.jonahseguin.drink.annotation.Sender;
import org.bukkit.entity.Player;

public class HologramCommand {

    @Command(name = "", desc = "Allows you to manage your holograms", usage = "<subcommand>")
    @Require("battle.entity.hologram")
    public void hologram(@Sender Player player) {
        player.sendMessage(CC.translate("&e&lHologram Commands"));

        player.sendMessage(CC.translate("&e/hologram create <name> &7- &fAllows you to create a hologram."));
        player.sendMessage(CC.translate("&e/hologram delete <name> &7- &fAllows you to delete a hologram."));
        player.sendMessage(CC.translate("&e/hologram addline <name> <line> &7- &fAllows you to add a line to a hologram."));
        player.sendMessage(CC.translate("&e/hologram setline <name> <number> <line> &7- &fAllows you to set a line on a hologram."));
        player.sendMessage(CC.translate("&e/hologram additemline <name> &7- &fAllows you to add an item line to a hologram."));
        player.sendMessage(CC.translate("&e/hologram setitemline <name> <number> <item> &7- &fAllows you to set an item line on a hologram."));
        player.sendMessage(CC.translate("&e/hologram removeline <name> <number> &7- &fAllows you to remove a line from a hologram."));
        player.sendMessage(CC.translate("&e/hologram teleport <name> &7- &fAllows you to teleport to a hologram."));
        player.sendMessage(CC.translate("&e/hologram movehere <name> &7- &fAllows you to move a hologram to your location."));
        player.sendMessage(CC.translate("&e/hologram list &7- &fAllows you to list all holograms."));
        player.sendMessage(CC.translate("&e/hologram edit <name> &7- &fAllows you to edit a hologram."));

    }

}
