package rip.battle.entity.npc.commands.sub;

import cc.stormworth.core.util.chat.CC;
import com.jonahseguin.drink.annotation.Command;
import com.jonahseguin.drink.annotation.Sender;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import rip.battle.entity.npc.NPCManager;
import rip.battle.entity.npc.api.NPC;

@RequiredArgsConstructor
public class NPCSetArmorSubCommand {

    private final NPCManager npcManager;

    @Command(name = "setarmor", desc = "Allows you to set the armor of a npc", usage = "<name>")
    public void setArmor(@Sender Player player, String name) {

        NPC npc = npcManager.getNPC(name);

        if (npc == null) {
            player.sendMessage(CC.translate("&cA npc with that name does not exist."));
            return;
        }

        npc.setArmor(player);

        player.sendMessage(CC.translate("&aYou have set your armor to the npc &f" + name + "&a."));
    }

}
