package cc.stormworth.meetup.utils;

import cc.stormworth.meetup.Meetup;
import lombok.experimental.UtilityClass;
import org.bukkit.conversations.ConversationFactory;
import org.bukkit.conversations.StringPrompt;
import org.bukkit.entity.Player;

@UtilityClass
public class ChatUtils {

    public void beginPrompt(Player player, StringPrompt prompt) {
        player.closeInventory();
        player.beginConversation(
                new ConversationFactory(Meetup.getInstance())
                        .withFirstPrompt(prompt)
                        .withTimeout(60)
                        .withModality(false)
                        .withLocalEcho(false)
                        .buildConversation(player));
    }
}