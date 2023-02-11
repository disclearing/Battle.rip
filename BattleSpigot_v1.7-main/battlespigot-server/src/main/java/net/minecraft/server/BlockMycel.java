package net.minecraft.server;

import java.util.Random;
// CraftBukkit end

public class BlockMycel extends Block {

    protected BlockMycel() {
        super(Material.GRASS);
        this.a(true);
        this.a(CreativeModeTab.b);
    }

    public Item getDropType(int i, Random random, int j) {
        return Blocks.DIRT.getDropType(0, random, j);
    }
}