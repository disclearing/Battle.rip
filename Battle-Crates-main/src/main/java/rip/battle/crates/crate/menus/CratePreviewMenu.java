package rip.battle.crates.crate.menus;

import cc.stormworth.core.menu.Button;
import cc.stormworth.core.menu.Menu;
import com.google.common.collect.Maps;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.Player;
import rip.battle.crates.crate.Crate;
import rip.battle.crates.crate.CratePlaceholder;
import rip.battle.crates.misterybox.MysteryBox;

@RequiredArgsConstructor
public class CratePreviewMenu extends Menu {

  private final Crate crate;

  @Override
  public String getTitle(Player player) {
    String name = crate.getName();

    if(name.length() > 32) {
      name = name.substring(0, 32);
    }

    return ChatColor.GOLD + "Preview " + name;
  }

  @Override
  public Map<Integer, Button> getButtons(Player player) {
    Map<Integer, Button> buttons = Maps.newHashMap();

    for (CratePlaceholder placeholder : crate.getPlaceholders()) {
      if (placeholder.getItem() != null) {
        buttons.put(placeholder.getSlot(), Button.fromItem(placeholder.getPreview()));
      }
    }

    crate.getRewards().forEach(reward -> buttons.put(reward.getSlot(), Button.fromItem(reward.getItem())));

    if(crate instanceof MysteryBox) {
      ((MysteryBox) crate).getObligatoryRewards().forEach(reward -> buttons.put(reward.getSlot(), Button.fromItem(reward.getItem())));
    }

    return buttons;
  }
}