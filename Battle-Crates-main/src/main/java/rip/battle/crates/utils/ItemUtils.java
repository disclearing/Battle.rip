package rip.battle.crates.utils;

import cc.stormworth.core.util.chat.CC;
import com.google.common.collect.Lists;
import lombok.experimental.UtilityClass;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import rip.battle.crates.reward.Reward;
import rip.battle.crates.reward.RewardType;

import java.util.List;

@UtilityClass
public class ItemUtils {

    public boolean isReward(ItemStack item) {

        if (item == null) return false;

        if (!item.hasItemMeta()) return false;

        if (!item.getItemMeta().hasLore()) return false;

        return item.getItemMeta().getLore().stream().anyMatch(lore -> lore.contains(CC.translate("&7Type: &e")));
    }

    public void consume(Player player) {

        ItemStack item = player.getInventory().getItemInHand();

        if (item.getAmount() == 1) {
            player.getInventory().remove(item);
        } else {
            item.setAmount(item.getAmount() - 1);
        }

        player.updateInventory();
    }

    public RewardType getRewardType(ItemStack item) {
        if (!isReward(item)) return null;

        for (String lore : item.getItemMeta().getLore()) {
            if (lore.contains(CC.translate("&7Type: &e"))) {
                return RewardType.getByName(ChatColor.stripColor(lore).replace(CC.translate("Type: "), "").toUpperCase());
            }
        }

        return null;
    }


    public Reward getRewardByItem(ItemStack item, int slot) {
        if (!isReward(item)) return null;

        RewardType type = getRewardType(item);

        if (type == null) return null;

        double chance = getChance(item);

        List<String> commands = Lists.newArrayList();

        if (type == RewardType.COMMAND) {
            commands.addAll(getCommands(item));
        }

        return new Reward(item, chance, slot, type, commands, isObligatory(item));
    }

    public void removeRewardsLore(ItemStack item) {
        ItemMeta meta = item.getItemMeta();

        if (meta == null) return;

        List<String> lore = meta.getLore();

        if (lore == null) return;

        lore.removeIf(line -> line.contains(CC.translate("&7Type: &e")));
        lore.removeIf(line -> line.contains(CC.translate("&7Chance: &e")));
        lore.removeIf(line -> line.contains(CC.translate("&cObligatory")));
        lore.removeIf(line -> line.contains(CC.translate("&7Commands: &e")));
        lore.removeIf(line -> line.contains(CC.translate("&fRight Click &7to edit")));
        lore.removeIf(line -> line.equalsIgnoreCase(CC.translate("&0")));

        meta.setLore(lore);

        item.setItemMeta(meta);
    }

    public double getChance(ItemStack item) {
        if (!isReward(item)) return 0;
        for (String lore : item.getItemMeta().getLore()) {
            if (lore.contains(CC.translate("&7Chance: &e"))) {
                return Double.parseDouble(ChatColor.stripColor(lore).replace(CC.translate("Chance: "), "").replace("%", ""));
            }
        }

        return 0;
    }

    public boolean isObligatory(ItemStack item) {

        if (item == null) return false;

        if (!item.hasItemMeta()) return false;

        if (!item.getItemMeta().hasLore()) return false;

        return item.getItemMeta().getLore().stream().anyMatch(lore -> lore.contains(CC.translate("&cObligatory")));
    }

    public boolean isCommandType(ItemStack item) {

        if (item == null) return false;

        if (!item.hasItemMeta()) return false;

        if (!item.getItemMeta().hasLore()) return false;

        return item.getItemMeta().getLore().stream().anyMatch(lore -> lore.contains(CC.translate("&7Commands: &e")));
    }

    public List<String> getCommands(ItemStack item) {
        if (!isCommandType(item)) return null;

        List<String> commands = Lists.newArrayList();

        for (String lore : item.getItemMeta().getLore()) {
            if (lore.contains(CC.translate(" &7- &f"))) {
                commands.add(ChatColor.stripColor(lore).replace(CC.translate(" - "), ""));
            }
        }

        return commands;
    }

    public boolean isAirDrop(ItemStack item) {
        if (item == null) return false;
        if (!item.hasItemMeta()) return false;
        if (!item.getItemMeta().hasLore()) return false;
        if (item.getItemMeta().getDisplayName() == null) return false;
        return item.getItemMeta().getDisplayName().equalsIgnoreCase(CC.translate("&6&lAirdrop"));
    }
}