package rip.battle.crates.reward.menus;

import cc.stormworth.core.kt.util.ItemBuilder;
import cc.stormworth.core.menu.Button;
import cc.stormworth.core.menu.Menu;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import rip.battle.crates.crate.Crate;
import rip.battle.crates.reward.Reward;
import rip.battle.crates.reward.RewardType;
import rip.battle.crates.reward.promt.RewardAddCommandPrompt;
import rip.battle.crates.utils.ChatUtils;

public class RewardCommandsMenu extends Menu {

  private final Reward reward;
  private final Crate crate;

  public RewardCommandsMenu(Reward reward, Crate crate) {
    this.reward = reward;
    this.crate = crate;

    setUpdateAfterClick(true);
  }

  @Override
  public String getTitle(Player player) {
    return ChatColor.GREEN + "Manage commands";
  }

  @Override
  public Map<Integer, Button> getButtons(Player player) {

    Map<Integer, Button> buttons = Maps.newHashMap();


    buttons.put(getSlot(0,  0), Button.fromItem(
        new ItemBuilder(Material.BED)
            .name("&cGo Back")
            .addToLore(
                "",
                "&7Click to go back to the reward edit menu."
            ).build(),
        (other) -> {
          if(reward.getCommands().isEmpty()) {
            reward.setType(RewardType.ITEMS);
          }

          new RewardEditMenu(reward, crate).openMenu(other);
        }));

    if(reward.getCommands().isEmpty()) {
      buttons.put(getSlot(4,  0), Button.fromItem(
          new ItemBuilder(Material.PAPER)
              .name("&bAdd Command")
              .addToLore(
                  "",
                  "&7Click add command to reward."
              ).build(),
          (other) -> ChatUtils.beginPrompt(player, new RewardAddCommandPrompt(reward, crate, new RewardCommandsMenu(reward, crate)))));
    }

    int slot = 9;

    for (String command : reward.getCommands()) {
      buttons.put(slot, new CommandButton(command));
    }

    return buttons;
  }

  @RequiredArgsConstructor
  public class CommandButton extends Button{

    private final String command;

    @Override
    public String getName(Player player) {
      return ChatColor.GOLD + command;
    }

    @Override
    public List<String> getDescription(Player player) {
      return Lists.newArrayList(
          "",
          "&fLeft Click to edit command",
          "&fRight Click to remove command"
      );
    }

    @Override
    public Material getMaterial(Player player) {
      return Material.PAPER;
    }

    @Override
    public void clicked(Player player, int slot, ClickType clickType) {
      reward.getCommands().remove(command);
      if (clickType == ClickType.LEFT) {
        ChatUtils.beginPrompt(player, new RewardAddCommandPrompt(reward, crate, new RewardCommandsMenu(reward, crate)));
      } else if (clickType == ClickType.RIGHT) {
        Button.playNeutral(player);
      }
    }
  }
}