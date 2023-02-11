package cc.stormworth.meetup.ability.impl.pocketbard;

import cc.stormworth.core.menu.Button;
import cc.stormworth.core.util.chat.CC;
import cc.stormworth.core.util.cooldown.CooldownAPI;
import cc.stormworth.core.util.time.TimeUtil;
import cc.stormworth.meetup.ability.Ability;
import lombok.AllArgsConstructor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.Collections;
import java.util.List;

@AllArgsConstructor
public class PocketBardButton extends Button {

    private final String name;
    private final Ability ability;


    @Override
    public String getName(Player player) {
        return CC.translate(name);
    }

    @Override
    public List<String> getDescription(Player player) {

        if (CooldownAPI.hasCooldown(player, name + "_PocketBard")) {
            return Collections.singletonList(CC.translate(
                    "&cCooldowwn:&e " + TimeUtil.millisToRoundedTime(
                            CooldownAPI.getCooldown(player, name + "_PocketBard"))));
        }

        return Collections.singletonList(CC.translate("&7Click to select this bard buff"));
    }

    @Override
    public Material getMaterial(Player player) {
        return ability.getItem().getType();
    }

    @Override
    public void clicked(Player player, int slot, ClickType clickType) {
        ItemStack item = ability.getItem().clone();
        item.setAmount(3);
        player.closeInventory();
        if (player.getInventory().firstEmpty() == -1) {
            player.getWorld().dropItemNaturally(player.getLocation(), item);
        } else {
            player.getInventory().addItem(item);
        }

        consumeInInventory(player);
    }

    private void consumeInInventory(Player player) {
        Ability ability = Ability.getByName("PocketBard");

        for (int i = 0; i < player.getInventory().getSize(); i++) {
            if (player.getInventory().getItem(i) != null && ability.isItem(player.getInventory().getItem(i))) {

                if (player.getInventory().getItem(i).getAmount() > 1) {
                    player.getInventory().getItem(i).setAmount(player.getInventory().getItem(i).getAmount() - 1);
                } else {
                    player.getInventory().setItem(i, null);
                }
            }
        }
    }
}