package rip.battle.entity.hologram.prompt;

import cc.stormworth.core.util.chat.CC;
import lombok.RequiredArgsConstructor;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.StringPrompt;
import org.bukkit.entity.Player;
import rip.battle.entity.EntityPlugin;
import rip.battle.entity.hologram.api.Hologram;
import rip.battle.entity.hologram.lines.HologramLine;
import rip.battle.entity.hologram.lines.impl.ItemLine;
import rip.battle.entity.hologram.lines.impl.TextLine;
import rip.battle.entity.hologram.menu.HologramManageLinesMenu;

@RequiredArgsConstructor
public class HologramEditLinePrompt extends StringPrompt {

    private final EntityPlugin plugin;
    private final Hologram hologram;
    private final HologramLine<?> line;

    @Override
    public String getPromptText(ConversationContext context) {

        if (line instanceof ItemLine){
            return CC.translate("&7Enter &aconfirm&7 to set item in hand as the item for this line or type &ccancel &7to cancel.");
        }

        return CC.translate("&7Enter the text for the line or type &ccancel &7to cancel.");
    }

    @Override
    public Prompt acceptInput(ConversationContext context, String input) {

        Player player = (Player) context.getForWhom();

        if (input.equalsIgnoreCase("cancel")) {
            new HologramManageLinesMenu(plugin, hologram).open(player);
            return Prompt.END_OF_CONVERSATION;
        }

        if (line instanceof ItemLine){
            if (input.equalsIgnoreCase("confirm")){
                ((ItemLine) line).setLine(player.getItemInHand());

                line.updateForCurrentWatchers();
                player.sendMessage(CC.translate("&aSet item in hand as the item for this line."));

                new HologramManageLinesMenu(plugin, hologram).open(player);
                return Prompt.END_OF_CONVERSATION;
            }
        }else{
            TextLine textLine = (TextLine) line;

            textLine.setLine(input);
            textLine.updateForCurrentWatchers();

            player.sendMessage(CC.translate("&aSet text for this line."));
            new HologramManageLinesMenu(plugin, hologram).open(player);
            return Prompt.END_OF_CONVERSATION;
        }

        return this;
    }
}
