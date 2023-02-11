package rip.battle.entity.npc.menu;

import cc.stormworth.core.util.chat.CC;
import cc.stormworth.core.util.inventory.ItemBuilder;
import cc.stormworth.core.util.menu.button.Button;
import cc.stormworth.core.util.menu.paginated.CenteredPaginatedMenu;
import cc.stormworth.core.util.skin.SkinTexture;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import rip.battle.entity.EntityPlugin;
import rip.battle.entity.npc.api.NPC;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
public class NPCSkinOnlinePlayersMenu extends CenteredPaginatedMenu {

    private final NPC npc;
    private final EntityPlugin plugin;

    @Override
    public String getTitle(Player player) {
        return "&eEditing skin for NPC &6" + npc.getName();
    }

    @Override
    public int getRows(Player player) {
        return 3;
    }

    @Override
    public int getStartSlot(Player player) {
        return 9;
    }

    @Override
    public int getPreviousPageSlot(Player player) {
        return 3;
    }

    @Override
    public int getNextPageSlot(Player player) {
        return 5;
    }

    @Override
    public List<Button> getAllPagesButtons(Player player) {

        List<Button> buttons = Lists.newArrayList();

        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            buttons.add(new OnlinePlayerButton(onlinePlayer));
        }

        return buttons;
    }

    @Override
    public Map<Integer, Button> getGlobalButtons(Player player) {
        Map<Integer, Button> buttons = Maps.newHashMap();

        buttons.put(0, new Button() {

            @Override
            public ItemStack getDisplayItem(Player player) {
                return new ItemBuilder(Material.BED).setName("&cBack").toItemStack();
            }

            @Override
            public void onClick(Player player) {
                new NPCSkinMenu(npc, plugin).open(player);
            }
        });

        return buttons;
    }

    @RequiredArgsConstructor
    public class OnlinePlayerButton extends Button{
        private final Player onlinePlayer;

        @Override
        public ItemStack getDisplayItem(Player player) {

            ItemBuilder builder = new ItemBuilder(Material.SKULL_ITEM);

            builder.setDurability((short) 3);

            builder.setName("&6" + onlinePlayer.getName());

            builder.addLore(
                    "",
                    "&7Click to set the skin to &6" + onlinePlayer.getName()
            );

            builder.setSkullOwner(onlinePlayer.getName());

            return builder.toItemStack();
        }

        @Override
        public void onClick(Player player) {
            npc.setSkin(SkinTexture.getPlayerTexture(onlinePlayer));
            player.sendMessage(CC.translate("&aSet the skin to &6" + onlinePlayer.getName()));
            player.playSound(player.getLocation(), Sound.CLICK, 1, 1);
        }
    }
}
