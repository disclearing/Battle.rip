package rip.battle.crates.reward.commands;

import cc.stormworth.core.util.chat.CC;
import cc.stormworth.core.util.command.annotations.Command;
import cc.stormworth.core.util.command.annotations.Param;
import com.google.common.collect.Lists;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class RewardCommand {

    @Command(names = {"reward"}, permission = "PLATFORMADMINISTRATOR")
    public static void help(Player player) {
        player.sendMessage(CC.translate("&a&lReward Help"));
        player.sendMessage(CC.translate("&7- &e/reward setchance <chance> &7- &aSets the chance for the item in your hand"));
        player.sendMessage(CC.translate("&7- &e/reward setobligatory &7- &aSets the item in your hand to be obligatory"));
        player.sendMessage(CC.translate("&7- &e/reward addcommand <command> &7- &aAdds a command to the item in your hand"));
    }

    @Command(names = {"reward setchance"}, permission = "PLATFORMADMINISTRATOR")
    public static void setChance(Player player, @Param(name = "chance") int chance) {
        if (chance < 0 || chance > 100) {
            player.sendMessage(CC.translate("&cChance must be between 0 and 100!"));
            return;
        }

        ItemStack itemStack = player.getItemInHand();

        if (itemStack == null) {
            player.sendMessage(CC.translate("&cYou must be holding an item to set the chance!"));
            return;
        }

        ItemMeta meta = itemStack.getItemMeta();
        if (meta == null) {
            player.sendMessage(CC.translate("&cYou must be holding an item to set the chance!"));
            return;
        }

        List<String> lore = meta.getLore();

        if (lore == null) {
            lore = Lists.newArrayList();
        }

        lore.add(0, CC.translate("&7Chance:&a " + chance));

        meta.setLore(lore);
        itemStack.setItemMeta(meta);

        player.sendMessage(CC.translate("&aSuccessfully set chance for &e" + itemStack.getType().name() + " &ato &e" + chance + "%"));
    }

    @Command(names = {"reward setobligatory"}, permission = "PLATFORMADMINISTRATOR")
    public static void setObligatory(Player player) {
        ItemStack itemStack = player.getItemInHand();

        if (itemStack == null) {
            player.sendMessage(CC.translate("&cYou must be holding an item to set the chance!"));
            return;
        }

        ItemMeta meta = itemStack.getItemMeta();
        if (meta == null) {
            player.sendMessage(CC.translate("&cYou must be holding an item to set the chance!"));
            return;
        }

        List<String> lore = meta.getLore();

        if (lore == null) {
            lore = Lists.newArrayList();
        }

        lore.add(0, CC.translate("&7Obligatory"));

        meta.setLore(lore);
        itemStack.setItemMeta(meta);

        player.sendMessage(CC.translate("&aSuccessfully set obligatory for &e" + itemStack.getType().name()));
    }

    @Command(names = {"reward addcommand"}, permission = "PLATFORMADMINISTRATOR")
    public static void setcommand(Player player, @Param(name = "command", wildcard = true) String command) {
        ItemStack itemStack = player.getItemInHand();

        if (itemStack == null) {
            player.sendMessage(CC.translate("&cYou must be holding an item to set the chance!"));
            return;
        }

        ItemMeta meta = itemStack.getItemMeta();
        if (meta == null) {
            player.sendMessage(CC.translate("&cYou must be holding an item to set the chance!"));
            return;
        }

        List<String> lore = meta.getLore();

        if (lore == null) {
            lore = Lists.newArrayList();
        }

        lore.add(0, CC.translate("&7Command:&a " + command));

        meta.setLore(lore);
        itemStack.setItemMeta(meta);

        player.sendMessage(CC.translate("&aSuccessfully added command for &e" + itemStack.getType().name()));
    }

}