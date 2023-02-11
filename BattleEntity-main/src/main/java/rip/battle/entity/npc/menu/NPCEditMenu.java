package rip.battle.entity.npc.menu;

import cc.stormworth.core.util.chat.CC;
import cc.stormworth.core.util.chat.ChatUtils;
import cc.stormworth.core.util.inventory.ItemBuilder;
import cc.stormworth.core.util.menu.Menu;
import cc.stormworth.core.util.menu.button.Button;
import com.google.common.collect.Maps;
import lombok.RequiredArgsConstructor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import rip.battle.entity.EntityPlugin;
import rip.battle.entity.hologram.menu.HologramEditMenu;
import rip.battle.entity.npc.api.NPC;
import rip.battle.entity.npc.prompt.NPCChangeDisplayNamePrompt;

import java.util.Map;

@RequiredArgsConstructor
public class NPCEditMenu extends Menu {

    private final NPC npc;
    private final EntityPlugin plugin;

    @Override
    public String getTitle(Player player) {
        return "&eEditing NPC &6" + npc.getName();
    }

    @Override
    public int getRows(Player player) {
        return 5;
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {

        Map<Integer, Button> buttons = Maps.newHashMap();

        buttons.put(getSlot(1, 2), new Button() {
            @Override
            public ItemStack getDisplayItem(Player player) {

                ItemBuilder builder = new ItemBuilder(Material.NAME_TAG);

                builder.setName("&6Change Display Name");

                builder.addLore(
                        "",
                        "&7Current: &e" + npc.getDisplayName(),
                        "",
                        "&7Click to change the display name"
                );

                return builder.toItemStack();
            }

            @Override
            public void onClick(Player player) {
                ChatUtils.beginPrompt(player, new NPCChangeDisplayNamePrompt(plugin, npc), plugin);
            }
        });

        buttons.put(getSlot(1, 4), new Button() {
            @Override
            public ItemStack getDisplayItem(Player player) {

                ItemBuilder builder = new ItemBuilder(Material.SKULL_ITEM);

                builder.setDurability((short) 3);

                builder.setName("&6Change Skin");

                builder.addLore(
                        "",
                        "&7Click to change the skin"
                );

                if (npc.getSkin() != null){
                    builder.setTexture(npc.getSkin().getValue());
                }

                return builder.toItemStack();
            }

            @Override
            public void onClick(Player player) {
                new NPCSkinMenu(npc, plugin).open(player);
            }
        });

        buttons.put(getSlot(1, 6), new Button() {
            @Override
            public ItemStack getDisplayItem(Player player) {

                ItemBuilder builder = new ItemBuilder(Material.ENDER_PEARL);

                builder.setName("&6Teleport");

                builder.addLore(
                        "",
                        "&7Click to teleport to the NPC"
                );

                return builder.toItemStack();
            }

            @Override
            public void onClick(Player player) {
                player.closeInventory();
                player.teleport(npc.getLocation());
            }
        });

        buttons.put(getSlot(2, 2), new Button() {
            @Override
            public ItemStack getDisplayItem(Player player) {

                ItemBuilder builder = new ItemBuilder(Material.BARRIER);

                builder.setName("&6Delete");

                builder.addLore(
                        "",
                        "&7Click to delete the NPC"
                );

                return builder.toItemStack();
            }

            @Override
            public void onClick(Player player) {
                player.closeInventory();
                plugin.getNpcManager().removeNPC(npc);

                player.sendMessage(CC.translate("&aSuccessfully deleted NPC &6" + npc.getName()));
            }
        });

        buttons.put(getSlot(2, 4), new Button() {
            @Override
            public ItemStack getDisplayItem(Player player) {

                ItemBuilder builder = new ItemBuilder(Material.POTION);

                builder.setDurability((short) 8238);

                builder.setName("&6Change Visibility");

                builder.addLore(
                        "",
                        "&7Current: " + (npc.isVisible() ? "&aVisible" : "&cHidden"),
                        "",
                        "&7Click to change the visibility"
                );

                return builder.toItemStack();
            }

            @Override
            public void onClick(Player player) {
                npc.updateVisibility(npc.isVisible());
                player.sendMessage(CC.translate("&aSuccessfully " + (npc.isVisible() ? "showed" : "hidden") + " NPC &6" + npc.getName()));
            }
        });

        buttons.put(getSlot(2, 6), new Button() {
            @Override
            public ItemStack getDisplayItem(Player player) {

                ItemBuilder builder = new ItemBuilder(Material.DIAMOND_CHESTPLATE);

                builder.setName("&6Edit Armor");

                builder.addLore(
                        "",
                        "&7Click to edit the armor"
                );

                return builder.toItemStack();
            }

            @Override
            public void onClick(Player player) {
                new NPCEditArmorMenu(plugin, npc).open(player);
            }
        });


        if (npc.getHologram() != null){

            buttons.put(getSlot(3, 3), new Button() {
                @Override
                public ItemStack getDisplayItem(Player player) {

                    ItemBuilder builder = new ItemBuilder(npc.getItemInHand() == null ? Material.INK_SACK : npc.getItemInHand().getType());

                    if (npc.getItemInHand() != null) {
                        builder.setDurability(npc.getItemInHand().getDurability());
                    }else {
                        builder.setDurability((short) 8);
                    }

                    builder.setName("&6Edit Item In Hand");

                    builder.addLore(
                            "",
                            "&7Click with item to set the item in hand of npc"
                    );

                    return builder.toItemStack();
                }

                @Override
                public void onClick(InventoryClickEvent event) {
                    if (event.getCursor() != null && event.getCursor().getType() != Material.AIR) {
                        npc.setItemInHand(event.getCursor());
                        player.sendMessage(CC.translate("&aSuccessfully set item in hand of NPC &6" + npc.getName()));
                        event.setCursor(null);

                        player.playSound(player.getLocation(), Sound.CLICK, 1F, 1F);
                    }else {
                        player.sendMessage(CC.translate("&cYou must have an item in your cursor to set the item in hand of the NPC"));
                    }
                }
            });

            buttons.put(getSlot(3, 5), new Button() {
                @Override
                public ItemStack getDisplayItem(Player player) {

                    ItemBuilder builder = new ItemBuilder(Material.PAPER);

                    builder.setName("&6Edit Hologram");

                    builder.addLore(
                            "",
                            "&7Click to edit the hologram"
                    );

                    return builder.toItemStack();
                }

                @Override
                public void onClick(Player player) {
                    new HologramEditMenu(plugin, npc.getHologram(), NPCEditMenu.this).open(player);
                }
            });
        }else{
            buttons.put(getSlot(3, 4), new Button() {
                @Override
                public ItemStack getDisplayItem(Player player) {

                    ItemBuilder builder = new ItemBuilder(npc.getItemInHand() == null ? Material.INK_SACK : npc.getItemInHand().getType());

                    if (npc.getItemInHand() != null) {
                        builder.setDurability(npc.getItemInHand().getDurability());
                    }else {
                        builder.setDurability((short) 8);
                    }

                    builder.setName("&6Edit Item In Hand");

                    builder.addLore(
                            "",
                            "&7Click with item to set the item in hand of npc"
                    );

                    return builder.toItemStack();
                }

                @Override
                public void onClick(InventoryClickEvent event) {
                    if (event.getCursor() != null && event.getCursor().getType() != Material.AIR) {
                        npc.setItemInHand(event.getCursor());
                        player.sendMessage(CC.translate("&aSuccessfully set item in hand of NPC &6" + npc.getName()));
                        event.setCursor(null);

                        player.playSound(player.getLocation(), Sound.CLICK, 1F, 1F);
                    }else {
                        player.sendMessage(CC.translate("&cYou must have an item in your cursor to set the item in hand of the NPC"));
                    }
                }
            });
        }

        return buttons;
    }
}
