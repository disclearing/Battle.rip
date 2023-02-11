package rip.battle.crates.crate.menus;

import cc.stormworth.core.kt.util.ItemBuilder;
import cc.stormworth.core.menu.Button;
import cc.stormworth.core.menu.Menu;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import rip.battle.crates.crate.Crate;
import rip.battle.crates.crate.prompt.CrateBroadCastPrompt;
import rip.battle.crates.crate.prompt.CrateEditLinePrompt;
import rip.battle.crates.reward.Reward;
import rip.battle.crates.reward.menus.RewardEditMenu;
import rip.battle.crates.utils.ChatUtils;

public class CrateBroadcastMenu extends Menu {

  private final Crate crate;
  private final Reward reward;

  public CrateBroadcastMenu(Crate crate, Reward reward) {
    this.crate = crate;
    this.reward = reward;

    setUpdateAfterClick(true);
  }

  @Override
  public String getTitle(Player player) {
    return "&eBroadcast Lines";
  }

  @Override
  public Map<Integer, Button> getButtons(Player player) {

    Map<Integer, Button> buttons = Maps.newHashMap();

    buttons.put(0, Button.fromItem(new ItemBuilder(Material.BED)
            .name("&cGo Back").build(),
    (other) -> new RewardEditMenu(reward, crate).openMenu(other)));

    buttons.put(4, Button.fromItem(new ItemBuilder(Material.BOOK)
            .name("&aAdd new lines").build(),
        (other) -> ChatUtils.beginPrompt(other, new CrateBroadCastPrompt(crate, reward))));

    buttons.put(8, Button.fromItem(new ItemBuilder(Material.JUKEBOX)
            .name("&eClick to preview broadcast").build(),
    (other) -> {

      player.closeInventory();

      LinkedList<String> lines = reward.getBroadcast();

      if (lines.isEmpty()) {
        other.sendMessage("&cNo broadcast lines set!");
      }else{
        lines.forEach(line -> other.sendMessage(line.replace("{player}", other.getName())));
      }
    }));

    int i = 9;
    int index = 0;
    for (String line : reward.getBroadcast()) {
      buttons.put(i++, new BroadcastLineButton(line, index++));
    }

    return buttons;
  }

  @RequiredArgsConstructor
  public class BroadcastLineButton extends Button{

    private final String line;
    private final int index;

    @Override
    public String getName(Player player) {
      return line;
    }

    @Override
    public List<String> getDescription(Player player) {
      return Lists.newArrayList(
          "&fRight &7click to Delete this line",
          "&fLeft &7click to Edit this line"
      );
    }

    @Override
    public Material getMaterial(Player player) {
      return Material.PAPER;
    }

    @Override
    public void clicked(Player player, int slot, ClickType clickType) {
      if (clickType == ClickType.RIGHT) {
        reward.getBroadcast().remove(line);
      } else if (clickType == ClickType.LEFT) {
        ChatUtils.beginPrompt(player, new CrateEditLinePrompt(crate, reward, index));
      }
    }
  }
}