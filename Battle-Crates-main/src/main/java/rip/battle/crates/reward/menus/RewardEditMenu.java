package rip.battle.crates.reward.menus;

import cc.stormworth.core.kt.util.ItemBuilder;
import cc.stormworth.core.menu.Button;
import cc.stormworth.core.menu.Menu;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import rip.battle.crates.Crates;
import rip.battle.crates.crate.Crate;
import rip.battle.crates.crate.menus.CrateBroadcastMenu;
import rip.battle.crates.misterybox.MysteryBox;
import rip.battle.crates.reward.Reward;
import rip.battle.crates.reward.promt.RewardAddCommandPrompt;
import rip.battle.crates.reward.promt.RewardSetChancePrompt;
import rip.battle.crates.utils.ChatUtils;

@RequiredArgsConstructor
public class RewardEditMenu extends Menu {

  private final Reward reward;
  private final Crate crate;

  @Override
  public String getTitle(Player player) {
    return "&eEditing reward...";
  }

  @Override
  public int size(Map<Integer, Button> buttons) {
    return 3 * 9;
  }

  @Override
  public void onClose(Player player) {
    if (player.hasMetadata("closeByRightClick")) {
      player.removeMetadata("closeByRightClick", Crates.getInstance());
    }
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
          other.closeInventory();
          crate.openEditRewardsInventory(other);
        }));

    buttons.put(getSlot(2,  1), Button.fromItem(
        new ItemBuilder(Material.EXP_BOTTLE)
            .name("&bSet Chance")
            .addToLore(
                "",
                "&7Click to set the chance of this reward",
                "",
                "&7Current chance: &a" + reward.getChance() + "%"
            ).build(),
        (other) -> ChatUtils.beginPrompt(other, new RewardSetChancePrompt(reward, crate))
    ));

    if(crate instanceof MysteryBox){
      MysteryBox box = (MysteryBox) crate;
      buttons.put(getSlot(4, 1), Button.fromItem(
          new ItemBuilder(Material.ANVIL)
              .name(box.getObligatoryRewards().contains(reward) ? "&aObligatory" : "&cObligatory")
              .addToLore(
                  "",
                  (box.getObligatoryRewards().contains(reward) ? "&cClick to unset" : "&aClick to set") + " &7as an obligatory reward"
              ).build(),
          (other) -> {
            if(box.getObligatoryRewards().contains(reward)){
              box.getObligatoryRewards().remove(reward);
              box.getRewards().add(reward);
            }else{
              box.getObligatoryRewards().add(reward);
              box.getRewards().remove(reward);
            }

            Button.playNeutral(other);
          }
      ));
    }

    buttons.put(getSlot(6, 1), new CommandButtons());

    buttons.put(getSlot(4, 2),
        Button.fromItem(new ItemBuilder(Material.PAPER)
                .name("&6Broadcast Lines")
                .addToLore("",
                    "&7Click to view broadcast lines").build(),
            (other) -> new CrateBroadcastMenu(crate, reward).openMenu(other)));

    return buttons;
  }

  public class CommandButtons extends Button{

    @Override
    public String getName(Player player) {
      return "&bCommands";
    }

    @Override
    public List<String> getDescription(Player player) {
      return Lists.newArrayList(
          "",
          "&fLeft Click to add a command to this reward",
          "&fRight Click to manage commands",
          "",
          "&7Current Commands: &a" + (reward.getCommands() == null
              || reward.getCommands().isEmpty() ? "None" : Arrays.toString(reward.getCommands().toArray()))
      );
    }

    @Override
    public Material getMaterial(Player player) {
      return Material.BOOK;
    }

    @Override
    public void clicked(Player player, int slot, ClickType clickType) {
      if(clickType == ClickType.LEFT){
        ChatUtils.beginPrompt(player, new RewardAddCommandPrompt(reward, crate, new RewardEditMenu(reward, crate)));
      }else{
        new RewardCommandsMenu(reward, crate).openMenu(player);
      }
    }
  }
}