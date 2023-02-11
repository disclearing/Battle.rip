package rip.battle.entity.npc.commands.sub;

import cc.stormworth.core.util.chat.CC;
import com.jonahseguin.drink.annotation.Command;
import com.jonahseguin.drink.annotation.Sender;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import rip.battle.entity.npc.NPCManager;
import rip.battle.entity.npc.api.NPC;

@RequiredArgsConstructor
public class NPCDeleteSubCommand {

    private final NPCManager npcManager;

    @Command(name = "delete", desc = "Allows you to delete a npc", usage = "<name>")
    public void delete(@Sender Player player, String name) {

        NPC npc = npcManager.getNPC(name);

        if (npc == null) {
            player.sendMessage(CC.translate("&cA npc with that name does not exist."));
            return;
        }

        npc.destroy();
        npcManager.removeNPC(npc);

        player.sendMessage(CC.translate("&aYou have deleted the npc &f" + name + "&a."));
    }

}
