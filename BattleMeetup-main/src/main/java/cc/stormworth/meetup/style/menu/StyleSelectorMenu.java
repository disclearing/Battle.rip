package cc.stormworth.meetup.style.menu;

import cc.stormworth.core.util.chat.CC;
import cc.stormworth.meetup.style.Style;
import cc.stormworth.meetup.util.ItemBuilder;
import cc.stormworth.meetup.util.menu.Menu;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class StyleSelectorMenu extends Menu {

    public StyleSelectorMenu(Player player) {
        super(player, "Game Style Selector", 3, true);
    }

    @Override
    public void updateInventory(Player player) {
        this.getInventory().clear();

        this.set(12, new ItemBuilder(Material.GOLDEN_APPLE)
                .setName(CC.PRIMARY + "UHC")
                .setLore(CC.DARK_GRAY + "Game Style",
                        "",
                        CC.GRAY + "× " + CC.WHITE + "UHC styled kits!",
                        CC.GRAY + "× " + CC.WHITE + "Random scenarios!!!!!!!",
                        "",
                        CC.GRAY + "Votes: " + CC.WHITE + Style.UHC.getVotePercentage(),
                        "",
                        CC.SECONDARY + "Click to vote!"
                )
                .build());

        this.set(14, new ItemBuilder(Material.POTION)
                .setName(CC.PRIMARY + "HCF")
                .setLore(CC.DARK_GRAY + "Game Style",
                        "",
                        CC.GRAY + "× " + CC.WHITE + "HCF styled kits!",
                        CC.GRAY + "× " + CC.WHITE + "Countless abilities!",
                        "",
                        CC.GRAY + "Votes: " + CC.WHITE + Style.HCF.getVotePercentage(),
                        "",
                        CC.SECONDARY + "Click to vote!"
                )
                .setDurability(16421)
                .build());

        this.fill(new ItemBuilder(Material.STAINED_GLASS_PANE)
                .setName(" ")
                .setDurability(7)
                .build());
    }

    @Override
    public void onClickItem(Player player, ItemStack item, boolean isRightClicked) {

        if (item.getItemMeta() == null)
            return;

        Style gameStyle = Style.getByName(ChatColor.stripColor(item.getItemMeta().getDisplayName()));

        if (gameStyle == null)
            return;

        if (gameStyle.getPlayersVoted().contains(player.getUniqueId())) {
            player.sendMessage(CC.RED + "You have already voted for game style " + CC.B_RED + gameStyle.getName() + CC.RED + ".");
            this.playSound(false);
            this.close();
            return;
        }

        // Remove all player votes before casting vote
        for (Style style : Style.values()) {
            style.removeVote(player);
        }

        // Add vote here
        gameStyle.addVote(player);
        player.sendMessage(CC.SECONDARY + "You have voted for game style " + CC.PRIMARY + gameStyle.getName() + CC.SECONDARY + ".");
        this.playSound(true);
    }
}
