package rip.battle.crates.airdrop;

import cc.stormworth.core.kt.util.ItemBuilder;
import cc.stormworth.core.util.trail.ParticleEffect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import rip.battle.crates.Crates;
import rip.battle.crates.crate.Crate;

import java.util.Arrays;
import java.util.List;

public class Airdrop extends Crate {

    public Airdrop() {
        super("Airdrop");
        getCrates().put("Airdrop", this);
    }

    @Override
    public List<String> getHologramsLines() {
        return Arrays.asList(
                getDisplayName() + " Airdrop",
                "&7",
                "&fLeft click &7for preview rewards",
                "&7",
                "&7&ostore.battle.rip");
    }

    @Override
    public ItemStack generateKey() {
        return new ItemBuilder(Material.CHEST)
                .name("&6&lAirdrop")
                .addToLore(
                        "&7Purchasable at &fstore.battle.rip&7.",
                        "",
                        "&ePlace this down to designate a &6&lLocation &efor the &6&lLoot&e!"
                ).build();
    }

    public void startAnimation(Block block, Player player) {
        createFlameRings(block.getLocation().add(0, 0, 0.5));

        block.setMetadata("airdrop", new FixedMetadataValue(Crates.getInstance(), true));

        /*Dropper dropper = (Dropper) block.getState();

        List<Reward> rewardList = new ArrayList<>(getRewards());
        Collections.shuffle(rewardList);

        int i = 0;

        while (i <= 9) {
            Reward randomReward = RandomUtils.getRandomReward(rewardList);

            dropper.getInventory().setItem(i, randomReward.getItem());

            if (!randomReward.getBroadcast().isEmpty()) {
                randomReward.getBroadcast().forEach(line -> Bukkit.broadcastMessage(line.replace("{player}", player.getName())));
            }

            i++;
        }*/
    }

    private void createFlameRings(Location location) {
        double alpha = 0;
        for (int count = 0; count < 50; count++) {
            alpha += Math.PI / 16;

            Location firstLocation = location.clone().add(Math.cos(alpha), Math.sin(alpha) + 1, Math.sin(alpha));
            Location secondLocation = location.clone().add(Math.cos(alpha + Math.PI), Math.sin(alpha) + 1, Math.sin(alpha + Math.PI));

            ParticleEffect.FLAME.display(0, 0, 0, 0, 10, firstLocation, 10);
            ParticleEffect.FLAME.display(0, 0, 0, 0, 10, secondLocation, 10);
        }
    }
}