package rip.battle.entity.npc.commands.sub;

import cc.stormworth.core.util.chat.CC;
import com.jonahseguin.drink.annotation.Command;
import com.jonahseguin.drink.annotation.Sender;
import com.jonahseguin.drink.annotation.Text;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import rip.battle.entity.hologram.impl.UpdatableHologram;
import rip.battle.entity.npc.NPCManager;
import rip.battle.entity.npc.api.NPC;

@RequiredArgsConstructor
public class NPCAddLineSubCommand {

    private final NPCManager npcManager;

    @Command(name = "addline", desc = "Allows you to add a line to a npc", usage = "<name> <line>")
    public void addLine(@Sender Player player, String name, @Text String line) {

        NPC npc = npcManager.getNPC(name);

        if (npc == null) {
            player.sendMessage(CC.translate("&cA npc with that name does not exist."));
            return;
        }

        if (npc.getHologram() == null){
            npc.setHologram(new UpdatableHologram(npc.getName() + "_hologram", npc.getLocation()));
        }

        npc.getHologram().addLine(line);

        player.sendMessage(CC.translate("&aYou have added a line to the npc &f" + name + "&a."));
    }

}
