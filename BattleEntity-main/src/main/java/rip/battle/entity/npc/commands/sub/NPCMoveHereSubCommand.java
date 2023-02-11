package rip.battle.entity.npc.commands.sub;

import cc.stormworth.core.util.chat.CC;
import com.jonahseguin.drink.annotation.Command;
import com.jonahseguin.drink.annotation.Sender;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import rip.battle.entity.npc.NPCManager;
import rip.battle.entity.npc.api.NPC;

@RequiredArgsConstructor
public class NPCMoveHereSubCommand {

    private final NPCManager npcManager;

    @Command(name = "movehere", desc = "Allows you to move a npc to your location", usage = "<name>", aliases = {"mh", "s", "teleporto", "teleporthere"})
    public void moveHere(@Sender Player player, String name) {

        NPC npc = npcManager.getNPC(name);

        if (npc == null) {
            player.sendMessage(CC.translate("&cA npc with that name does not exist."));
            return;
        }

        npc.teleport(player.getLocation());

        player.sendMessage(CC.translate("&aYou have moved the npc &f" + name + "&a to your location."));
    }
}
