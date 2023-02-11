package rip.battle.crates.crate.listeners;

import cc.stormworth.core.util.chat.CC;
import cc.stormworth.core.util.general.TaskUtil;
import com.google.common.collect.Lists;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import rip.battle.crates.Crates;
import rip.battle.crates.airdrop.Airdrop;
import rip.battle.crates.crate.Crate;
import rip.battle.crates.crate.CratePlaceholder;
import rip.battle.crates.crate.menus.CSGOOpenMenu;
import rip.battle.crates.crate.menus.CrateEditMenu;
import rip.battle.crates.crate.menus.CratePreviewMenu;
import rip.battle.crates.misterybox.MysteryBox;
import rip.battle.crates.reward.Reward;
import rip.battle.crates.supplydrop.SupplyDrop;
import rip.battle.crates.utils.RandomUtils;

import java.util.List;

public class CrateListener implements Listener {

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        Player player = (Player) event.getPlayer();

        Inventory inventory = event.getInventory();

        if (player.hasMetadata("closeByRightClick")) {
            player.removeMetadata("closeByRightClick", Crates.getInstance());
            return;
        }

        if (inventory.getName().contains(CC.translate("&eEdit Rewards of "))) {

            String name = inventory.getName().replace(CC.translate("&eEdit Rewards of "), "");

            Crate crate = Crate.getByName(name);

            if (crate == null) {
                return;
            }

            List<CratePlaceholder> placeholders = Lists.newArrayList();

            for (int i = 0; i < 54; i++) {
                ItemStack item = inventory.getItem(i);

                if (item == null || item.getType() == null) {
                    continue;
                }

                if (crate.getReward(i) != null) {
                    continue;
                }

                if (crate instanceof MysteryBox) {
                    MysteryBox box = (MysteryBox) crate;

                    if (box.getObligatoryReward(i) != null) {
                        continue;
                    }
                }

                placeholders.add(new CratePlaceholder(item, i));
            }

            crate.setPlaceholders(placeholders);

            player.sendMessage(CC.translate("&aSuccessfully updated the rewards of &e" + name + "&a!"));
            player.playSound(player.getLocation(), Sound.LEVEL_UP, 0.5F, 0.5F);

            TaskUtil.runAsync(Crates.getInstance(), crate::save);

            TaskUtil.runLater(Crates.getInstance(), () -> new CrateEditMenu(crate).openMenu(player),
                    2L);
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        if (Crate.getCrateByKey(player.getItemInHand()) instanceof Airdrop) {
            player.sendMessage(CC.translate("&cTo use AirDrop you have to put the block on the ground."));
            return;
        }

        if (Crate.getCrateByKey(player.getItemInHand()) instanceof SupplyDrop) {
            return;
        }

        if (Crate.getCrateByKey(player.getItemInHand()) instanceof MysteryBox) {
            MysteryBox box = (MysteryBox) Crate.getCrateByKey(player.getItemInHand());
            box.openCrate(player);
            return;
        }

        if (Crate.getCrateByKey(player.getItemInHand()) != null) {

            Crate crate = Crate.getCrateByKey(player.getItemInHand());

            if (crate.getName().equalsIgnoreCase("Giftbox")) {
                crate.consumeKey(player);
                if (player.isSneaking()) {

                    if (player.getInventory().firstEmpty() == -1) {
                        player.sendMessage(CC.translate("&cYour inventory is full!"));
                        return;
                    }

                    Reward reward = RandomUtils.getRandomReward(crate.getRewards());

                    player.getInventory().addItem(reward.getItem());
                } else {
                    new CSGOOpenMenu(crate).openMenu(player);
                }
                return;
            }

            event.setCancelled(true);

            if (event.getAction().name().contains("RIGHT_CLICK")) {
                if (Crate.getCrateByKey(player.getItemInHand()) == crate) {
                    crate.openCrate(player);
                    return;
                }

                if (player.isSneaking() && player.getGameMode() == GameMode.CREATIVE && player.isOp()) {
                    new CrateEditMenu(crate).openMenu(player);
                    return;
                }

                new CratePreviewMenu(crate).openMenu(player);
            }
            if (event.getAction().name().contains("LEFT_CLICK")) {
                if (player.isSneaking() && player.getGameMode() == GameMode.CREATIVE && player.isOp()) {
                    new CrateEditMenu(crate).openMenu(player);
                    return;
                }

                new CratePreviewMenu(crate).openMenu(player);
            }
        }

        if (event.getClickedBlock() == null) return;

        Location location = event.getClickedBlock().getLocation();

        Crate crate = Crate.getByLocation(location);

        if (crate == null) {
            return;
        }

        event.setCancelled(true);

        if (event.getAction().name().contains("RIGHT_CLICK")) {
            if (Crate.getCrateByKey(player.getItemInHand()) == crate) {
                crate.openCrate(player);
                return;
            }

            if (player.isSneaking() && player.getGameMode() == GameMode.CREATIVE && player.isOp()) {
                new CrateEditMenu(crate).openMenu(player);
                return;
            }

            new CratePreviewMenu(crate).openMenu(player);
        }
        if (event.getAction().name().contains("LEFT_CLICK")) {
            if (player.isSneaking() && player.getGameMode() == GameMode.CREATIVE && player.isOp()) {
                new CrateEditMenu(crate).openMenu(player);
                return;
            }

            new CratePreviewMenu(crate).openMenu(player);
        }
    }
}