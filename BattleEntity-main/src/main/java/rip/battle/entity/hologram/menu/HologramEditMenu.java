package rip.battle.entity.hologram.menu;

import cc.stormworth.core.util.inventory.ItemBuilder;
import cc.stormworth.core.util.menu.Menu;
import cc.stormworth.core.util.menu.button.Button;
import cc.stormworth.core.util.menu.button.impl.PlaceholderButton;
import cc.stormworth.core.util.menu.impl.ConfirmationMenu;
import com.google.common.collect.Maps;
import lombok.RequiredArgsConstructor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import rip.battle.entity.EntityPlugin;
import rip.battle.entity.hologram.CraftHologram;
import rip.battle.entity.hologram.HologramManager;
import rip.battle.entity.hologram.api.Hologram;
import rip.battle.entity.npc.api.NPC;
import rip.battle.entity.npc.menu.NPCEditMenu;

import java.util.Map;
public class HologramEditMenu extends Menu {
    private final Hologram hologram;
    private final HologramManager hologramManager;
    private final EntityPlugin plugin;

    private final Menu back;

    public HologramEditMenu(EntityPlugin plugin, Hologram hologram, Menu back) {
        this.plugin = plugin;
        this.hologramManager = plugin.getHologramManager();
        this.hologram = hologram;
        this.back = back;
    }

    @Override
    public String getTitle(Player player) {
        return "&bEditing Hologram &8" + hologram.getName();
    }

    @Override
    public int getRows(Player player) {
        return 3;
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {

        Map<Integer, Button> buttons = Maps.newHashMap();


        if (back != null){
            buttons.put(0, new Button() {

                @Override
                public ItemStack getDisplayItem(Player player) {
                    return new ItemBuilder(Material.BED).setName("&cBack").toItemStack();
                }

                @Override
                public void onClick(Player player) {
                    back.open(player);
                }
            });
        }

        buttons.put(getSlot(1, 1), new Button() {
            @Override
            public ItemStack getDisplayItem(Player player) {
                ItemBuilder builder = new ItemBuilder(Material.ENDER_PEARL);

                builder.setName("&6Teleport");

                builder.addLore(
                        "",
                        "&7Click to teleport to this hologram."
                );

                return builder.toItemStack();
            }

            @Override
            public void onClick(Player player) {
                player.closeInventory();
                player.teleport(hologram.getLocation());
            }
        });

        buttons.put(getSlot(1, 4), new Button() {
            @Override
            public ItemStack getDisplayItem(Player player) {
                ItemBuilder builder = new ItemBuilder(Material.BOOK);

                builder.setName("&6Lines: " + hologram.getLines().size());

                builder.addLore(
                        "",
                        "&7Click to manage lines on this hologram."
                );

                return builder.toItemStack();
            }

            @Override
            public void onClick(Player player) {
                new HologramManageLinesMenu(plugin, hologram).open(player);
            }
        });


        buttons.put(getSlot(1, 7), new Button() {
            @Override
            public ItemStack getDisplayItem(Player player) {
                ItemBuilder builder = new ItemBuilder(Material.WOOL);

                builder.setWoolColour(DyeColor.RED);

                builder.setName("&4&lDELETE");

                builder.addLore(
                        "",
                        "&7Click to delete this hologram."
                );

                return builder.toItemStack();
            }

            @Override
            public void onClick(Player player) {
                new ConfirmationMenu("&4&lAre you sure?", (value) -> {

                    if (value){
                        CraftHologram craftHologram = (CraftHologram) hologram;

                        if (craftHologram.getParent() != null){
                            NPC npc = (NPC) craftHologram.getParent();
                            npc.setHologram(null);
                            hologram.destroy();
                            new NPCEditMenu(npc, plugin).open(player);
                        }else{
                            hologramManager.removeHologram(hologram.getName());
                            player.closeInventory();
                        }

                    }else {
                        new HologramEditMenu(plugin, hologram, null).open(player);
                    }

                }, new PlaceholderButton(new ItemBuilder(Material.WOOL)
                        .setWoolColour(DyeColor.RED)
                        .setName("&4&lDeleting &c" + hologram.getName())
                        .toItemStack()))
                        .open(player);
            }
        });

        return buttons;
    }
}
