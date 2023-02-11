package rip.battle.crates.crate.menus;

import cc.stormworth.core.kt.util.ItemBuilder;
import cc.stormworth.core.menu.Button;
import cc.stormworth.core.menu.Menu;
import cc.stormworth.core.util.chat.CC;
import com.google.common.collect.Maps;
import lombok.RequiredArgsConstructor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import rip.battle.crates.crate.Crate;

import java.util.Map;

@RequiredArgsConstructor
public class CrateDeleteMenu extends Menu {

    private final Crate crate;

    @Override
    public String getTitle(Player player) {
        return "&cDelete Crate or chest";
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> buttons = Maps.newHashMap();

        buttons.put(0, Button.fromItem(new ItemBuilder(Material.BED)
                        .name("&cGo Back").build(),
                (other) -> new CrateEditMenu(crate).openMenu(other)));

        buttons.put(getSlot(3, 1), Button.fromItem(new ItemBuilder(Material.FLINT_AND_STEEL).name("&cDelete Crate").build(),
                (other) -> {
                    other.closeInventory();
                    crate.delete();
                }));

        buttons.put(getSlot(5, 1),
                Button.fromItem(new ItemBuilder(Material.CHEST)
                        .name("&cDelete Chest").build(), (other) -> {


                    crate.removeHologram();
                    crate.setChestLocation(null);
                    player.sendMessage(CC.translate("&aSuccessfully removed chest location."));
                    other.closeInventory();
                }));

        return buttons;
    }
}