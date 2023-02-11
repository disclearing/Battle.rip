package rip.battle.entity.npc.commands.sub;

import cc.stormworth.core.util.chat.CC;
import com.jonahseguin.drink.annotation.Command;
import com.jonahseguin.drink.annotation.Sender;
import com.jonahseguin.drink.annotation.Text;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import rip.battle.entity.hologram.impl.UpdatableHologram;
import rip.battle.entity.npc.NPCManager;
import rip.battle.entity.npc.api.NPC;

@RequiredArgsConstructor
public class NPCAddItemLineSubCommand {

    private final NPCManager npcManager;

    @Command(name = "additemline", desc = "Allows you to add a item line to a npc", usage = "<name>")
    public void addLine(@Sender Player player, String name) {

        NPC npc = npcManager.getNPC(name);

        if (npc == null) {
            player.sendMessage(CC.translate("&cA npc with that name does not exist."));
            return;
        }

        if (npc.getHologram() == null){
            npc.setHologram(new UpdatableHologram(npc.getName() + "_hologram", npc.getLocation()));
        }

        ItemStack item = player.getItemInHand();

        if (item == null) {
            player.sendMessage(CC.translate("&cYou must be holding an item in your hand."));
            return;
        }

        npc.getHologram().addLine(item);

        player.sendMessage(CC.translate("&aYou have added a line to the npc &f" + name + "&a."));
    }


}
