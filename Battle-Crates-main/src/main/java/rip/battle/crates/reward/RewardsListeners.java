package rip.battle.crates.reward;

import cc.stormworth.core.util.chat.CC;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.metadata.FixedMetadataValue;
import rip.battle.crates.Crates;
import rip.battle.crates.crate.Crate;
import rip.battle.crates.crate.CratePlaceholder;
import rip.battle.crates.misterybox.MysteryBox;
import rip.battle.crates.reward.menus.RewardEditMenu;
import rip.battle.crates.utils.ItemUtils;

public class RewardsListeners implements Listener {

    @EventHandler
    public void onInventoryOpen(InventoryOpenEvent event) {
        Inventory inventory = event.getInventory();

        if (inventory.getName().contains(CC.translate("&eEdit Rewards of "))) {

            String name = inventory.getName().replace(CC.translate("&eEdit Rewards of "), "");

            Crate crate = Crate.getByName(name);

            if (crate == null) {
                return;
            }

            for (CratePlaceholder placeholder : crate.getPlaceholders()) {
                if (placeholder.getItem() == null) {
                    crate.getPlaceholders().remove(placeholder);
                    continue;
                }

                inventory.setItem(placeholder.getSlot(), placeholder.getItem());
            }

            crate.getRewards().forEach(reward -> inventory.setItem(reward.getSlot(), reward.getPreviewItem()));

            if (crate instanceof MysteryBox) {
                MysteryBox box = (MysteryBox) crate;
                box.getObligatoryRewards().forEach(reward -> inventory.setItem(reward.getSlot(), reward.getPreviewObligatoryItem()));
            }

        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();

        Inventory inventory = event.getInventory();

        if (event.getClickedInventory() == null) {
            return;
        }

        if (inventory.getName().contains(CC.translate("&eEdit Rewards of "))) {

            String name = inventory.getName().replace(CC.translate("&eEdit Rewards of "), "");

            Crate crate = Crate.getByName(name);

            if (crate == null) {
                return;
            }

            if (event.getClick() == ClickType.RIGHT) {

                if (event.getClickedInventory() == player.getInventory()) {
                    return;
                }

                event.setCancelled(true);


                if (crate instanceof MysteryBox) {
                    MysteryBox box = (MysteryBox) crate;

                    if (box.getObligatoryReward(event.getSlot()) != null) {
                        player.setMetadata("closeByRightClick", new FixedMetadataValue(Crates.getInstance(), true));

                        new RewardEditMenu(box.getObligatoryReward(event.getSlot()), crate).openMenu(player);
                        return;
                    }
                }

                if (crate.getReward(event.getSlot()) != null) {
                    player.setMetadata("closeByRightClick", new FixedMetadataValue(Crates.getInstance(), true));
                    new RewardEditMenu(crate.getReward(event.getSlot()), crate).openMenu(player);
                } else if (crate.getPlaceholder(event.getSlot()) != null) {
                    CratePlaceholder placeholder = crate.getPlaceholder(event.getSlot());
                    Reward newReward = new Reward(placeholder.getItem().clone(), 100, placeholder.getSlot(), RewardType.ITEMS, null, false);

                    crate.getPlaceholders().remove(placeholder);
                    crate.getRewards().add(newReward);

                    player.setMetadata("closeByRightClick", new FixedMetadataValue(Crates.getInstance(), true));

                    new RewardEditMenu(newReward, crate).openMenu(player);
                } else {
                    Reward newReward = new Reward(event.getCurrentItem(), 100, event.getSlot(), RewardType.ITEMS, null, false);
                    
                    crate.getRewards().add(newReward);

                    player.setMetadata("closeByRightClick", new FixedMetadataValue(Crates.getInstance(), true));

                    new RewardEditMenu(newReward, crate).openMenu(player);
                }
            } else {
                if (event.getClick() == ClickType.DROP || event.getClick() == ClickType.CONTROL_DROP) {
                    if (crate.getPlaceholder(event.getSlot()) != null) {
                        crate.getPlaceholders().remove(crate.getPlaceholder(event.getSlot()));
                    }

                    if (crate.getReward(event.getSlot()) != null) {
                        crate.getRewards().remove(crate.getReward(event.getSlot()));
                    }

                    if (crate instanceof MysteryBox) {
                        MysteryBox box = (MysteryBox) crate;

                        if (box.getObligatoryReward(event.getSlot()) != null) {
                            box.getObligatoryRewards().remove(box.getObligatoryReward(event.getSlot()));
                        }
                    }
                } else if (event.getClick() == ClickType.SHIFT_LEFT) {
                    if (event.getClickedInventory() == player.getInventory()) {

                        if (ItemUtils.isReward(event.getCursor())) {
                            Reward reward = ItemUtils.getRewardByItem(event.getCurrentItem().clone(), inventory.firstEmpty());

                            if (reward != null) {
                                if (reward.isObligatory()) {
                                    if (crate instanceof MysteryBox) {
                                        MysteryBox box = (MysteryBox) crate;

                                        box.getObligatoryRewards().add(reward);
                                        return;
                                    }
                                }

                                crate.getRewards().add(reward);

                                return;
                            }
                        }

                        crate.getPlaceholders().add(new CratePlaceholder(event.getCurrentItem().clone(), inventory.firstEmpty()));
                    } else {
                        if (crate.getPlaceholder(event.getSlot()) != null) {
                            crate.getPlaceholders().remove(crate.getPlaceholder(event.getSlot()));
                        }

                        if (crate.getReward(event.getSlot()) != null) {
                            crate.getRewards().remove(crate.getReward(event.getSlot()));
                        }

                        if (crate instanceof MysteryBox) {
                            MysteryBox box = (MysteryBox) crate;

                            if (box.getObligatoryReward(event.getSlot()) != null) {
                                box.getObligatoryRewards().remove(box.getObligatoryReward(event.getSlot()));
                            }
                        }
                    }
                } else if (event.getClick() == ClickType.LEFT) {
                    if (event.getClickedInventory() == player.getInventory()) {
                        return;
                    }

                    if (event.getInventory().getItem(event.getSlot()) == null) {
                        if (ItemUtils.isReward(event.getCursor())) {
                            Reward reward = ItemUtils.getRewardByItem(event.getCursor().clone(), event.getSlot());

                            if (reward != null) {
                                if (reward.isObligatory()) {
                                    if (crate instanceof MysteryBox) {
                                        MysteryBox box = (MysteryBox) crate;

                                        box.getObligatoryRewards().add(reward);
                                        return;
                                    }
                                }

                                crate.getRewards().add(reward);

                                return;
                            }
                        }

                        crate.getPlaceholders().add(new CratePlaceholder(event.getCursor().clone(), event.getSlot()));
                    } else {
                        if (crate.getPlaceholder(event.getSlot()) != null) {
                            crate.getPlaceholders().remove(crate.getPlaceholder(event.getSlot()));
                        }

                        if (crate.getReward(event.getSlot()) != null) {
                            crate.getRewards().remove(crate.getReward(event.getSlot()));
                        }

                        if (crate instanceof MysteryBox) {
                            MysteryBox box = (MysteryBox) crate;

                            if (box.getObligatoryReward(event.getSlot()) != null) {
                                box.getObligatoryRewards().remove(box.getObligatoryReward(event.getSlot()));
                            }
                        }
                    }
                }
            }
        }
    }

}