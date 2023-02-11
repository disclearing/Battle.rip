package cc.stormworth.meetup.border.glass;

import cc.stormworth.meetup.util.TaskUtil;
import eu.vortexdev.battlespigot.BattleSpigot;
import eu.vortexdev.battlespigot.handler.PacketHandler;
import net.minecraft.server.v1_7_R4.Packet;
import net.minecraft.server.v1_7_R4.PacketPlayInBlockDig;
import net.minecraft.server.v1_7_R4.PacketPlayInBlockPlace;
import net.minecraft.server.v1_7_R4.PlayerConnection;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class GlassPacketHandler implements PacketHandler {

    @Override
    public void handleReceivedPacket(PlayerConnection connection, Packet packet) {
        if (packet instanceof PacketPlayInBlockDig) {
            CraftPlayer craftPlayer = connection.getPlayer();
            PacketPlayInBlockDig digPacket = (PacketPlayInBlockDig) packet;
            if (digPacket.g() != 0 && digPacket.g() != 2)
                return;
            Location location = new Location(craftPlayer.getWorld(), digPacket.c(), digPacket.d(), digPacket.e());
            GlassInfo glassInfo = GlassManager.getInstance().getGlassAt(craftPlayer, location);
            if (glassInfo != null)
                if (craftPlayer.getGameMode() == GameMode.CREATIVE) {
                    TaskUtil.runAsyncLater(() -> craftPlayer.sendBlockChange(location, glassInfo.getMaterial(), glassInfo.getData()), 2L);
                } else {
                    craftPlayer.sendBlockChange(location, glassInfo.getMaterial(), glassInfo.getData());
                }
        } else if (packet instanceof PacketPlayInBlockPlace) {
            CraftPlayer craftPlayer = connection.getPlayer();
            PacketPlayInBlockPlace placePacket = (PacketPlayInBlockPlace) packet;
            Location location = new Location(craftPlayer.getWorld(), placePacket.c(), placePacket.d(), placePacket.e());
            GlassInfo glassInfo = GlassManager.getInstance().getGlassAt(craftPlayer, location);
            if (glassInfo != null)
                TaskUtil.runAsyncLater(() -> craftPlayer.sendBlockChange(location, glassInfo.getMaterial(), glassInfo.getData()), 2L);
        }
    }

    @Override
    public void handleSentPacket(PlayerConnection playerConnection, Packet packet) {

    }
}
