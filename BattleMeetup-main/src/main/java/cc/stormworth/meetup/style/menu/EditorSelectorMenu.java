package cc.stormworth.meetup.style.menu;

import cc.stormworth.core.util.chat.CC;
import cc.stormworth.meetup.Meetup;
import cc.stormworth.meetup.style.hcf.kit.menu.HCFEditorMenu;
import cc.stormworth.meetup.tasks.StartingTask;
import cc.stormworth.meetup.util.ItemBuilder;
import cc.stormworth.meetup.util.menu.Menu;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class EditorSelectorMenu extends Menu {

    public EditorSelectorMenu(Player player) {
        super(player, "Kit Editor Selector", 3, true);
    }

    @Override
    public void updateInventory(Player player) {
        this.getInventory().clear();
        this.set(12, new ItemBuilder(Material.GOLDEN_APPLE)
                .setName(CC.PRIMARY + "UHC")
                .setLore(CC.DARK_GRAY + "Kit Layout",
                        "",
                        CC.GRAY + "Kit editor for UHC styled games.",
                        "",
                        CC.SECONDARY + "Click to open!"
                )
                .build());
        this.set(14, new ItemBuilder(Material.POTION)
                .setName(CC.PRIMARY + "HCF")
                .setLore(CC.DARK_GRAY + "Kit Layout",
                        "",
                        CC.GRAY + "Kit editor for HCF styled games.",
                        "",
                        CC.SECONDARY + "Click to open!"
                )
                .setDurability(16421)
                .build());
        this.fill(new ItemBuilder(Material.STAINED_GLASS_PANE)
                .setName(" ")
                .setDurability(7)
                .build());
    }

    @Override
    public void onClickItem(Player player, ItemStack item, boolean b) {
        if (item.getItemMeta() == null)
            return;

        if (StartingTask.editorBlocked) {
            player.sendMessage(CC.translate("&cYou're not allowed to edit your kit while scattering!"));
            return;
        }

        switch (ChatColor.stripColor(item.getItemMeta().getDisplayName())) {
            default:
                break;
            case "UHC":
                this.playSound(true);
                player.closeInventory();
                Meetup.getInstance().getKitEditorMenu().openKitEditor(player);
                break;
            case "HCF":
                this.playSound(true);
                player.closeInventory();
                new HCFEditorMenu(player).openInventory();
                break;
        }
    }
}
