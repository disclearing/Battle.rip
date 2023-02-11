package cc.stormworth.meetup.border.glass;

import eu.vortexdev.battlespigot.BattleSpigot;
import eu.vortexdev.battlespigot.handler.MovementHandler;
import net.minecraft.server.v1_7_R4.PacketPlayInFlying;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class GlassMoveHandler implements MovementHandler {

    @Override
    public void handleUpdateLocation(Player player, Location from, Location to, PacketPlayInFlying packet) {
        GlassManager.getInstance().handleMove(player, from, to);
    }

    @Override
    public void handleUpdateRotation(Player player, Location location, Location location1, PacketPlayInFlying packetPlayInFlying) {
    }
}
