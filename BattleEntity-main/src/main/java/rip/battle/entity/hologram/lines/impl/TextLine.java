package rip.battle.entity.hologram.lines.impl;

import cc.stormworth.core.util.chat.CC;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.entity.Player;
import rip.battle.entity.hologram.CraftHologram;
import rip.battle.entity.hologram.lines.HologramLine;
import rip.battle.entity.taks.RainbowTask;
import rip.battle.entity.utils.DataWatcherUtil;
import rip.battle.entity.utils.PacketUtils;
import rip.battle.entity.utils.PlayerUtil;

import java.util.Collections;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

@Getter
@Setter
public class TextLine extends HologramLine<String> {

    private Function<CommandSender, String> text;

    private boolean multiLine = false;

    public TextLine(CraftHologram parent, Location location, Function<CommandSender, String> text) {
        this(parent, location, text, null);
    }

    public TextLine(CraftHologram parent, Location location, Function<CommandSender, String> text, Consumer<Player> onClick) {
        this(parent, location, text, onClick, player -> true);
    }
    public TextLine(CraftHologram parent, Location location, Function<CommandSender, String> text, Consumer<Player> onClick, Predicate<Player> shouldSend) {
        super(parent, location, shouldSend, onClick);
        this.text = text;
        setPersistent(false);
    }

    @Override
    public String getLine(Player player) {
        return text.apply(player);
    }

    @Override
    public void setLine(String line) {
        this.text = player -> line;
    }

    @Override
    public void sendSpawnPackets(Player player) {

        boolean legacy = PlayerUtil.isLegacy(player);

        String text = processPlaceholders(getLine(player), player);

        DataWatcher dataWatcher = DataWatcherUtil.createDataWatcher();

        dataWatcher.a(0, (byte) 0);
        dataWatcher.a(1, (short) 0);
        dataWatcher.a(2, text);
        dataWatcher.a(3, text.isEmpty() || text.equalsIgnoreCase("&7") || text.equalsIgnoreCase(" ") ? (byte) 0 : (byte) 1);
        dataWatcher.a(4, (byte) 0);
        if (legacy){
            dataWatcher.a(16, (byte) -2);
        }else{
            DataWatcherUtil.setFlag(dataWatcher, 5, true);

            dataWatcher.a(6, (float) 1);
            dataWatcher.a(7, 0);
            dataWatcher.a(8, (byte) 0);
            dataWatcher.a(9, (byte) 0);

            dataWatcher.a(10, (byte) 0);

            DataWatcherUtil.setTypeFlag(dataWatcher, 10, 1, true);
            DataWatcherUtil.setTypeFlag(dataWatcher, 10, 4, false);
            DataWatcherUtil.setTypeFlag(dataWatcher, 10, 8, false);
            DataWatcherUtil.setTypeFlag(dataWatcher, 10, 10, true);

            dataWatcher.a(11, PacketUtils.ARMOR_STAND_HEAD_POSE);
            dataWatcher.a(12, PacketUtils.ARMOR_STAND_BODY_POSE);
            dataWatcher.a(13, PacketUtils.ARMOR_STAND_LEFT_ARM_POSE);
            dataWatcher.a(14, PacketUtils.ARMOR_STAND_RIGHT_ARM_POSE);
            dataWatcher.a(15, PacketUtils.ARMOR_STAND_LEFT_LEG_POSE);
            dataWatcher.a(16, PacketUtils.ARMOR_STAND_RIGHT_LEG_POSE);
        }

        PacketPlayOutSpawnEntityLiving packet = new PacketPlayOutSpawnEntityLiving(
                getId(),
                legacy ? 55 : 30,
                MathHelper.floor(getLocation().getX() * 32.0D),
                MathHelper.floor(getLocation().getY() * 32.0D),
                MathHelper.floor(getLocation().getZ() * 32.0D),
                dataWatcher
        );

        PacketUtils.sendPacket(player, packet);

        if (legacy){
            EntityWitherSkull skull = new EntityWitherSkull(((CraftWorld) getLocation().getWorld()).getHandle());
            skull.d(getSupportEntityID());
            skull.setLocation(getLocation().getX(), (getLocation().getY() + 3), getLocation().getZ(), 0.0f, 0.0f);
            PacketUtils.sendPacket(player, new PacketPlayOutSpawnEntity(skull, 66));
            PacketUtils.sendPacket(player, new PacketPlayOutAttachEntity(0, getId(), getSupportEntityID()));
        }
    }

    @Override
    public void sendUpdatePackets(Player player) {
        String text = processPlaceholders(getLine(player), player);

        PacketPlayOutEntityMetadata packet = new PacketPlayOutEntityMetadata();

        packet.setEntityId(getId());
        packet.setData(Collections.singletonList(new DataWatcher.WatchableObject(4, 2, text)));

        PacketUtils.sendPacket(player, packet);
    }

    @Override
    public void sendRePositionPackets(Player player) {
        if (PlayerUtil.isLegacy(player)){
            PacketPlayOutEntityTeleport packet = new PacketPlayOutEntityTeleport(
                    getSupportEntityID(),
                    MathHelper.floor(getLocation().getX() * 32.0D),
                    MathHelper.floor((getLocation().getY() + 3) * 32.0D),
                    MathHelper.floor(getLocation().getZ() * 32.0D),
                    (byte) 0,
                    (byte) 0,
                    false
            );

            PacketUtils.sendPacket(player, packet);
        }else{
            super.sendRePositionPackets(player);
        }
    }

    @Override
    public void sendDestroyPackets(Player player) {
        PacketUtils.sendPacket(player, new PacketPlayOutEntityDestroy(getSupportEntityID()));
        PacketUtils.sendPacket(player, new PacketPlayOutEntityDestroy(getId()));
    }

    public String processPlaceholders(String text, Player player) {

        String textFormatted = text;

        if (textFormatted == null) {
            return null;
        }

        textFormatted = textFormatted.replace("{player}", player.getName());
        textFormatted = textFormatted.replace("{player_displayname}", player.getDisplayName());
        textFormatted = textFormatted.replace("{rainbow}", RainbowTask.getCurrentColor().toString());

        return CC.translate(textFormatted);
    }
}
