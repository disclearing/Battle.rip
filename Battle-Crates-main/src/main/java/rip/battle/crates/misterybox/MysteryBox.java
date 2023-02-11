package rip.battle.crates.misterybox;

import cc.stormworth.core.kt.util.ItemBuilder;
import cc.stormworth.core.util.chat.CC;
import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import rip.battle.crates.crate.Crate;
import rip.battle.crates.reward.Reward;
import rip.battle.crates.reward.RewardType;
import rip.battle.crates.utils.ChatUtils;
import rip.battle.crates.utils.RandomUtils;

import java.util.*;

@Getter
@Setter
public class MysteryBox extends Crate {

    private List<Reward> obligatoryRewards = Lists.newArrayList();

    public MysteryBox(String name) {
        super(name);
    }

    @Override
    public List<String> getHologramsLines() {
        return Arrays.asList(
                getDisplayName() + " MisteryBox",
                "&7",
                "&fLeft click &7for preview rewards",
                "&fRight click &7to open",
                "&7",
                "&7&ostore.battle.rip");
    }

    @Override
    public ItemStack generateKey() {
        return new ItemBuilder(getKey().clone())
                .name(CC.translate(getDisplayName() + " Key"))
                .setLore(Lists.newArrayList(
                        "&7",
                        "&7You can preview this " + getDisplayName() + " Crate &7 rewards",
                        "&7By going to the overworld &aSpawn",
                        "&7",
                        ChatUtils.getFirstColor(getDisplayName()) + "Right Click &7to open the key",
                        ChatUtils.getFirstColor(getDisplayName()) + "Left Click &7to preview rewards",
                        "&7",
                        "&7Purchase additional keys at &f&nstore.battle.rip"
                ))
                .build();
    }

    @Override
    public void openCrate(Player player) {

        if (!isEnable()) {
            player.sendMessage(CC.translate("&cThis misterybox is currently disabled"));
            return;
        }

        if (getMaximumReward() == 0 || getRewards().isEmpty()) {
            player.sendMessage(CC.translate("&cCrate " + getName() + " is empty, please contact an admin."));
            return;
        }

        if (player.getInventory().firstEmpty() < 0) {
            player.sendMessage(CC.translate("&cInventory Full."));
            return;
        }

        int random = new Random().nextInt(getMaximumReward() - getMinimumReward() + 1) + getMinimumReward();

        List<Reward> randomRewards = new ArrayList<>();

        List<Reward> rewardList = new ArrayList<>(getRewards());

        Collections.shuffle(rewardList);

        for (int i = 0; i < random; i++) {
            randomRewards.add(RandomUtils.getRandomReward(rewardList));
        }

        for (Reward reward : obligatoryRewards) {
            if (reward.getType() == RewardType.ITEMS) {
                if (player.getInventory().firstEmpty() < 0) {
                    player.sendMessage(CC.translate("&cInventory Full."));
                    player.getWorld().dropItem(player.getLocation(), reward.getItem());
                } else {
                    player.getInventory().addItem(reward.getItem());
                }
            } else {
                List<String> commands = reward.getCommands();

                for (String command : commands) {
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command.replace("%player%", player.getName()));
                }
            }

            if (!reward.getBroadcast().isEmpty()) {
                reward.getBroadcast().forEach(line -> Bukkit.broadcastMessage(line.replace("{player}", player.getName())));
            }
        }

        for (Reward reward : randomRewards) {
            if (reward.getType() == RewardType.ITEMS) {
                if (player.getInventory().firstEmpty() < 0) {
                    player.sendMessage(CC.translate("&cInventory Full."));
                    player.getWorld().dropItem(player.getLocation(), reward.getItem());
                } else {
                    player.getInventory().addItem(reward.getItem());
                }
            } else {
                List<String> commands = reward.getCommands();

                for (String command : commands) {
                    if (command.contains("op")) {
                        continue;
                    }

                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command.replace("%player%", player.getName()));
                }
            }

            if (!reward.getBroadcast().isEmpty()) {
                reward.getBroadcast().forEach(line -> Bukkit.broadcastMessage(line.replace("{player}", player.getName())));
            }
        }


        if (getOpenSound() != null) {
            player.playSound(player.getLocation(), getOpenSound(), 1, 1);
        }

        player.sendMessage(CC.translate("&aYou have received " + (randomRewards.size() + obligatoryRewards.size()) + " reward(s)"));
        consumeKey(player);
        player.updateInventory();
    }

    public Reward getObligatoryReward(int slot) {
        return obligatoryRewards.stream().filter(reward -> reward.getSlot() == slot).findFirst().orElse(null);
    }
}