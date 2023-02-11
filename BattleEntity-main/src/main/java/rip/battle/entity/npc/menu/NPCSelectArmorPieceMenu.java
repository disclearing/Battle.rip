package rip.battle.entity.npc.menu;

import cc.stormworth.core.util.inventory.ItemBuilder;
import cc.stormworth.core.util.menu.Menu;
import cc.stormworth.core.util.menu.button.Button;
import com.google.common.collect.Maps;
import lombok.RequiredArgsConstructor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import rip.battle.entity.EntityPlugin;
import rip.battle.entity.npc.api.NPC;
import rip.battle.entity.utils.ArmorPiece;

import java.util.Map;

@RequiredArgsConstructor
public class NPCSelectArmorPieceMenu extends Menu {

    private final EntityPlugin plugin;
    private final NPC npc;
    private final ArmorPiece armorPiece;

    @Override
    public String getTitle(Player player) {
        return "&eSelect a " + armorPiece.name() + " for NPC &6" + npc.getName();
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
                new NPCEditArmorMenu(plugin, npc).open(player);
            }
        });


        if (armorPiece == ArmorPiece.HELMET) {
            buttons.put(getSlot(1, 2), new ArmorPieceButton(getPiece("leather")));
            buttons.put(getSlot(1, 3), new ArmorPieceButton(getPiece("chainmail")));
            buttons.put(getSlot(1, 4), new ArmorPieceButton(getPiece("iron")));
            buttons.put(getSlot(1, 5), new ArmorPieceButton(getPiece("gold")));
            buttons.put(getSlot(1, 6), new ArmorPieceButton(getPiece("diamond")));
        } else if (armorPiece == ArmorPiece.CHESTPLATE) {
            buttons.put(getSlot(1, 2), new ArmorPieceButton(getPiece("leather")));
            buttons.put(getSlot(1, 3), new ArmorPieceButton(getPiece("chainmail")));
            buttons.put(getSlot(1, 4), new ArmorPieceButton(getPiece("iron")));
            buttons.put(getSlot(1, 5), new ArmorPieceButton(getPiece("gold")));
            buttons.put(getSlot(1, 6), new ArmorPieceButton(getPiece("diamond")));
        } else if (armorPiece == ArmorPiece.LEGGINGS) {
            buttons.put(getSlot(1, 2), new ArmorPieceButton(getPiece("leather")));
            buttons.put(getSlot(1, 3), new ArmorPieceButton(getPiece("chainmail")));
            buttons.put(getSlot(1, 4), new ArmorPieceButton(getPiece("iron")));
            buttons.put(getSlot(1, 5), new ArmorPieceButton(getPiece("gold")));
            buttons.put(getSlot(1, 6), new ArmorPieceButton(getPiece("diamond")));
        } else if (armorPiece == ArmorPiece.BOOTS) {
            buttons.put(getSlot(1, 2), new ArmorPieceButton(getPiece("leather")));
            buttons.put(getSlot(1, 3), new ArmorPieceButton(getPiece("chainmail")));
            buttons.put(getSlot(1, 4), new ArmorPieceButton(getPiece("iron")));
            buttons.put(getSlot(1, 5), new ArmorPieceButton(getPiece("gold")));
            buttons.put(getSlot(1, 6), new ArmorPieceButton(getPiece("diamond")));
        }

        return buttons;
    }

    @RequiredArgsConstructor
    public class ArmorPieceButton extends Button {

            private final ItemStack item;

            @Override
            public ItemStack getDisplayItem(Player player) {
                ItemBuilder itemBuilder = new ItemBuilder(item.getType());

                itemBuilder.addLore(
                        "",
                        "&7Click to select this " + armorPiece.name().toLowerCase()
                );

                return itemBuilder.toItemStack();
            }

            @Override
            public void onClick(Player player) {
                if (armorPiece == ArmorPiece.HELMET) {
                    npc.setHelmet(item);
                } else if (armorPiece == ArmorPiece.CHESTPLATE) {
                    npc.setChestplate(item);
                } else if (armorPiece == ArmorPiece.LEGGINGS) {
                    npc.setLeggings(item);
                } else if (armorPiece == ArmorPiece.BOOTS) {
                    npc.setBoots(item);
                }

                new NPCEditArmorMenu(plugin, npc).open(player);
            }
    }

    public ItemStack getPiece(String type) {

        if (type.equalsIgnoreCase("leather")) {
            if (armorPiece == ArmorPiece.HELMET){
                return new ItemStack(Material.LEATHER_HELMET);
            } else if (armorPiece == ArmorPiece.CHESTPLATE) {
                return new ItemStack(Material.LEATHER_CHESTPLATE);
            } else if (armorPiece == ArmorPiece.LEGGINGS) {
                return new ItemStack(Material.LEATHER_LEGGINGS);
            } else if (armorPiece == ArmorPiece.BOOTS) {
                return new ItemStack(Material.LEATHER_BOOTS);
            }
        }else if (type.equalsIgnoreCase("chainmail")) {
            if (armorPiece == ArmorPiece.HELMET) {
                return new ItemStack(Material.CHAINMAIL_HELMET);
            } else if (armorPiece == ArmorPiece.CHESTPLATE) {
                return new ItemStack(Material.CHAINMAIL_CHESTPLATE);
            } else if (armorPiece == ArmorPiece.LEGGINGS) {
                return new ItemStack(Material.CHAINMAIL_LEGGINGS);
            } else if (armorPiece == ArmorPiece.BOOTS) {
                return new ItemStack(Material.CHAINMAIL_BOOTS);
            }
        } else if (type.equalsIgnoreCase("iron")) {
            if (armorPiece == ArmorPiece.HELMET) {
                return new ItemStack(Material.IRON_HELMET);
            } else if (armorPiece == ArmorPiece.CHESTPLATE) {
                return new ItemStack(Material.IRON_CHESTPLATE);
            } else if (armorPiece == ArmorPiece.LEGGINGS) {
                return new ItemStack(Material.IRON_LEGGINGS);
            } else if (armorPiece == ArmorPiece.BOOTS) {
                return new ItemStack(Material.IRON_BOOTS);
            }
        } else if (type.equalsIgnoreCase("gold")) {
            if (armorPiece == ArmorPiece.HELMET) {
                return new ItemStack(Material.GOLD_HELMET);
            } else if (armorPiece == ArmorPiece.CHESTPLATE) {
                return new ItemStack(Material.GOLD_CHESTPLATE);
            } else if (armorPiece == ArmorPiece.LEGGINGS) {
                return new ItemStack(Material.GOLD_LEGGINGS);
            } else if (armorPiece == ArmorPiece.BOOTS) {
                return new ItemStack(Material.GOLD_BOOTS);
            }
        } else if (type.equalsIgnoreCase("diamond")) {
            if (armorPiece == ArmorPiece.HELMET) {
                return new ItemStack(Material.DIAMOND_HELMET);
            } else if (armorPiece == ArmorPiece.CHESTPLATE) {
                return new ItemStack(Material.DIAMOND_CHESTPLATE);
            } else if (armorPiece == ArmorPiece.LEGGINGS) {
                return new ItemStack(Material.DIAMOND_LEGGINGS);
            } else if (armorPiece == ArmorPiece.BOOTS) {
                return new ItemStack(Material.DIAMOND_BOOTS);
            }
        }

        return null;
    }
}
