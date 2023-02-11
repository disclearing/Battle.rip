package rip.battle.entity.hologram.menu;

import cc.stormworth.core.util.chat.CC;
import cc.stormworth.core.util.chat.ChatUtils;
import cc.stormworth.core.util.inventory.ItemBuilder;
import cc.stormworth.core.util.menu.Menu;
import cc.stormworth.core.util.menu.button.Button;
import com.google.common.collect.Maps;
import lombok.RequiredArgsConstructor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import rip.battle.entity.EntityPlugin;
import rip.battle.entity.hologram.CraftHologram;
import rip.battle.entity.hologram.api.Hologram;
import rip.battle.entity.hologram.lines.HologramLine;
import rip.battle.entity.hologram.prompt.HologramAddItemLinePrompt;
import rip.battle.entity.hologram.prompt.HologramAddLinePrompt;
import rip.battle.entity.hologram.prompt.HologramEditLinePrompt;
import rip.battle.entity.npc.api.NPC;
import rip.battle.entity.npc.menu.NPCEditMenu;

import java.util.Map;

@RequiredArgsConstructor
public class HologramManageLinesMenu extends Menu {

    private final EntityPlugin plugin;
    private final Hologram hologram;
    @Override
    public String getTitle(Player player) {
        return "&bEditing lines of &8" + hologram.getName();
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
                new HologramEditMenu(plugin, hologram, null).open(player);
            }
        });

        buttons.put(getSlot(0, 3), new Button() {
            @Override
            public ItemStack getDisplayItem(Player player) {

                ItemBuilder builder = new ItemBuilder(Material.BOOK);

                builder.setName("&6Add line");

                builder.addLore(
                        "",
                        "&7Click to add new line to this hologram."
                );

                return builder.toItemStack();
            }

            @Override
            public void onClick(Player player) {
                ChatUtils.beginPrompt(player, new HologramAddLinePrompt(plugin, hologram), plugin);
            }
        });

        buttons.put(getSlot(0, 5), new Button() {
            @Override
            public ItemStack getDisplayItem(Player player) {

                ItemBuilder builder = new ItemBuilder(Material.DIAMOND_PICKAXE);

                builder.setName("&6Add Item line");

                builder.addLore(
                        "",
                        "&7Click to add new item line to this hologram."
                );

                return builder.toItemStack();
            }

            @Override
            public void onClick(Player player) {
                ChatUtils.beginPrompt(player, new HologramAddItemLinePrompt(plugin, hologram), plugin);
            }
        });

        int startSlot = 9;

        for (HologramLine<?> line : hologram.getLines()) {
            buttons.put(startSlot++, new Button() {
                @Override
                public ItemStack getDisplayItem(Player player) {
                    ItemBuilder builder = new ItemBuilder(Material.PAPER);

                    builder.setName("&6" + line.getLine(player));

                    builder.addLore(
                            "",
                            "&7Click to edit this line.",
                            "&cShift click to remove this line."
                    );

                    return builder.toItemStack();
                }

                @Override
                public void onClick(Player player, ClickType clickType) {
                    if (clickType.isShiftClick()) {
                        hologram.removeLine(line);
                        player.sendMessage(CC.translate("&aRemoved line &8" + line.getLine(player) + " &afrom hologram &8" + hologram.getName() + "&a."));

                        if (hologram.getLines().isEmpty()){

                            CraftHologram craftHologram = (CraftHologram) hologram;

                            if (craftHologram.getParent() != null){
                                NPC npc = (NPC) craftHologram.getParent();
                                npc.setHologram(null);
                                hologram.destroy();
                                new NPCEditMenu(npc, plugin).open(player);
                            }
                        }

                    } else {
                        ChatUtils.beginPrompt(player, new HologramEditLinePrompt(plugin, hologram, line), plugin);
                    }
                }
            });
        }

        return buttons;
    }
}
