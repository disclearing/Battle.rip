package rip.battle.entity.npc.commands.sub;

import cc.stormworth.core.util.chat.CC;
import com.jonahseguin.drink.annotation.Command;
import com.jonahseguin.drink.annotation.Sender;
import lombok.RequiredArgsConstructor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import org.bukkit.entity.Player;
import rip.battle.entity.hologram.api.Hologram;
import rip.battle.entity.npc.NPCManager;
import rip.battle.entity.npc.api.NPC;

import java.util.Map;

@RequiredArgsConstructor
public class NPCListCommand {

    private final NPCManager npcManager;

    @Command(name = "list", desc = "List all NPCs", usage = "/npc list", aliases = {"l", "ls"})
    public void list(@Sender Player player){
        Map<String, NPC> npcs = npcManager.getNpcs();

        if (npcs.isEmpty()) {
            player.sendMessage(CC.translate("&cThere are no npcs."));
            return;
        }

        player.sendMessage(CC.translate("&aThere are &e" + npcs.size() + " &anpcs."));

        npcs.forEach((name, npc) -> {

            ComponentBuilder builder = new ComponentBuilder(CC.translate("&e" + name + " &b[Click to teleport]"));
            builder.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(CC.translate("&7Click to teleport")).create()));
            builder.event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/npc tp " + name));

            player.spigot().sendMessage(builder.create());
        });
    }

}
