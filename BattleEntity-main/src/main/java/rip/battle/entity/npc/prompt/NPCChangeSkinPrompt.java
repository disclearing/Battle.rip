package rip.battle.entity.npc.prompt;

import cc.stormworth.core.util.chat.CC;
import cc.stormworth.core.util.skin.SkinTexture;
import lombok.RequiredArgsConstructor;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.StringPrompt;
import org.bukkit.entity.Player;
import rip.battle.entity.EntityPlugin;
import rip.battle.entity.npc.api.NPC;
import rip.battle.entity.npc.menu.NPCSkinMenu;

import java.util.concurrent.CompletableFuture;

@RequiredArgsConstructor
public class NPCChangeSkinPrompt extends StringPrompt {

    private final EntityPlugin plugin;
    private final NPC npc;

    @Override
    public String getPromptText(ConversationContext conversationContext) {
        return CC.translate("&eEnter a username to set the skin to or type &ccancel &eto cancel.");
    }

    @Override
    public Prompt acceptInput(ConversationContext conversationContext, String input) {

        Player player = (Player) conversationContext.getForWhom();

        if(input.equalsIgnoreCase("cancel")){
            new NPCSkinMenu(npc, plugin).open(player);
            return Prompt.END_OF_CONVERSATION;
        }

        CompletableFuture<SkinTexture> skin = SkinTexture.getSkinFromUsername(input);
        player.sendMessage(CC.translate("&eSearching for skin..."));

        skin.thenAccept(skin1 -> {
            if(skin1 == null){
                player.sendMessage(CC.translate("&cCould not find a skin with that username."));
                return;
            }

            npc.setSkin(skin1);
            player.sendMessage(CC.translate("&aSet the skin to &e" + input + "&a."));

            new NPCSkinMenu(npc, plugin).open(player);
        });

        return Prompt.END_OF_CONVERSATION;
    }
}
