package rip.battle.crates.airdrop;

import cc.stormworth.core.util.chat.CC;
import cc.stormworth.core.util.general.LocationUtil;
import cc.stormworth.core.util.general.TaskUtil;
import cc.stormworth.hcf.Main;
import cc.stormworth.hcf.team.Team;
import cc.stormworth.hcf.team.claims.LandBoard;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.Skull;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;
import rip.battle.crates.Crates;
import rip.battle.crates.crate.Crate;
import rip.battle.crates.reward.Reward;
import rip.battle.crates.utils.FireworkUtil;
import rip.battle.crates.utils.ItemUtils;
import rip.battle.crates.utils.RandomUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AirdropListener implements Listener {

    @EventHandler
    public void onBlockPlaceEvent(BlockPlaceEvent event) {
        Player player = event.getPlayer();

        ItemStack item = event.getItemInHand();

        if (!ItemUtils.isAirDrop(item)) {
            return;
        }

        Airdrop airdrop = (Airdrop) Crate.getByName("Airdrop");
        if (!airdrop.isEnable()) {
            player.sendMessage(CC.translate("&cAirdrop is currently disabled."));
            event.setCancelled(true);
            player.playSound(player.getLocation(), Sound.ANVIL_BREAK, 1, 1);
            return;
        }

        if (!player.isOp()) {

            Team team = LandBoard.getInstance().getTeam(player.getLocation());

            if (team != null && !team.isMember(player.getUniqueId())) {
                player.sendMessage(CC.translate("&cYou can only put the &6&lAirdrop&c in your claim or in Wilderness."));
                event.setCancelled(true);
                return;
            }

            if (Main.getInstance().getServerHandler().isWarzone(player.getLocation())) {
                player.sendMessage(CC.translate("&cYou cannot place an &6&lAirdrop&c in a Warzone."));
                event.setCancelled(true);
                return;
            }
        }

        Location location = event.getBlock().getLocation().clone().add(0, 25, 0);

        if (location.getBlock() != null && location.getBlock().getType() != Material.AIR) {
            player.sendMessage(CC.translate("&cYou cannot place an &6&lAirdrop&c there."));
            player.sendMessage(CC.translate("&cGo out in the open air to deploy the &6&lAirdrop&c."));
            player.playSound(player.getLocation(), Sound.VILLAGER_NO, 1, 1);
            event.setCancelled(true);
            return;
        }

        FallingBlock block = player.getLocation().getWorld().spawnFallingBlock(
                location,
                Material.SNOW_BLOCK,
                (byte) 3);

        block.setMetadata("airdrop", new FixedMetadataValue(Crates.getInstance(), player.getName()));

        event.setCancelled(true);

        FireworkUtil.launchFirework(event.getBlock().getLocation());

        ItemUtils.consume(player);
    }

    @EventHandler
    public void onDeath(EntityChangeBlockEvent event) {
        if (!(event.getEntity() instanceof FallingBlock)) {
            return;
        }

        FallingBlock fallingBlock = (FallingBlock) event.getEntity();

        if (!fallingBlock.hasMetadata("airdrop")) {
            return;
        }

        Airdrop airdrop = (Airdrop) Crate.getByName("Airdrop");

        Player player = Bukkit.getPlayer(fallingBlock.getMetadata("airdrop").get(0).asString());

        fallingBlock.setDropItem(false);

        Block block = event.getBlock();
        new BukkitRunnable() {
            @Override
            public void run() {
                block.setType(Material.SKULL);
                block.setData((byte) 3);
                Skull skull = (Skull) block.getState();
                skull.setOwner("conhost");
                skull.update();
            }
        }.runTaskLater(Crates.getInstance(), 2L);
        for(Player online : Bukkit.getServer().getOnlinePlayers()) {
            if(((CraftPlayer) online).getHandle().playerConnection.networkManager.getVersion() < 47){
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        online.sendBlockChange(block.getLocation(), Material.CHEST, (byte) 0);
                    }
                }.runTaskLater(Crates.getInstance(), 2L);
            }
        }

        block.setMetadata("airdrop", new FixedMetadataValue(Crates.getInstance(), player.getName()));

        TaskUtil.runAsync(Crates.getInstance(), () -> airdrop.startAnimation(block, player));
    }

    /*@EventHandler
    public void onEntityChangeBlock(EntityChangeBlockEvent event) {

        if (!(event.getEntity() instanceof FallingBlock)) {
            return;
        }

        FallingBlock fallingBlock = (FallingBlock) event.getEntity();

        if (!fallingBlock.hasMetadata("airdrop")) {
            return;
        }

        Airdrop airdrop = (Airdrop) Crate.getByName("Airdrop");

        Player player = Bukkit.getPlayer(fallingBlock.getMetadata("airdrop").get(0).asString());

        Block block = event.getBlock();

        block.setType(Material.DROPPER);

        airdrop.startAnimation(block, player);
    }*/

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        if (event.getClickedBlock() == null) {
            return;
        }

        Block block = event.getClickedBlock();

        if (!block.hasMetadata("airdrop")) {
            return;
        }
        event.setCancelled(true);
        if(block.hasMetadata("airdropOpen")) return;

        if (block.getType() != Material.DROPPER) {
            Inventory inventory = Bukkit.createInventory(null, 9, "Airdrop");

            List<Reward> rewardList = new ArrayList<>(Airdrop.getByName("Airdrop").getRewards());
            Collections.shuffle(rewardList);

            int i = 0;

            while (i < 9) {
                Reward randomReward = RandomUtils.getRandomReward(rewardList);

                inventory.setItem(i, randomReward.getItem());

                if (!randomReward.getBroadcast().isEmpty()) {
                    randomReward.getBroadcast().forEach(line -> Bukkit.broadcastMessage(line.replace("{player}", player.getName())));
                }

                i++;
            }

            player.openInventory(inventory);
        }
        block.setMetadata("airdropOpen", new FixedMetadataValue(Crates.getInstance(), true));

        player.setMetadata("airdropOpen", new FixedMetadataValue(Crates.getInstance(), LocationUtil.parseLocation(block.getLocation())));

    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        Player player = (Player) event.getPlayer();

        if (!player.hasMetadata("airdropOpen")) {
            return;
        }

        if (!event.getInventory().getName().equals("Airdrop")) {
            return;
        }

        Inventory inventory = event.getInventory();

        for (int i = 0; i < inventory.getSize(); i++) {
            ItemStack item = inventory.getItem(i);

            if (item == null) {
                continue;
            }

            player.getLocation().getWorld().dropItem(player.getLocation(), item);
        }

        Location location = LocationUtil.convertLocation(player.getMetadata("airdropOpen").get(0).asString());

        Block block = location.getBlock();

        block.setType(Material.AIR);

        player.removeMetadata("airdropOpen", Crates.getInstance());
        block.removeMetadata("airdrop", Crates.getInstance());
    }
}