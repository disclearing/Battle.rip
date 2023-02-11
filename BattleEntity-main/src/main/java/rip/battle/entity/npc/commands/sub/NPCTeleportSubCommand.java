package rip.battle.entity.npc.commands.sub;

import cc.stormworth.core.util.chat.CC;
import com.jonahseguin.drink.annotation.Command;
import com.jonahseguin.drink.annotation.Sender;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import rip.battle.entity.npc.NPCManager;
import rip.battle.entity.npc.api.NPC;

@RequiredArgsConstructor
public class NPCTeleportSubCommand {

    private final NPCManager npcManager;

    @Command(name = "teleport", desc = "Allows you to teleport to a hologram", usage = "<name>", aliases = "tp")
    public void teleport(@Sender Player player, String name) {

        NPC npc = npcManager.getNPC(name);

        if (npc == null) {
            player.sendMessage(CC.translate("&cA npc with that name does not exist."));
            return;
        }

        player.teleport(npc.getLocation());

        player.sendMessage(CC.translate("&aYou have teleported to the npc &f" + name + "&a."));
    }

}
