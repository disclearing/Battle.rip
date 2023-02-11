package rip.battle.crates.crate.prompt;

import cc.stormworth.core.util.chat.CC;
import lombok.AllArgsConstructor;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.StringPrompt;
import org.bukkit.entity.Player;
import rip.battle.crates.crate.Crate;
import rip.battle.crates.crate.menus.CrateEditMenu;

@AllArgsConstructor
public class CrateRenamePrompt extends StringPrompt {

  private final Crate crate;

  @Override
  public String getPromptText(ConversationContext conversationContext) {
    return CC.translate("&7Enter a new display name for the crate: &6" + crate.getName() + "&7. Or type &6cancel&7 to cancel.");
  }

  @Override
  public Prompt acceptInput(ConversationContext context, String input) {

    Player player = (Player) context.getForWhom();

    if (input.equalsIgnoreCase("cancel")) {
      player.sendMessage(CC.translate("&cCrate rename cancelled."));
      new CrateEditMenu(crate).openMenu(player);
      return Prompt.END_OF_CONVERSATION;
    }

    crate.setDisplayName(CC.translate(input));

    player.sendMessage(CC.translate("&aCrate displayname change to &6" + crate.getDisplayName() + "&a."));

    crate.destroyHolograms();
    crate.sendHolograms();

    new CrateEditMenu(crate).openMenu(player);
    return Prompt.END_OF_CONVERSATION;
  }
}