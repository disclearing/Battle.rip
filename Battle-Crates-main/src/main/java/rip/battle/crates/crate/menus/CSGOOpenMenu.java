package rip.battle.crates.crate.menus;

import cc.stormworth.core.kt.util.ItemBuilder;
import cc.stormworth.core.menu.Button;
import cc.stormworth.core.menu.Menu;
import com.google.common.collect.Maps;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import rip.battle.crates.crate.Crate;
import rip.battle.crates.reward.Reward;
import rip.battle.crates.utils.RandomUtils;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class CSGOOpenMenu extends Menu {

    private final Crate crate;
    private final Reward randomReward;
    private final List<Reward> rewards;
    int time = 0;
    private boolean claimed = false;

    @Override
    public String getTitle(Player player) {
        return crate.getDisplayName() + " Crate";
    }

    public CSGOOpenMenu(Crate crate) {
        this.crate = crate;
        this.rewards = crate.getRewards();

        randomReward = RandomUtils.getRandomReward(rewards);

        Collections.shuffle(rewards);
        setAutoUpdate(true);
        setUpdateAfterClick(false);
    }

    @Override
    public void onClose(Player player) {
        if (!claimed) {
            player.getInventory().addItem(randomReward.getItem());
            player.updateInventory();
            player.playSound(player.getLocation(), Sound.LEVEL_UP, 1.0F, 1.0F);
            claimed = true;
        }
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> buttons = Maps.newHashMap();

        Collections.rotate(rewards, 1);

        for (int i = 0; i < 9; i++) {
            buttons.put(buttons.size(), Button.fromItem(new ItemBuilder(Material.STAINED_GLASS_PANE).data((short) 0).build()));
        }

        for (int i = 0; i < 9; i++) {
            buttons.put(buttons.size(), Button.fromItem(rewards.get(i).getItem()));
        }

        for (int i = 18; i < 27; i++) {
            buttons.put(buttons.size(), Button.fromItem(new ItemBuilder(Material.STAINED_GLASS_PANE).data((short) 0).build()));
        }

        buttons.put(4, Button.fromItem(new ItemBuilder(Material.STAINED_GLASS_PANE).data((short) 5).build()));
        buttons.put(22, Button.fromItem(new ItemBuilder(Material.STAINED_GLASS_PANE).data((short) 5).build()));

        player.playSound(player.getLocation(), Sound.NOTE_STICKS, 1.0F, 1.0F);

        Button button = buttons.get(13);

        if (time == 25) {
            if (button.getButtonItem(player).isSimilar(randomReward.getItem())) {
                setAutoUpdate(false);

                player.getInventory().addItem(randomReward.getItem());
                player.updateInventory();
                player.playSound(player.getLocation(), Sound.LEVEL_UP, 1.0F, 1.0F);
                claimed = true;
            }
        } else {
            time++;
        }

        return buttons;
    }
}
