package rip.battle.entity.npc.menu;

import cc.stormworth.core.util.chat.ChatUtils;
import cc.stormworth.core.util.inventory.ItemBuilder;
import cc.stormworth.core.util.menu.Menu;
import cc.stormworth.core.util.menu.button.Button;
import cc.stormworth.core.util.menu.button.impl.PlaceholderButton;
import com.google.common.collect.Maps;
import lombok.RequiredArgsConstructor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import rip.battle.entity.EntityPlugin;
import rip.battle.entity.npc.api.NPC;
import rip.battle.entity.npc.prompt.NPCChangeSkinPrompt;

import java.util.Map;

@RequiredArgsConstructor
public class NPCSkinMenu extends Menu {

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
    public Map<Integer, Button> getButtons(Player player) {

        Map<Integer, Button> buttons = Maps.newHashMap();

        buttons.put(0, new Button() {

            @Override
            public ItemStack getDisplayItem(Player player) {
                return new ItemBuilder(Material.BED).setName("&cBack").toItemStack();
            }

            @Override
            public void onClick(Player player) {
                new NPCEditMenu(npc, plugin).open(player);
            }
        });

        buttons.put(getSlot(1, 2), new Button() {

            @Override
            public ItemStack getDisplayItem(Player player) {
                ItemBuilder builder = new ItemBuilder(Material.PAPER);

                builder.setName("&6Set custom skin");

                builder.addLore(
                        "",
                        "&7Click to set custom skin with a username"
                );

                return builder.toItemStack();
            }

            @Override
            public void onClick(Player player) {
                ChatUtils.beginPrompt(player, new NPCChangeSkinPrompt(plugin, npc), plugin);
            }
        });

        buttons.put(getSlot(1, 4), new PlaceholderButton(new ItemBuilder(Material.SKULL_ITEM)
                .setName("&6Current Skin")
                .setDurability((short) 3)
                .setTexture(npc.getSkin() == null ? null : npc.getSkin().getValue())
                .toItemStack()));

        buttons.put(getSlot(1, 6), new Button() {
            @Override
            public ItemStack getDisplayItem(Player player) {

                ItemBuilder builder = new ItemBuilder(Material.BOOK);

                builder.setName("&eSet skin of current online players");

                builder.addLore(
                        "",
                        "&7Click to open menu and select a player",
                        "&7to set the skin of the NPC"
                );

                return builder.toItemStack();
            }

            @Override
            public void onClick(Player player) {
                new NPCSkinOnlinePlayersMenu(npc, plugin).open(player);
            }
        });

        buttons.put(getSlot(2, 4), new Button() {

            @Override
            public ItemStack getDisplayItem(Player player) {
                ItemBuilder builder = new ItemBuilder(Material.BARRIER);

                builder.setName("&cRemove skin");

                builder.addLore(
                        "",
                        "&7Click to remove the skin"
                );

                return builder.toItemStack();
            }

            @Override
            public void onClick(Player player) {
                npc.setSkin(null);
                player.playSound(player.getLocation(), Sound.CLICK, 1, 1);
            }
        });
        return buttons;
    }
}
