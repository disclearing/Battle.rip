package rip.battle.crates.reward.promt;

import cc.stormworth.core.menu.Menu;
import cc.stormworth.core.util.chat.CC;
import lombok.RequiredArgsConstructor;
import net.minecraft.util.com.google.common.collect.Lists;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.StringPrompt;
import org.bukkit.entity.Player;
import rip.battle.crates.crate.Crate;
import rip.battle.crates.reward.Reward;
import rip.battle.crates.reward.menus.RewardEditMenu;
import rip.battle.crates.reward.RewardType;

@RequiredArgsConstructor
public class RewardAddCommandPrompt extends StringPrompt {

  private final Reward reward;
  private final Crate crate;
  private final Menu menu;

  @Override
  public String getPromptText(ConversationContext context) {
    return CC.translate("&7Enter the command you want to add. Or type &6cancel&7 to go back.");
  }

  @Override
  public Prompt acceptInput(ConversationContext context, String input) {

    Player player = (Player) context.getForWhom();

    if (input.equalsIgnoreCase("cancel")) {
      new RewardEditMenu(reward, crate).openMenu(player);
      return END_OF_CONVERSATION;
    }

    reward.setType(RewardType.COMMAND);

    if(reward.getCommands() == null) {
      reward.setCommands(Lists.newArrayList());
    }

    reward.getCommands().add(input);

    menu.openMenu(player);
    player.sendMessage(CC.translate("&aCommand &e" + input + "&e has been added to the reward."));
    return END_OF_CONVERSATION;
  }
}