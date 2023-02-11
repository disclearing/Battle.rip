package rip.battle.entity.utils;

import lombok.experimental.UtilityClass;
import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.Vector3f;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.util.List;

@UtilityClass
public class PacketUtils {

    public static final Vector3f ARMOR_STAND_HEAD_POSE = new Vector3f(0, 0, 0);
    public static final Vector3f ARMOR_STAND_BODY_POSE = new Vector3f(0, 0, 0);
    public static final Vector3f ARMOR_STAND_LEFT_ARM_POSE = new Vector3f(-10, 0, -10);
    public static final Vector3f ARMOR_STAND_RIGHT_ARM_POSE = new Vector3f(-15, 0, 10);
    public static final Vector3f ARMOR_STAND_LEFT_LEG_POSE = new Vector3f(-1, 0, -1);
    public static final Vector3f ARMOR_STAND_RIGHT_LEG_POSE = new Vector3f(1, 0, 1);

    public void sendPacket(List<Player> players, Packet<?> packet) {
        players.forEach(player -> sendPacket(player, packet));
    }

    public void sendPacket(Player player, Packet<?> packet){
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
    }

}
