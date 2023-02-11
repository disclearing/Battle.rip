package rip.battle.entity.npc.commands.sub;

import cc.stormworth.core.util.chat.CC;
import com.jonahseguin.drink.annotation.Command;
import com.jonahseguin.drink.annotation.Sender;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import rip.battle.entity.npc.NPCManager;
import rip.battle.entity.npc.api.NPC;

@RequiredArgsConstructor
public class NPCSetDisplaynameSubCommand {

    private final NPCManager npcManager;

    @Command(name = "setdisplayname", desc = "Allows you to set the display name of a npc", usage = "<name> <displayname>")
    public void setDisplayname(@Sender Player player, String name, String displayName) {

        NPC npc = npcManager.getNPC(name);

        if (npc == null) {
            player.sendMessage(CC.translate("&cA npc with that name does not exist."));
            return;
        }

        npc.setDisplayName(displayName);
        player.sendMessage(CC.translate("&aYou have set the display name of the npc &f" + name + " &ato &f" + displayName + "&a."));
    }

}
