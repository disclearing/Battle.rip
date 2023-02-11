package rip.battle.crates.utils;

import lombok.experimental.UtilityClass;
import org.bukkit.ChatColor;
import org.bukkit.conversations.ConversationFactory;
import org.bukkit.conversations.StringPrompt;
import org.bukkit.entity.Player;
import rip.battle.crates.Crates;

@UtilityClass
public class ChatUtils {

  public void beginPrompt(Player player, StringPrompt prompt) {
    player.closeInventory();
    player.beginConversation(
        new ConversationFactory(Crates.getInstance())
            .withFirstPrompt(prompt)
            .withTimeout(60)
            .withModality(false)
            .withLocalEcho(false)
            .buildConversation(player));
  }

  public String getFirstColor(String message) {
    return ChatColor.translateAlternateColorCodes('&', message.substring(0, 2));
  }

}