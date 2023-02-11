package rip.battle.entity.packet;

import cc.stormworth.core.util.packet.PacketAdapter;
import lombok.RequiredArgsConstructor;
import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PacketPlayInUseEntity;
import org.bukkit.entity.Player;
import rip.battle.entity.Entity;
import rip.battle.entity.EntityManager;
import rip.battle.entity.EntityPlugin;
import rip.battle.entity.hologram.api.Hologram;
import rip.battle.entity.hologram.lines.HologramLine;
import rip.battle.entity.hologram.menu.HologramEditMenu;
import rip.battle.entity.npc.NPCEntity;
import rip.battle.entity.npc.api.NPC;
import rip.battle.entity.npc.menu.NPCEditMenu;

@RequiredArgsConstructor
public class PacketEntityUseAdapter implements PacketAdapter<PacketPlayInUseEntity> {

    private final EntityPlugin plugin;
    @Override
    public Packet<?> handle(Player player, PacketPlayInUseEntity packetPlayInUseEntity) {

        int id = packetPlayInUseEntity.getA();
        Entity entity = EntityManager.getEntity(id);

        if (entity == null){
            return packetPlayInUseEntity;
        }

        if (packetPlayInUseEntity.a() == PacketPlayInUseEntity.EnumEntityUseAction.ATTACK){
            entity.onLeftClick(player);
        } else if (packetPlayInUseEntity.a() == PacketPlayInUseEntity.EnumEntityUseAction.INTERACT
                || packetPlayInUseEntity.a() == PacketPlayInUseEntity.EnumEntityUseAction.INTERACT_AT){

            if (entity instanceof HologramLine){
                HologramLine<?> line = (HologramLine<?>) entity;
                Hologram hologram = line.getParent();
                new HologramEditMenu(plugin, hologram, null).open(player);

                if (player.isSneaking() && player.hasPermission("hologram.edit")){
                    new HologramEditMenu(plugin, hologram, null).open(player);

                    return packetPlayInUseEntity;
                }

            } else if (entity instanceof NPCEntity){
                NPCEntity npc = (NPCEntity) entity;
                if (player.isSneaking() && player.hasPermission("npc.edit")){
                    new NPCEditMenu(npc, plugin).open(player);

                    return packetPlayInUseEntity;
                }
            }

            entity.onRightClick(player);
        }

        return packetPlayInUseEntity;
    }
}
