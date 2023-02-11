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
public class NPCEditArmorMenu extends Menu {

    private final EntityPlugin plugin;
    private final NPC npc;

    @Override
    public String getTitle(Player player) {
        return "&eEditing armor for NPC &6" + npc.getName();
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

        buttons.put(getSlot(1, 2), new EditArmorPieceButton(npc.getHelmet(), ArmorPiece.HELMET));
        buttons.put(getSlot(1, 3), new EditArmorPieceButton(npc.getChestplate(), ArmorPiece.CHESTPLATE));
        buttons.put(getSlot(1, 4), new EditArmorPieceButton(npc.getLeggings(), ArmorPiece.LEGGINGS));
        buttons.put(getSlot(1, 5), new EditArmorPieceButton(npc.getBoots(), ArmorPiece.BOOTS));

        return buttons;
    }

    @RequiredArgsConstructor
    public class EditArmorPieceButton extends Button{

        private final ItemStack itemStack;
        private final ArmorPiece armorPiece;

        @Override
        public ItemStack getDisplayItem(Player player) {
            ItemBuilder builder = new ItemBuilder(itemStack == null ? Material.INK_SACK : itemStack.getType());

            if (itemStack == null) {
                builder.setDurability((short) 8);
            }

            builder.setName("&6" + armorPiece.name().toLowerCase());

            builder.addLore(
                    "",
                    "&7Click to change the " + armorPiece.name().toLowerCase()
            );

            return builder.toItemStack();
        }

        @Override
        public void onClick(Player player) {
            new NPCSelectArmorPieceMenu(plugin, npc, armorPiece).open(player);
        }
    }
}
