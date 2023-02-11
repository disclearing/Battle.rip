package rip.battle.crates.crate.prompt;

import cc.stormworth.core.util.chat.CC;
import java.util.LinkedList;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.StringPrompt;
import org.bukkit.entity.Player;
import rip.battle.crates.crate.Crate;
import rip.battle.crates.crate.menus.CrateBroadcastMenu;
import rip.battle.crates.reward.Reward;

public class CrateBroadCastPrompt extends StringPrompt {

  private final Crate crate;
  private final Reward reward;
  private final LinkedList<String> lines;

  public CrateBroadCastPrompt(Crate crate, Reward reward) {
    this.reward = reward;
    this.crate = crate;
    this.lines = reward.getBroadcast();
  }

  @Override
  public String getPromptText(ConversationContext context) {
    return CC.translate("&7Enter a lines to broadcast when the crate is opened. "
        + "&7&oOr type &ccancel&7&o to cancel or type &aconfirm&7 to end: ");
  }

  @Override
  public Prompt acceptInput(ConversationContext context, String input) {

    Player player = (Player) context.getForWhom();

    if (input.equalsIgnoreCase("cancel") || input.equalsIgnoreCase("confirm")) {

      reward.setBroadcast(lines);

      new CrateBroadcastMenu(crate, reward).openMenu(player);
      return Prompt.END_OF_CONVERSATION;
    }

    lines.add(CC.translate(input.replace("{name}", crate.getDisplayName())));

    player.sendMessage(CC.translate("&aAdded new broadcast line"));
    return this;
  }
}