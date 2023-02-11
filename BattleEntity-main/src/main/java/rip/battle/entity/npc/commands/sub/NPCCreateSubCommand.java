package rip.battle.entity.npc.commands.sub;

import cc.stormworth.core.util.chat.CC;
import com.jonahseguin.drink.annotation.Command;
import com.jonahseguin.drink.annotation.Sender;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import rip.battle.entity.npc.NPCManager;

@RequiredArgsConstructor
public class NPCCreateSubCommand {

    private final NPCManager npcManager;

    @Command(name = "create", desc = "Allows you to create a npc", usage = "<name>")
    public void create(@Sender Player player, String name) {

        if (npcManager.getNPC(name) != null) {
            player.sendMessage(CC.translate("&cA npc with that name already exists."));
            return;
        }

        npcManager.createNPC(name, player.getLocation());
        player.sendMessage(CC.translate("&aYou have created a npc with the name &f" + name + "&a."));
    }

}
