package rip.battle.crates.crate.menus;

import cc.stormworth.core.kt.util.ItemBuilder;
import cc.stormworth.core.menu.Button;
import cc.stormworth.core.menu.Menu;
import cc.stormworth.core.util.chat.CC;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.List;
import java.util.Map;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import rip.battle.crates.crate.Crate;
import rip.battle.crates.crate.prompt.CrateKeyItemPrompt;
import rip.battle.crates.crate.prompt.CrateRenamePrompt;
import rip.battle.crates.utils.ChatUtils;

public class CrateEditMenu extends Menu {

  private final Crate crate;

  public CrateEditMenu(Crate crate) {
    this.crate = crate;

    setUpdateAfterClick(true);
  }

  @Override
  public String getTitle(Player player) {
    String name = "&eEditing &a" + crate.getDisplayName() + "&e...";

    if(name.length() > 32) {
      name = name.substring(0, 32);
    }

  return name;
  }

  @Override
  public Map<Integer, Button> getButtons(Player player) {
    Map<Integer, Button> buttons = Maps.newHashMap();


    buttons.put(getSlot(2, 1), Button.fromItem(
        new ItemBuilder(Material.NAME_TAG).name("&6Change display name")
            .addToLore(
                "",
                "&7Click to change displayname."
            ).build(),
        (other) -> ChatUtils.beginPrompt(other, new CrateRenamePrompt(crate))));

    buttons.put(getSlot(4, 1), Button.fromItem(
        new ItemBuilder(Material.CHEST)
            .name("&6Edit rewards")
            .addToLore(
                "",
                "&7Click to edit the rewards for this crate."
            ).build(),
        crate::openEditRewardsInventory));

    buttons.put(getSlot(6, 1), Button.fromItem(
        new ItemBuilder(crate.getKey().getType())
            .data(crate.getKey().getDurability())
            .name("&6Change key item")
            .addToLore(
                "",
                "&7Click to change the key item for this crate."
            ).build(),
        (other) -> ChatUtils.beginPrompt(other, new CrateKeyItemPrompt(crate))));


    buttons.put(getSlot(4, 2), Button.fromItem(
        new ItemBuilder(Material.JUKEBOX).name("&6Select open sound")
            .addToLore(
                "",
                "&7Click to select the sound that plays when this crate is opened."
            )
            .build(),
            (other) -> new SelectSoundMenu(crate).openMenu(player)));

    buttons.put(getSlot(2, 3), new MinAmountButton());

    buttons.put(getSlot(4, 3), Button.fromItem(
        new ItemBuilder(crate.isEnable() ? Material.REDSTONE_TORCH_ON : Material.LEVER)
            .name(crate.isEnable() ? "&cDisable crate" : "&aEnable crate")
            .addToLore(
                "",
                "&7Click to " + (crate.isEnable() ? "disable" : "enable") + " this crate."
            ).build(),
        (other) -> crate.setEnable(!crate.isEnable())
    ));

    buttons.put(getSlot(6, 3), new MaxRewardsButton());


    buttons.put(getSlot(5, 5), Button.fromItem(
        new ItemBuilder(Material.FLINT_AND_STEEL).name("&4&lDelete crate")
            .addToLore(
                "",
                "&4&lClick to delete this crate."
            ).build(), (other) -> new CrateDeleteMenu(crate).openMenu(other)));

    buttons.put(getSlot(3, 5), Button.fromItem(
        new ItemBuilder(Material.ENDER_CHEST).name("&6Preview rewards")
            .addToLore(
                "",
                "&7Click to open preview menu of rewards for this crate."
            )
            .build(), (other) -> new CratePreviewMenu(crate).openMenu(other)));

    return buttons;
  }

  public class MinAmountButton extends Button{

    @Override
    public String getName(Player player) {
      return "&6Select Min rewards amount";
    }

    @Override
    public List<String> getDescription(Player player) {
      return Lists.newArrayList(
          "&7Minimum amount of rewards to be given: &f" + crate.getMinimumReward(),
          "",
          "&fRight click &7to decrease",
          "&fLeft click &7to increase",
          ""
      );
    }

    @Override
    public Material getMaterial(Player player) {
      return Material.WOOD_BUTTON;
    }

    @Override
    public void clicked(Player player, int slot, ClickType clickType) {
      if(clickType == ClickType.RIGHT) {
        if(crate.getMinimumReward() == 1) {
          player.sendMessage(CC.translate("&cCrates with a minimum reward of 1 are not allowed."));
          Button.playFail(player);
          return;
        }
        crate.setMinimumReward(crate.getMinimumReward() - 1);
      } else if(clickType == ClickType.LEFT) {
        crate.setMinimumReward(crate.getMinimumReward() + 1);
      }
    }
  }

  public class MaxRewardsButton extends Button{

    @Override
    public String getName(Player player) {
      return "&6Select Max rewards amount";
    }

    @Override
    public List<String> getDescription(Player player) {
      return Lists.newArrayList(
          "&7Maximum amount of rewards to be given: &f" + crate.getMaximumReward(),
          "",
          "&fRight click &7to decrease",
          "&fLeft click &7to increase",
          ""
      );
    }

    @Override
    public Material getMaterial(Player player) {
      return Material.STONE_BUTTON;
    }

    @Override
    public void clicked(Player player, int slot, ClickType clickType) {
      if(clickType == ClickType.RIGHT) {
        if(crate.getMaximumReward() <= crate.getMinimumReward()) {
          player.sendMessage(CC.translate("&cMaximum reward must be greater than or equal to minimum reward."));
          Button.playFail(player);
          return;
        }
        crate.setMaximumReward(crate.getMaximumReward() - 1);
      } else if(clickType == ClickType.LEFT) {
        crate.setMaximumReward(crate.getMaximumReward() + 1);
      }
    }
  }
}