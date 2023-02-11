package rip.battle.entity.npc.commands.sub;

import cc.stormworth.core.util.chat.CC;
import com.jonahseguin.drink.annotation.OptArg;
import com.jonahseguin.drink.annotation.Sender;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import rip.battle.entity.hologram.lines.HologramLine;
import rip.battle.entity.hologram.lines.impl.ItemLine;
import rip.battle.entity.hologram.lines.impl.TextLine;
import rip.battle.entity.npc.NPCManager;
import rip.battle.entity.npc.api.NPC;

@RequiredArgsConstructor
public class NPCSetLineSubCommand {

    private final NPCManager npcManager;

    public void setLine(@Sender Player player, String name, int line, @OptArg String text) {

        NPC npc = npcManager.getNPC(name);

        if (npc == null) {
            player.sendMessage(CC.translate("&cA npc with that name does not exist."));
            return;
        }

        if (npc.getHologram() == null) {
            player.sendMessage(CC.translate("&cThis npc does not have a hologram."));
            return;
        }

        if (line > npc.getHologram().getLines().size()) {
            player.sendMessage(CC.translate("&cThis npc does not have a line with that index."));
            return;
        }

        HologramLine<?> hologramLine = npc.getHologram().getLines().get(line);

        if (hologramLine instanceof ItemLine){

            if (text != null){
                player.sendMessage(CC.translate("&cYou cannot set the text of an item line."));
                return;
            }

            ItemStack item = player.getItemInHand();

            if (item == null){
                player.sendMessage(CC.translate("&cYou must be holding an item to set the item line."));
                return;
            }

            ((ItemLine) hologramLine).setItemStack(item);
            hologramLine.updateForCurrentWatchers();

            player.sendMessage(CC.translate("&aYou have set the item line &f" + line + " &afor the npc &f" + name + "&a."));
        }else {

            if (text == null) {
                player.sendMessage(CC.translate("&cYou must provide text to set the text line."));
                return;
            }

            ((TextLine) hologramLine).setText(other -> text);

            TextLine textLine = (TextLine) hologramLine;

            textLine.setLine(text);
            hologramLine.updateForCurrentWatchers();

            player.sendMessage(CC.translate("&aYou have set the line &f" + line + " &afor the npc &f" + name + "&a."));
        }

    }

}
