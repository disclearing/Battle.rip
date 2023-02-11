package rip.battle.crates.crate.menus;

import cc.stormworth.core.kt.util.ItemBuilder;
import cc.stormworth.core.menu.Button;
import cc.stormworth.core.menu.Menu;
import cc.stormworth.core.menu.pagination.PaginatedMenu;
import cc.stormworth.core.util.chat.CC;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import rip.battle.crates.crate.Crate;

@RequiredArgsConstructor
public class SelectSoundMenu extends PaginatedMenu {

  private final Crate crate;

  @Override
  public String getTitle(Player player) {
    return "&6Select sound when open";
  }

  @Override
  public String getPrePaginatedTitle(Player player) {
    return "&6Select sound when open &f" + crate.getName();
  }

  @Override
  public int getMaxItemsPerPage(Player player) {
    return 27;
  }

  @Override
  public Map<Integer, Button> getGlobalButtons(Player player) {
    Map<Integer, Button> buttons = Maps.newHashMap();

    buttons.put(4, Button.fromItem(new ItemBuilder(Material.BED).name("&cGo Back").build(),
        (other) -> new CrateEditMenu(crate).openMenu(other)));

    return buttons;
  }

  @Override
  public Map<Integer, Button> getAllPagesButtons(Player player) {
    Map<Integer, Button> buttons = Maps.newHashMap();

    for (Sound sound : Sound.values()) {
      buttons.put(buttons.size(), new SoundButton(sound));
    }

    return buttons;
  }

  @RequiredArgsConstructor
  public class SoundButton extends Button{
    private final Sound sound;

    @Override
    public String getName(Player player) {
      return "&6" + sound.name();
    }

    @Override
    public List<String> getDescription(Player player) {
      return Lists.newArrayList(
          "&fLeft click &6to Set sound",
          "&fRight click &7to Play sound"
      );
    }

    @Override
    public Material getMaterial(Player player) {
      return Material.JUKEBOX;
    }

    @Override
    public void clicked(Player player, int slot, ClickType clickType) {
      if(clickType == ClickType.LEFT){
        player.playSound(player.getLocation(), sound, 1, 1);
        crate.setOpenSound(sound);

        player.sendMessage(CC.translate("&6Set sound to &f" + sound.name()));

        new CrateEditMenu(crate).openMenu(player);
      }else{
        player.playSound(player.getLocation(), sound, 1, 1);
      }
    }
  }
}