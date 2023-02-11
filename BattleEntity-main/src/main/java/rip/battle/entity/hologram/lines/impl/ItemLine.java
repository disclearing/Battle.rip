package rip.battle.entity.hologram.lines.impl;

import lombok.Getter;
import lombok.Setter;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import rip.battle.entity.hologram.CraftHologram;
import rip.battle.entity.hologram.lines.HologramLine;
import rip.battle.entity.utils.DataWatcherUtil;
import rip.battle.entity.utils.PacketUtils;
import rip.battle.entity.utils.PlayerUtil;

import java.util.Collections;
import java.util.function.Consumer;
import java.util.function.Predicate;

@Getter @Setter
public class ItemLine extends HologramLine<ItemStack> {

    private ItemStack itemStack;

    public ItemLine(CraftHologram parent, Location location, ItemStack itemStack) {
        this(parent, location, itemStack, null);
    }

    public ItemLine(CraftHologram parent, Location location, ItemStack itemStack, Consumer<Player> onClick) {
        this(parent, location, itemStack, onClick, player -> true);
    }

    public ItemLine(CraftHologram parent, Location location, ItemStack itemStack, Consumer<Player> onClick, Predicate<Player> shouldSend) {
        super(parent, location, shouldSend, onClick);
        this.itemStack = itemStack;
        setPersistent(false);
    }

    @Override
    public void sendSpawnPackets(Player player) {
        boolean legacy = PlayerUtil.isLegacy(player);

        PacketPlayOutSpawnEntity spawnPacket = new PacketPlayOutSpawnEntity(
                getId(),
                2,
                (int) getLocation().getX(),
                (int) getLocation().getY(),
                (int) getLocation().getZ(),
                0,
                0
        );

        DataWatcher itemDataWatcher = DataWatcherUtil.createDataWatcher();

        itemDataWatcher.a(0, (byte) 0);
        itemDataWatcher.a(1, (short) 0);
        itemDataWatcher.a(2, "");
        itemDataWatcher.a(3, (byte) 0);
        itemDataWatcher.a(4, (byte) 0);

        itemDataWatcher.add(10, 5);
        itemDataWatcher.watch(10, CraftItemStack.asNMSCopy(itemStack));

        DataWatcherUtil.setFlag(itemDataWatcher, 5, false);

        PacketPlayOutEntityMetadata metadataPacket = new PacketPlayOutEntityMetadata(getId(), itemDataWatcher, true);

        DataWatcher armorStandDataWatcher = DataWatcherUtil.createDataWatcher();

        if (!legacy) {
            armorStandDataWatcher.a(0, (byte) 0);
            armorStandDataWatcher.a(1, (short) 0);
            armorStandDataWatcher.a(2, "");
            armorStandDataWatcher.a(3, (byte) 0);
            armorStandDataWatcher.a(4, (byte) 0);

            DataWatcherUtil.setFlag(armorStandDataWatcher, 5, true);

            armorStandDataWatcher.a(6, (float) 1);

            armorStandDataWatcher.a(7, 0);

            armorStandDataWatcher.a(8, (byte) 0);
            armorStandDataWatcher.a(9, (byte) 0);
            armorStandDataWatcher.a(10, (byte) 0);

            DataWatcherUtil.setTypeFlag(armorStandDataWatcher, 10, 1, true);
            DataWatcherUtil.setTypeFlag(armorStandDataWatcher, 10, 4, false);
            DataWatcherUtil.setTypeFlag(armorStandDataWatcher, 10, 8, false);
            DataWatcherUtil.setTypeFlag(armorStandDataWatcher, 10, 10, true);

            armorStandDataWatcher.a(11, PacketUtils.ARMOR_STAND_HEAD_POSE);
            armorStandDataWatcher.a(12, PacketUtils.ARMOR_STAND_BODY_POSE);
            armorStandDataWatcher.a(13, PacketUtils.ARMOR_STAND_LEFT_ARM_POSE);
            armorStandDataWatcher.a(14, PacketUtils.ARMOR_STAND_RIGHT_ARM_POSE);
            armorStandDataWatcher.a(15, PacketUtils.ARMOR_STAND_LEFT_LEG_POSE);
            armorStandDataWatcher.a(16, PacketUtils.ARMOR_STAND_RIGHT_LEG_POSE);
        }

        Packet<?> armorStandPacket;

        if (legacy){
            EntityWitherSkull skull = new EntityWitherSkull(((CraftWorld) getLocation().getWorld()).getHandle());
            skull.d(getSupportEntityID());
            skull.setLocation(getLocation().getX(), (getLocation().getY() + 1.5), getLocation().getZ(), 0.0f, 0.0f);
            armorStandPacket = new PacketPlayOutSpawnEntity(skull, 66);
        } else {
            armorStandPacket = new PacketPlayOutSpawnEntityLiving(
                    getSupportEntityID(),
                    30,
                    MathHelper.floor(getLocation().getX() * 32.0D),
                    MathHelper.floor((getLocation().getY() + 0.15) * 32.0D),
                    MathHelper.floor(getLocation().getZ() * 32.0D),
                    armorStandDataWatcher
            );
        }

        PacketPlayOutAttachEntity attachPacket = new PacketPlayOutAttachEntity(0, getId(), getSupportEntityID());

        PacketUtils.sendPacket(player, spawnPacket);
        PacketUtils.sendPacket(player, metadataPacket);
        PacketUtils.sendPacket(player, armorStandPacket);
        PacketUtils.sendPacket(player, attachPacket);
    }

    @Override
    public void sendRePositionPackets(Player player){

        PacketPlayOutEntityTeleport teleportPacket;

        if (PlayerUtil.isLegacy(player)){
            teleportPacket = new PacketPlayOutEntityTeleport(
                    getSupportEntityID(),
                    MathHelper.floor(getLocation().getX() * 32.0D),
                    MathHelper.floor((getLocation().getY() + 1.5) * 32.0D) ,
                    MathHelper.floor(getLocation().getZ() * 32.0D),
                    (byte) 0,
                    (byte) 0,
                    false
            );
        }else{
            teleportPacket = new PacketPlayOutEntityTeleport(
                    getSupportEntityID(),
                    MathHelper.floor(getLocation().getX() * 32.0D),
                    MathHelper.floor((getLocation().getY() + 0.15) * 32.0D) ,
                    MathHelper.floor(getLocation().getZ() * 32.0D),
                    (byte) 0,
                    (byte) 0,
                    false
            );
        }

        PacketUtils.sendPacket(player, teleportPacket);
    }

    @Override
    public void sendUpdatePackets(Player player) {
        PacketPlayOutEntityMetadata packet = new PacketPlayOutEntityMetadata();

        packet.setEntityId(getId());
        packet.setData(Collections.singletonList(new DataWatcher.WatchableObject(5, 10, CraftItemStack.asNMSCopy(itemStack))));

        PacketUtils.sendPacket(player, packet);
    }

    @Override
    public void sendDestroyPackets(Player player) {
        PacketPlayOutEntityDestroy destroyPacket = new PacketPlayOutEntityDestroy(getId(), getSupportEntityID());
        PacketUtils.sendPacket(player, destroyPacket);

        super.sendDestroyPackets(player);
    }

    @Override
    public ItemStack getLine(Player player) {
        return itemStack;
    }

    @Override
    public void setLine(ItemStack line) {
        this.itemStack = line;
    }
}
