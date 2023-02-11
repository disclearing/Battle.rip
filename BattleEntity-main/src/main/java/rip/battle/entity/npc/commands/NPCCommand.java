package rip.battle.entity.npc.commands;

import cc.stormworth.core.util.chat.CC;
import com.jonahseguin.drink.annotation.Command;
import com.jonahseguin.drink.annotation.Sender;
import org.bukkit.entity.Player;

public class NPCCommand {

    @Command(name = "", desc = "Allows you to manage your npc", usage = "<subcommand>")
    public void npc(@Sender Player player) {
        player.sendMessage(CC.translate("&e&lNPC Commands"));

        player.sendMessage(CC.translate("&e/npc create <name> &7- &fCreates a npc with the given name."));
        player.sendMessage(CC.translate("&e/npc remove <name> &7- &fRemoves a npc with the given name."));
        player.sendMessage(CC.translate("&e/npc setline <name> <line> [text] &7- &fSets a line on a npc."));
        player.sendMessage(CC.translate("&e/npc removeline <name> <line> &7- &fRemoves a line from a npc."));
        player.sendMessage(CC.translate("&e/npc addline <name> <text> &7- &fAdds a line to a npc."));
        player.sendMessage(CC.translate("&e/npc additemline <name> <item> &7- &fAdds a line with an item to a npc."));
        player.sendMessage(CC.translate("&e/npc teleport <name> &7- &fTeleports you to a npc."));
        player.sendMessage(CC.translate("&e/npc movehere <name> &7- &fSets the location of a npc."));
        player.sendMessage(CC.translate("&e/npc setdisplayname <name> <displayname> &7- &fSets the display name of a npc."));
        player.sendMessage(CC.translate("&e/npc setskin <name> <skin> &7- &fSets the skin of a npc."));
        player.sendMessage(CC.translate("&e/npc setarmor <name> &7- &fSets your armor as the armor of a npc."));
        player.sendMessage(CC.translate("&e/npc setitem <name> <slot> &7- &fSets your item in hand as the item in hand of a npc."));
        player.sendMessage(CC.translate("&e/npc sethelmet <name> &7- &fSets your helmet as the helmet of a npc."));
        player.sendMessage(CC.translate("&e/npc setchestplate <name> &7- &fSets your chestplate as the chestplate of a npc."));
        player.sendMessage(CC.translate("&e/npc setleggings <name> &7- &fSets your leggings as the leggings of a npc."));
        player.sendMessage(CC.translate("&e/npc setboots <name> &7- &fSets your boots as the boots of a npc."));
        player.sendMessage(CC.translate("&e/npc list &7- &fLists all npcs."));
    }

}
