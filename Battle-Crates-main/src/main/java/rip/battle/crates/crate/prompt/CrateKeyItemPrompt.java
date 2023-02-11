package rip.battle.crates.crate.prompt;

import cc.stormworth.core.util.chat.CC;
import lombok.RequiredArgsConstructor;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.StringPrompt;
import org.bukkit.entity.Player;
import rip.battle.crates.crate.Crate;
import rip.battle.crates.crate.menus.CrateEditMenu;

@RequiredArgsConstructor
public class CrateKeyItemPrompt extends StringPrompt {

  private final Crate crate;

  @Override
  public String getPromptText(ConversationContext conversationContext) {
    return CC.translate("&7Put item in your hand and type &6accept&7 to confirm. Or type &6cancel&7 to cancel.");
  }

  @Override
  public Prompt acceptInput(ConversationContext context, String input) {

    Player player = (Player) context.getForWhom();

    if (input.equalsIgnoreCase("cancel")) {
      player.sendMessage(CC.translate("&cCrate rename cancelled."));
      new CrateEditMenu(crate).openMenu(player);
      return Prompt.END_OF_CONVERSATION;
    }

    if(player.getItemInHand() == null) {
      player.sendMessage(CC.translate("&cYou must have an item in your hand to select it."));
      return this;
    }

    if (input.equalsIgnoreCase("accept")) {
      crate.setKey(player.getItemInHand());
      player.sendMessage(CC.translate("&aCrate key item set to &6" + player.getItemInHand().getType().name() + "&a."));

      new CrateEditMenu(crate).openMenu(player);
      return Prompt.END_OF_CONVERSATION;
    }

    return this;
  }
}