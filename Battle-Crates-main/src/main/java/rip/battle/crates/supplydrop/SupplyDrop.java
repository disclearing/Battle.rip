package rip.battle.crates.supplydrop;

import cc.stormworth.core.kt.util.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import rip.battle.crates.crate.Crate;

import java.util.Arrays;
import java.util.List;

public class SupplyDrop extends Crate {

    public SupplyDrop() {
        super("SupplyDrop");
        getCrates().put("SupplyDrop", this);
    }

    @Override
    public List<String> getHologramsLines() {
        return Arrays.asList(
                getDisplayName() + " SupplyDrop",
                "&7",
                "&fLeft click &7for preview rewards",
                "&7",
                "&7&ostore.battle.rip");
    }

    @Override
    public ItemStack generateKey() {
        return new ItemBuilder(Material.DROPPER)
                .name("&4&lSupplyDrop")
                .addToLore(
                        "&7Purchasable at &fstore.battle.rip&7.",
                        ""
                ).build();
    }


}
