package rip.battle.entity.npc.commands.sub;

import cc.stormworth.core.util.chat.CC;
import com.jonahseguin.drink.annotation.Command;
import com.jonahseguin.drink.annotation.Sender;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import rip.battle.entity.npc.NPCManager;
import rip.battle.entity.npc.api.NPC;

@RequiredArgsConstructor
public class NPCSetItemInHandSubCommand {

    private final NPCManager npcManager;

    @Command(name = "setitemhand", desc = "Sets the item in hand of a npc", aliases = "setitem")
    public void setBoots(@Sender Player player){
        NPC npc = npcManager.getNPC(player.getName());

        if (npc == null) {
            player.sendMessage(CC.translate("&cA npc with that name does not exist."));
            return;
        }

        npc.setItemInHand(player.getInventory().getItemInHand());
        player.sendMessage(CC.translate("&aYou have set the item in hand of the npc &f" + npc.getName() + "&a."));
    }


}
