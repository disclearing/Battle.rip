package rip.battle.crates.crate.commands;

import cc.stormworth.core.util.chat.CC;
import cc.stormworth.core.util.chat.Clickable;
import cc.stormworth.core.util.command.annotations.Command;
import cc.stormworth.core.util.command.annotations.Param;
import cc.stormworth.core.util.onedoteight.TitleBuilder;
import cc.stormworth.hcf.profile.HCFProfile;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import rip.battle.crates.airdrop.Airdrop;
import rip.battle.crates.crate.Crate;
import rip.battle.crates.crate.menus.CSGOOpenMenu;
import rip.battle.crates.crate.menus.CrateEditMenu;
import rip.battle.crates.crate.menus.CratePreviewMenu;
import rip.battle.crates.misterybox.MysteryBox;

public class CrateCommands {

    @Command(names = {"crate", "cr", "crates", "mysterybox", "mb"}, permission = "PLATFORMADMINISTRATOR", hidden = true)
    public static void help(Player player) {
        player.sendMessage(CC.translate("&a&lCrates Help"));
        player.sendMessage(CC.translate("&7- &e/crate create <name> &7- &aCreate a crate"));
        player.sendMessage(CC.translate("&7- &e/mystery create <name> &7- &aCreate a mystery box"));
        player.sendMessage("");
        player.sendMessage(CC.translate("&7- &e/crate delete <name> &7- &aDelete a crate"));
        player.sendMessage(CC.translate("&7- &e/crate list &7- &aList all crates"));
        player.sendMessage(CC.translate("&7- &e/crate edit <name> &7- &aEdit a crate"));
        player.sendMessage(CC.translate("&7- &e/crate preview <name> &7- &aPreview a crate"));
        player.sendMessage(CC.translate("&7- &e/crate give <name> <amount> <target> &7- &aGive a crate key to player"));
        player.sendMessage(CC.translate("&7- &e/reward &7- &aTo show help for rewards"));

        player.sendMessage(CC.translate("&7- &e/airdrop give <target> <amount> &7- &aTo show help for rewards"));
    }

    @Command(names = {"airdrop"}, permission = "", hidden = true)
    public static void airdrop(Player player) {
        Airdrop airdrop = (Airdrop) Crate.getByName("Airdrop");

        if (airdrop == null) {
            player.sendMessage(CC.translate("&cairdrop with that name does not exist!"));
            return;
        }

        new CratePreviewMenu(airdrop).openMenu(player);
    }

    @Command(names = {"crate create", "cr create", "crates create"}, permission = "PLATFORMADMINISTRATOR", hidden = true)
    public static void create(Player player, @Param(name = "name") String name) {
        if (Crate.getByName(name) != null) {
            player.sendMessage(CC.translate("&cCrate with that name already exists!"));
            return;
        }

        Crate crate = new Crate(name);

        crate.save();

        player.sendMessage(CC.translate("&aCrate &e" + name + "&a created!"));
    }

    @Command(names = {"mysterybox create", "mb create"}, permission = "PLATFORMADMINISTRATOR", hidden = true)
    public static void mysterybox(Player player, @Param(name = "name") String name) {
        if (Crate.getByName(name) != null) {
            player.sendMessage(CC.translate("&cCrate with that name already exists!"));
            return;
        }

        MysteryBox crate = new MysteryBox(name);

        crate.save();

        player.sendMessage(CC.translate("&aMystery &e" + name + "&a created!"));
    }

    @Command(names = {"crate delete", "cr delete", "crates delete", "crate remove", "cr remove", "crates remove"}, permission = "PLATFORMADMINISTRATOR", hidden = true)
    public static void delete(Player player, @Param(name = "name") String name) {
        Crate crate = Crate.getByName(name);
        if (crate == null) {
            player.sendMessage(CC.translate("&cCrate with that name does not exist!"));
            return;
        }

        crate.delete();
        player.sendMessage(CC.translate("&aCrate &e" + name + "&a deleted!"));
    }

    @Command(names = {"crate list", "cr list", "crates list", "mysterybox list", "mb list"}, permission = "PLATFORMADMINISTRATOR", hidden = true)
    public static void list(Player player) {
        player.sendMessage(CC.translate("&aCrates:"));
        for (Crate crate : Crate.getCrates().values()) {
            if (crate instanceof MysteryBox) {
                player.sendMessage(CC.translate("&7- &e" + crate.getName() + " &7- &aMystery Box"));
            } else if (crate instanceof Airdrop) {
                player.sendMessage(CC.translate("&7- &e" + crate.getName() + " &7- &aAirdrop"));
            } else {
                player.sendMessage(CC.translate("&7- &e" + crate.getName() + " &7- &aCrate"));
            }
        }
    }

    @Command(names = {"crate edit", "cr edit", "crates edit", "mysterybox edit", "mb edit", "airdrop edit", "ad edit"}, permission = "PLATFORMADMINISTRATOR", hidden = true)
    public static void edit(Player player, @Param(name = "name") String name) {
        Crate crate = Crate.getByName(name);
        if (crate == null) {
            player.sendMessage(CC.translate("&cCrate with that name does not exist!"));
            return;
        }

        new CrateEditMenu(crate).openMenu(player);
    }

    @Command(names = {"crate test"}, permission = "op", hidden = true)
    public static void test(Player player, @Param(name = "name") String name) {
        Crate crate = Crate.getByName(name);
        if (crate == null) {
            player.sendMessage(CC.translate("&cCrate with that name does not exist!"));
            return;
        }

        new CSGOOpenMenu(crate).openMenu(player);
    }

    @Command(names = {"airdrop edit", "ad edit"}, permission = "PLATFORMADMINISTRATOR", hidden = true)
    public static void airdropEdit(Player player) {
        Crate crate = Crate.getByName("Airdrop");

        if (crate == null) {
            player.sendMessage(CC.translate("&cCrate with that name does not exist!"));
            return;
        }

        new CrateEditMenu(crate).openMenu(player);
    }

    @Command(names = {"crate preview", "cr preview", "crates preview", "mysterybox preview", "mb preview"}, permission = "PLATFORMADMINISTRATOR", hidden = true)
    public static void preview(Player player, @Param(name = "name") String name) {
        Crate crate = Crate.getByName(name);
        if (crate == null) {
            player.sendMessage(CC.translate("&cCrate with that name does not exist!"));
            return;
        }

        new CratePreviewMenu(crate).openMenu(player);
    }

    @Command(names = {"crate give", "cr give", "crates give", "mysterybox give", "mb give", "airdrop give", "ad give"}, permission = "PLATFORMADMINISTRATOR", hidden = true)
    public static void give(CommandSender player, @Param(name = "name") String name,
                            @Param(name = "amount") int amount,
                            @Param(name = "target") Player target) {

        Crate crate = Crate.getByName(name);

        if (crate == null) {
            player.sendMessage(CC.translate("&cCrate with that name does not exist!"));
            return;
        }

        ItemStack item = crate.generateKey().clone();
        item.setAmount(amount);
        target.getInventory().addItem(item);

        if (crate instanceof MysteryBox) {
            player.sendMessage(CC.translate("&eYou have given x" + amount + " &6Mistery&e Keys to &6" + target.getName() + "&e!"));
        } else if (crate instanceof Airdrop) {
            player.sendMessage(CC.translate("&eYou have given x" + amount + " &6Airdrop&e to &6" + target.getName() + "&e!"));
        } else {
            player.sendMessage(CC.translate("&eYou have given x" + amount + " &6Crate Keys&e to &6" + target.getName() + "&e!"));
        }
    }

    @Command(names = {"givekey", "crate givekey"}, permission = "PLATFORMADMINISTRATOR", hidden = true)
    public static void give(CommandSender player,
                            @Param(name = "target") Player target,
                            @Param(name = "name") String name,
                            @Param(name = "amount") int amount) {
        Crate crate = Crate.getByName(name);
        if (crate == null) {
            player.sendMessage(CC.translate("&cCrate with that name does not exist!"));
            return;
        }

        ItemStack item = crate.generateKey().clone();
        item.setAmount(amount);

        if (target.getInventory().firstEmpty() == -1) {
            target.sendMessage("");
            target.sendMessage(CC.translate("&a&eYour inventory was &cfull&e."));

            Clickable clickable = new Clickable("&a[Click here] &eto recover the items.",
                    "&a[Click here]",
                    "/reclaimitems");
            clickable.sendToPlayer(target);
            target.sendMessage("");

            HCFProfile profile = HCFProfile.get(target);

            profile.getNoReclaimedItems().add(Crate.getByName("Blood").generateKey());
            return;
        }

        target.getInventory().addItem(item);
    }

    @Command(names = {"crate setchest", "cr setchest", "crates setchest", "mysterybox setchest", "mb setchest"}, permission = "PLATFORMADMINISTRATOR", hidden = true)
    public static void setChest(Player player, @Param(name = "name") String name) {
        Crate crate = Crate.getByName(name);

        if (crate == null) {
            player.sendMessage(CC.translate("&cCrate with that name does not exist!"));
            return;
        }

        Block targetBlock = player.getTargetBlock(null, 4);

        if (targetBlock == null) {
            player.sendMessage(CC.translate("&cYou must be look a block."));
            return;
        }

        Location location = targetBlock.getLocation();
        if (crate.getChestLocation() != null && crate.getChestLocation().equals(location)) {
            player.sendMessage(CC.translate("&cThere is another crate."));
            return;
        }

        crate.createHologram(location);

        crate.setChestLocation(location);
        player.sendMessage(CC.translate("&aSuccessfully set chest location."));
    }

    @Command(names = {"keyall"}, permission = "PLATFORMADMINISTRATOR", hidden = true)
    public static void keyall(CommandSender sender, @Param(name = "name") String name, @Param(name = "amount") int amount) {
        Crate crate = Crate.getByName(name);

        if (crate == null) {
            sender.sendMessage(CC.translate("&cCrate with that name does not exist!"));
            return;
        }

        for (Player target : Bukkit.getOnlinePlayers()) {
            ItemStack item = crate.generateKey().clone();
            item.setAmount(amount);
            if (target.getInventory().firstEmpty() != -1) {
                target.getInventory().addItem(item);
            } else {
                target.getWorld().dropItem(target.getLocation(), item);
            }

            TitleBuilder titleBuilder = new TitleBuilder(crate.getDisplayName() + " &areceived.",
                    "&eAmount: &f" + amount,
                    10, 20, 10);
            titleBuilder.send(target);
        }

        sender.sendMessage(CC.translate("&eYou have given &6x" + amount + " " + crate.getName() + " &eto &6everybody&e."));
    }

    @Command(names = {"keyalldone"}, permission = "PLATFORMADMINISTRATOR", hidden = true)
    public static void keyalldone(CommandSender sender) {

        for (Player other : Bukkit.getOnlinePlayers()) {
            TitleBuilder titleBuilder = new TitleBuilder("&4&lKeyall", "&7has finished.", 10, 20, 10);
            titleBuilder.send(other);
        }
    }

    @Command(names = {"create removechest", "cr removechest"}, permission = "PLATFORMADMINISTRATOR", hidden = true)
    public static void removeLocation(CommandSender sender, @Param(name = "name") String name) {
        Crate crate = Crate.getByName(name);

        if (crate == null) {
            sender.sendMessage(CC.translate("&cCrate with that name does not exist!"));
            return;
        }

        crate.setChestLocation(null);
        crate.removeHologram();
        sender.sendMessage(CC.translate("&aSuccessfully removed chest location."));
    }
}