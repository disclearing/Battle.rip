package net.tacospigot;

import net.minecraft.server.*;
import org.bukkit.event.inventory.InventoryMoveItemEvent;

public class HopperHelper {

    public static TileEntityHopper getHopper(World world, int x, int y, int z) {
        TileEntity tileEntity = world.getTileEntity(x, y, z);
        if (tileEntity instanceof TileEntityHopper) {
            return (TileEntityHopper) tileEntity;
        }
        return null;
    }

    public static IInventory getInventory(World world, int x, int y, int z) {
        Block block = world.getType(x, y, z);
        if (block instanceof BlockChest) {
            return ((BlockChest) block).m(world, x, y, z);
        }
        if (block.isTileEntity()) {
            TileEntity tile = world.getTileEntity(x, y, z);
            if (tile instanceof IInventory) return (IInventory) tile;
        }
        return null;
    }

    public static boolean isFireInventoryMoveItemEvent(IHopper hopper) {
        return hopper.getWorld().tacoSpigotWorldConfig.isHopperFireIMIE && InventoryMoveItemEvent.getHandlerList().getRegisteredListeners().length > 0;
    }
}