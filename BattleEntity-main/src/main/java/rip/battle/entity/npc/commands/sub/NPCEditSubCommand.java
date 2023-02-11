package rip.battle.entity.npc.commands.sub;

import com.jonahseguin.drink.annotation.Command;
import com.jonahseguin.drink.annotation.Sender;
import lombok.RequiredArgsConstructor;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import rip.battle.entity.EntityPlugin;
import rip.battle.entity.npc.NPCManager;
import rip.battle.entity.npc.api.NPC;
import rip.battle.entity.npc.menu.NPCEditMenu;

@RequiredArgsConstructor
public class NPCEditSubCommand {

    private final EntityPlugin plugin;
    private final NPCManager npcManager;

    @Command(name = "edit", desc = "Edit an NPC")
    public void edit(@Sender Player player, String name){
        NPC npc = npcManager.getNPC(name);

        if (npc == null){
            player.sendMessage(ChatColor.RED + "NPC not found.");
            return;
        }

        new NPCEditMenu(npc, plugin).open(player);
    }

}
