package rip.battle.entity.hologram.prompt;

import cc.stormworth.core.util.chat.CC;
import lombok.RequiredArgsConstructor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.StringPrompt;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import rip.battle.entity.EntityPlugin;
import rip.battle.entity.hologram.api.Hologram;
import rip.battle.entity.hologram.menu.HologramManageLinesMenu;

@RequiredArgsConstructor
public class HologramAddItemLinePrompt  extends StringPrompt {

    private final EntityPlugin plugin;
    private final Hologram hologram;

    @Override
    public String getPromptText(ConversationContext conversationContext) {
        return CC.translate("&7Enter &aconfirm&7 to set your item in hand as the line or type &ccancel &7to cancel.");
    }

    @Override
    public Prompt acceptInput(ConversationContext context, String input) {

        Player player = (Player) context.getForWhom();

        if (input.equalsIgnoreCase("cancel")) {
            new HologramManageLinesMenu(plugin, hologram).open(player);
            return Prompt.END_OF_CONVERSATION;
        }

        ItemStack itemStack = player.getItemInHand();

        if (itemStack == null || itemStack.getType() == Material.AIR) {
            player.sendMessage(CC.translate("&cYou must be holding an item to set it as a hologram line."));
            player.playSound(player.getLocation(), Sound.NOTE_BASS, 1, 1);
            return this;
        }

        hologram.addLine(itemStack);

        player.sendMessage(CC.translate("&aAdded line to hologram."));
        player.playSound(player.getLocation(), Sound.NOTE_PLING, 1, 1);
        new HologramManageLinesMenu(plugin, hologram).open(player);
        return Prompt.END_OF_CONVERSATION;
    }
}
