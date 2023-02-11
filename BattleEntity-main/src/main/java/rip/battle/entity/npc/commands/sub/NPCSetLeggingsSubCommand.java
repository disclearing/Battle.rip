package rip.battle.entity.npc.commands.sub;

import cc.stormworth.core.util.chat.CC;
import com.jonahseguin.drink.annotation.Command;
import com.jonahseguin.drink.annotation.Sender;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import rip.battle.entity.npc.NPCManager;
import rip.battle.entity.npc.api.NPC;

@RequiredArgsConstructor
public class NPCSetLeggingsSubCommand {

    private final NPCManager npcManager;

    @Command(name = "setleggings", desc = "Sets the leggings of a npc")
    public void setBoots(@Sender Player player){
        NPC npc = npcManager.getNPC(player.getName());

        if (npc == null) {
            player.sendMessage(CC.translate("&cA npc with that name does not exist."));
            return;
        }

        npc.setLeggings(player.getInventory().getLeggings());
        player.sendMessage(CC.translate("&aYou have set the leggings of the npc &f" + npc.getName() + "&a."));
    }


}
