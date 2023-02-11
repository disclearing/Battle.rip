package rip.battle.entity.hologram.prompt;

import cc.stormworth.core.util.chat.CC;
import lombok.RequiredArgsConstructor;
import org.bukkit.Sound;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.StringPrompt;
import org.bukkit.entity.Player;
import rip.battle.entity.EntityPlugin;
import rip.battle.entity.hologram.api.Hologram;
import rip.battle.entity.hologram.menu.HologramManageLinesMenu;

@RequiredArgsConstructor
public class HologramAddLinePrompt extends StringPrompt {

    private final EntityPlugin plugin;
    private final Hologram hologram;

    @Override
    public String getPromptText(ConversationContext conversationContext) {
        return CC.translate("&7Enter the text for the new line or type &ccancel &7to cancel.");
    }

    @Override
    public Prompt acceptInput(ConversationContext context, String input) {

        Player player = (Player) context.getForWhom();

        if (input.equalsIgnoreCase("cancel")) {
            new HologramManageLinesMenu(plugin, hologram).open(player);
            return Prompt.END_OF_CONVERSATION;
        }

        hologram.addLine(input);

        player.sendMessage(CC.translate("&aAdded line to hologram."));
        player.playSound(player.getLocation(), Sound.NOTE_PLING, 1, 1);
        new HologramManageLinesMenu(plugin, hologram).open(player);
        return Prompt.END_OF_CONVERSATION;
    }
}
