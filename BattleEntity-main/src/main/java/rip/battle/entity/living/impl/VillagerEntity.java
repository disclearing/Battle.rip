package rip.battle.entity.living.impl;

import lombok.Getter;
import lombok.Setter;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import rip.battle.entity.living.LivingEntity;
import rip.battle.entity.utils.AngleUtils;
import rip.battle.entity.utils.DataWatcherUtil;
import rip.battle.entity.utils.PacketUtils;

import java.util.Collections;

@Setter @Getter
public class VillagerEntity extends LivingEntity {

    public VillagerEntity(Location location) {
        super(location);
    }

    @Override
    public String getTypeName() {
        return "Villager";
    }

    @Override
    public void sendSpawnPackets(Player player) {
        DataWatcher dataWatcher = DataWatcherUtil.createDataWatcher();

        dataWatcher.a(0, (byte) 0);
        dataWatcher.a(1, (short) 300);
        dataWatcher.a(2, getCustomName() == null ? "" : getCustomName());
        dataWatcher.a(3, (byte) 1);
        dataWatcher.a(4, (byte) 1);
        dataWatcher.a(6, (float) 1);
        dataWatcher.a(10, (byte) 2);
        dataWatcher.a(12, (byte) 0);
        dataWatcher.a(16, 0);

        PacketPlayOutSpawnEntityLiving spawnPacket = new PacketPlayOutSpawnEntityLiving(
                getId(),
                120,
                MathHelper.floor(getLocation().getX() * 32.0D),
                MathHelper.floor(getLocation().getY() * 32.0D),
                MathHelper.floor(getLocation().getZ() * 32.0D),
                dataWatcher);

        PacketPlayOutEntity.PacketPlayOutEntityLook lookPacket = new PacketPlayOutEntity.PacketPlayOutEntityLook(
                getId(),
                AngleUtils.yawToBytes(getLocation().getYaw()),
                AngleUtils.yawToBytes(getLocation().getPitch()),
                true);

        PacketPlayOutEntityHeadRotation headRotationPacket = new PacketPlayOutEntityHeadRotation(
                getId(),
                AngleUtils.yawToBytes(getLocation().getYaw()));

        PacketPlayOutEntityTeleport teleportPacket = new PacketPlayOutEntityTeleport(
                getId(),
                MathHelper.floor(getLocation().getX() * 32.0D),
                MathHelper.floor(getLocation().getY() * 32.0D),
                MathHelper.floor(getLocation().getZ() * 32.0D),
                AngleUtils.yawToBytes(getLocation().getYaw()),
                AngleUtils.yawToBytes(getLocation().getPitch()),
                true);

        PacketUtils.sendPacket(player, spawnPacket);
        PacketUtils.sendPacket(player, lookPacket);
        PacketUtils.sendPacket(player, headRotationPacket);
        PacketUtils.sendPacket(player, teleportPacket);
    }

    @Override
    public void sendUpdatePackets(Player player) {
        PacketPlayOutEntityMetadata packet = new PacketPlayOutEntityMetadata();

        packet.setEntityId(getId());
        packet.setData(Collections.singletonList(new DataWatcher.WatchableObject(4, 2, getCustomName())));

        PacketUtils.sendPacket(player, packet);
    }
}
