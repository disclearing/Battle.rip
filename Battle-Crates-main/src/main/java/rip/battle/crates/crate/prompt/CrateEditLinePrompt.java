package rip.battle.crates.crate.prompt;

import cc.stormworth.core.util.chat.CC;
import lombok.RequiredArgsConstructor;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.StringPrompt;
import org.bukkit.entity.Player;
import rip.battle.crates.crate.Crate;
import rip.battle.crates.crate.menus.CrateBroadcastMenu;
import rip.battle.crates.reward.Reward;

@RequiredArgsConstructor
public class CrateEditLinePrompt extends StringPrompt {

  private final Crate crate;
  private final Reward reward;
  private final int index;

  @Override
  public String getPromptText(ConversationContext conversationContext) {
    return CC.translate("&7Enter a text to modify the line number &6" + index + "&7. Or type &ccancel&7 to cancel.");
  }

  @Override
  public Prompt acceptInput(ConversationContext context, String input) {
    Player player = (Player) context.getForWhom();

    if (input.equalsIgnoreCase("cancel")) {
      new CrateBroadcastMenu(crate, reward).openMenu(player);
      return Prompt.END_OF_CONVERSATION;
    }

    reward.getBroadcast().set(index, CC.translate(input.replace("{name}", crate.getDisplayName())));

    new CrateBroadcastMenu(crate, reward).openMenu(player);

    return Prompt.END_OF_CONVERSATION;
  }
}