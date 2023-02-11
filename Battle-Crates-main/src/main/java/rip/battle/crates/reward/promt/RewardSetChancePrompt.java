package rip.battle.crates.reward.promt;

import cc.stormworth.core.util.chat.CC;
import lombok.RequiredArgsConstructor;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.StringPrompt;
import org.bukkit.entity.Player;
import rip.battle.crates.crate.Crate;
import rip.battle.crates.reward.Reward;
import rip.battle.crates.reward.menus.RewardEditMenu;

@RequiredArgsConstructor
public class RewardSetChancePrompt extends StringPrompt {

  private final Reward reward;
  private final Crate crate;

  @Override
  public String getPromptText(ConversationContext context) {
    return CC.translate("&7Set the chance of this reward to be given. Or type &6cancel &7to go back.");
  }

  @Override
  public Prompt acceptInput(ConversationContext context, String input) {

    Player player = (Player) context.getForWhom();

    if (input.equalsIgnoreCase("cancel")) {
      new RewardEditMenu(reward, crate).openMenu(player);
      return END_OF_CONVERSATION;
    }

    try {
      double chance = Double.parseDouble(input);

      if (chance < 0 || chance > 1.0) {
        player.sendMessage(CC.translate("&cThe chance must be between 0 and 1.0."));
        return this;
      }

      reward.setChance(chance);

      player.sendMessage(CC.translate("&aThe chance of this reward is now &6" + chance + "&a%."));

      new RewardEditMenu(reward, crate).openMenu(player);
    } catch (NumberFormatException e) {
      player.sendMessage(CC.translate("&cThe chance must be a number between 0 and 100."));
      return this;
    }


    return END_OF_CONVERSATION;
  }
}