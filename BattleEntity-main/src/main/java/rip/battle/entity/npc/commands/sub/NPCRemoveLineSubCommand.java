package rip.battle.entity.npc.commands.sub;

import cc.stormworth.core.util.chat.CC;
import com.jonahseguin.drink.annotation.Command;
import com.jonahseguin.drink.annotation.Sender;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import rip.battle.entity.hologram.api.Hologram;
import rip.battle.entity.npc.NPCManager;
import rip.battle.entity.npc.api.NPC;

@RequiredArgsConstructor
public class NPCRemoveLineSubCommand {

    private final NPCManager npcManager;

    @Command(name = "removeline", desc = "Allows you to remove a line from a npc", usage = "<name> <line>")
    public void removeLine(@Sender Player player, String name, int line) {

        NPC npc = npcManager.getNPC(name);

        if (npc == null) {
            player.sendMessage(CC.translate("&cA npc with that name does not exist."));
            return;
        }

        Hologram hologram = npc.getHologram();

        if (hologram == null) {
            player.sendMessage(CC.translate("&cThis npc does not have a hologram."));
            return;
        }

        if (line > hologram.getLines().size()) {
            player.sendMessage(CC.translate("&cThis npc does not have a line with that index."));
            return;
        }

        if (hologram.getLines().size() == 1) {
            hologram.destroy();
            npc.setHologram(null);
        }else{
            hologram.removeLine(line);
        }

        player.sendMessage(CC.translate("&aYou have removed the line &f" + line + " &afrom the npc &f" + name + "&a."));
    }

}
