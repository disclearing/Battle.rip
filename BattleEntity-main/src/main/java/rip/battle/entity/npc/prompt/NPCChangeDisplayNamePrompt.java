package rip.battle.entity.npc.prompt;

import cc.stormworth.core.util.chat.CC;
import lombok.RequiredArgsConstructor;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.StringPrompt;
import org.bukkit.entity.Player;
import rip.battle.entity.EntityPlugin;
import rip.battle.entity.npc.api.NPC;
import rip.battle.entity.npc.menu.NPCEditMenu;

@RequiredArgsConstructor
public class NPCChangeDisplayNamePrompt extends StringPrompt {

    private final EntityPlugin plugin;
    private final NPC npc;

    @Override
    public String getPromptText(ConversationContext conversationContext) {
        return CC.translate("&eEnter a new display name for the NPC &6" + npc.getName() + "&e or type &ccancel &eto cancel.");
    }

    @Override
    public Prompt acceptInput(ConversationContext context, String input) {

        Player player = (Player) context.getForWhom();

        if(input.equalsIgnoreCase("cancel")){
            new NPCEditMenu(npc, plugin).open(player);
            return Prompt.END_OF_CONVERSATION;
        }

        npc.setDisplayName(input);

        player.sendMessage(CC.translate("&aSet the display name to &e" + input + "&a."));

        new NPCEditMenu(npc, plugin).open(player);
        return Prompt.END_OF_CONVERSATION;
    }
}
