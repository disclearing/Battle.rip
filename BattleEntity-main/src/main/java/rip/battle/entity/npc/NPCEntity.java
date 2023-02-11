package rip.battle.entity.npc;

import cc.stormworth.core.util.TaskUtil;
import cc.stormworth.core.util.chat.CC;
import cc.stormworth.core.util.skin.SkinTexture;
import com.google.common.collect.Lists;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.craftbukkit.v1_8_R3.scoreboard.CraftScoreboard;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.java.JavaPlugin;
import rip.battle.entity.Entity;
import rip.battle.entity.EntityPlugin;
import rip.battle.entity.hologram.CraftHologram;
import rip.battle.entity.hologram.api.Hologram;
import rip.battle.entity.npc.api.NPC;
import rip.battle.entity.utils.AngleUtils;
import rip.battle.entity.utils.DataWatcherUtil;
import rip.battle.entity.utils.PacketUtils;
import rip.battle.entity.utils.PlayerUtil;

import java.util.*;

@Getter @Setter
public class NPCEntity extends Entity implements NPC {

    private final UUID uuid;
    private String name;
    private String displayName;

    private GameProfile gameProfile;

    private SkinTexture skin;

    private CraftHologram hologram;

    private boolean hidden;

    private ItemStack itemInHand;

    private ItemStack helmet;
    private ItemStack chestplate;
    private ItemStack leggings;
    private ItemStack boots;

    private List<String> messages = Lists.newArrayList();

    private int supportEntityID;

    public NPCEntity(String name, String displayName, Location location) {
        super(location);
        this.name = name;
        this.displayName = CC.translate(displayName);
        this.uuid = UUID.randomUUID();

        this.gameProfile = new GameProfile(uuid, displayName);
    }

    public NPCEntity(String name, Location location) {
        this(name, name, location);
    }

    @Override
    public String getTypeName() {
        return "NPC";
    }

    @Override
    public Hologram getAttachedHologram() {
        return hologram;
    }

    @Override
    public void onRightClick(Player player) {

        super.onRightClick(player);

        if (!getCommands().isEmpty()){
            getCommands().forEach(command -> player.performCommand(command.replace("%player%", player.getName())));
        }

        if (!messages.isEmpty()) {
            messages.forEach(player::sendMessage);
        }
    }

    @Override
    public void sendSpawnPackets(Player player) {

        DataWatcher dataWatcher = DataWatcherUtil.createNPCDataWatcher(this);

        PacketPlayOutPlayerInfo addPlayerPacket = new PacketPlayOutPlayerInfo(
                PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER,
                gameProfile,
                0,
                WorldSettings.EnumGamemode.SURVIVAL,
                null);

        PacketPlayOutPlayerInfo removePlayerPacket = new PacketPlayOutPlayerInfo(
                PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER,
                gameProfile,
                0,
                WorldSettings.EnumGamemode.SURVIVAL,
                null);

        PacketPlayOutNamedEntitySpawn spawnPacket = new PacketPlayOutNamedEntitySpawn(
                getId(),
                uuid,
                MathHelper.floor(getLocation().getX() * 32.0),
                MathHelper.floor(getLocation().getY() * 32.0),
                MathHelper.floor(getLocation().getZ() * 32.0),
                AngleUtils.yawToBytes(getLocation().getYaw()),
                AngleUtils.yawToBytes(getLocation().getPitch()),
                0,
                dataWatcher
        );

        PacketPlayOutEntity.PacketPlayOutEntityLook lookPacket = new PacketPlayOutEntity.PacketPlayOutEntityLook(
                getId(),
                AngleUtils.yawToBytes(getLocation().getYaw()),
                AngleUtils.yawToBytes(getLocation().getPitch()),
                true
        );

        PacketPlayOutEntityHeadRotation headRotationPacket = new PacketPlayOutEntityHeadRotation(
                getId(),
                AngleUtils.yawToBytes(getLocation().getYaw())
        );

        PacketPlayOutEntityTeleport teleportPacket = new PacketPlayOutEntityTeleport(
                getId(),
                MathHelper.floor(getLocation().getX() * 32.0),
                MathHelper.floor(getLocation().getY() * 32.0),
                MathHelper.floor(getLocation().getZ() * 32.0),
                AngleUtils.yawToBytes(getLocation().getYaw()),
                AngleUtils.yawToBytes(getLocation().getPitch()),
                true
        );

        PacketUtils.sendPacket(player, addPlayerPacket);

        TaskUtil.runLater(JavaPlugin.getPlugin(EntityPlugin.class), () -> {

            PacketUtils.sendPacket(player, spawnPacket);

            if (itemInHand != null) {
                PacketUtils.sendPacket(player, new PacketPlayOutEntityEquipment(getId(), 0, CraftItemStack.asNMSCopy(itemInHand)));
            }

            if (helmet != null) {
                PacketUtils.sendPacket(player, new PacketPlayOutEntityEquipment(getId(), 4, CraftItemStack.asNMSCopy(helmet)));
            }

            if (chestplate != null) {
                PacketUtils.sendPacket(player, new PacketPlayOutEntityEquipment(getId(), 3, CraftItemStack.asNMSCopy(chestplate)));
            }

            if (leggings != null) {
                PacketUtils.sendPacket(player, new PacketPlayOutEntityEquipment(getId(), 2, CraftItemStack.asNMSCopy(leggings)));
            }

            if (boots != null) {
                PacketUtils.sendPacket(player, new PacketPlayOutEntityEquipment(getId(), 1, CraftItemStack.asNMSCopy(boots)));
            }

            TaskUtil.runLater(JavaPlugin.getPlugin(EntityPlugin.class), () -> {
                PacketUtils.sendPacket(player, lookPacket);
                PacketUtils.sendPacket(player, headRotationPacket);
                PacketUtils.sendPacket(player, teleportPacket);
                PacketUtils.sendPacket(player, new PacketPlayOutAnimation(getId(), 0));
                PacketUtils.sendPacket(player, removePlayerPacket);

                if (hologram != null) {
                    hideNamePlate(player);
                }

            }, 4L);

            if (hologram != null){
                hologram.spawnFor(player);
            }

        }, 1L);
    }

    @Override
    public void sendUpdatePackets(Player player) {

        PacketPlayOutEntityMetadata packet = new PacketPlayOutEntityMetadata();

        packet.setEntityId(getId());
        packet.setData(Collections.singletonList(new DataWatcher.WatchableObject(4, 2, displayName)));

        PacketUtils.sendPacket(player, packet);
    }

    @Override
    public void updateLocation(Location location) {
        super.updateLocation(location);

        if (hologram != null){
            hologram.updateLocation(calculateHologramLocation());
        }

        getCurrentWatchersPlayers().forEach(player -> {

            PacketUtils.sendPacket(player, new PacketPlayOutEntityHeadRotation(
                    getId(),
                    AngleUtils.yawToBytes(getLocation().getYaw())
            ));

            PacketUtils.sendPacket(player, new PacketPlayOutAnimation(
                    getId(),
                    0
            ));
        });
    }

    public Location calculateHologramLocation(){
        return getLocation().clone().subtract(0, 0.2, 0);
    }

    public void setSkin(SkinTexture skin){
        this.skin = skin;

        if (skin != null) {
            gameProfile = new GameProfile(uuid, displayName);

            gameProfile.getProperties().put("textures", new Property("textures", skin.getValue(), skin.getSignature()));
        }else {
            gameProfile = new GameProfile(uuid, displayName);

            gameProfile.getProperties().clear();
        }

        resendForCurrentWatchers();
    }

    @Override
    public void destroy() {
        onDelete();
    }

    @Override
    public void onDelete() {
        super.onDelete();

        if (hologram != null){
            hologram.destroy();
        }
    }

    @Override
    public void teleport(double x, double y, double z) {
        teleport(new Location(getLocation().getWorld(), x, y, z, getLocation().getYaw(), getLocation().getPitch()));
    }

    @Override
    public void teleport(double x, double y, double z, float yaw, float pitch) {
        teleport(new Location(getLocation().getWorld(), x, y, z, yaw, pitch));
    }

    public void teleport(Location location) {
        updateLocation(location);
    }

    @Override
    public void teleport(Location location, float yaw, float pitch) {

        Location newLocation = location.clone();

        newLocation.setYaw(yaw);
        newLocation.setPitch(pitch);

        updateLocation(newLocation);
    }

    protected void updateTexture(SkinTexture skin){
        this.skin = skin;

        if (skin != null) {
            gameProfile = new GameProfile(uuid, displayName);

            gameProfile.getProperties().put("textures", new Property("textures", skin.getValue(), skin.getSignature()));

            for (Player player : getCurrentWatchersPlayers()) {
                destroy(player);
                spawn(player);
            }
        }
    }

    public void updateItemInHand(ItemStack itemInHand) {
        this.itemInHand = itemInHand;

        for (Player player : getCurrentWatchersPlayers()){
            PacketUtils.sendPacket(player, new PacketPlayOutEntityEquipment(getId(), 0, CraftItemStack.asNMSCopy(itemInHand == null ? new ItemStack(Material.AIR) : itemInHand)));
        }
    }

    public void setHelmet(ItemStack helmet) {
        this.helmet = helmet;

        for (Player player : getCurrentWatchersPlayers()){
            PacketUtils.sendPacket(player, new PacketPlayOutEntityEquipment(getId(), 4, CraftItemStack.asNMSCopy(helmet == null ? new ItemStack(Material.AIR) : helmet)));
        }
    }

    public void setChestplate(ItemStack chestplate) {
        this.chestplate = chestplate;

        for (Player player : getCurrentWatchersPlayers()){
            PacketUtils.sendPacket(player, new PacketPlayOutEntityEquipment(getId(), 3, CraftItemStack.asNMSCopy(chestplate == null ? new ItemStack(Material.AIR) : chestplate)));
        }
    }

    public void setLeggings(ItemStack leggings) {
        this.leggings = leggings;

        for (Player player : getCurrentWatchersPlayers()){
            PacketUtils.sendPacket(player, new PacketPlayOutEntityEquipment(getId(), 2, CraftItemStack.asNMSCopy(leggings == null ? new ItemStack(Material.AIR) : leggings)));
        }
    }

    public void setBoots(ItemStack boots) {
        this.boots = boots;

        for (Player player : getCurrentWatchersPlayers()){
            PacketUtils.sendPacket(player, new PacketPlayOutEntityEquipment(getId(), 1, CraftItemStack.asNMSCopy(boots == null ? new ItemStack(Material.AIR) : boots)));
        }
    }

    public void setHologram(Hologram hologram) {
        this.hologram = (CraftHologram) hologram;

        if (hologram != null){
            this.hologram.updateLocation(calculateHologramLocation());
            ((CraftHologram) hologram).setParent(this);

            for (Player player : getCurrentWatchersPlayers()){
                hideNamePlate(player);
                this.hologram.spawnFor(player);
            }
        }else{
            for (Player player : getCurrentWatchersPlayers()){
                showNamePlate(player);
            }
        }

        updateForCurrentWatchers();
    }

    protected void hideNamePlate(Player player){
       if (PlayerUtil.isLegacy(player)){
           if (supportEntityID == 0){
               supportEntityID = nextId();
               DataWatcher slimeDatawatcher = DataWatcherUtil.createDataWatcher();

               slimeDatawatcher.a(0, (byte) 0);
               slimeDatawatcher.a(1, (short) 0);
               slimeDatawatcher.a(2, "");
               slimeDatawatcher.a(3, (byte) 0);
               slimeDatawatcher.a(4, (byte) 0);
               slimeDatawatcher.a(16, (byte) -2);

               PacketPlayOutSpawnEntityLiving packet = new PacketPlayOutSpawnEntityLiving(
                       supportEntityID,
                       55,
                       MathHelper.floor(getLocation().getX() * 32.0D),
                       MathHelper.floor(getLocation().getY() * 32.0D),
                       MathHelper.floor(getLocation().getZ() * 32.0D),
                       slimeDatawatcher
               );

               PacketUtils.sendPacket(player, packet);
           }
           PacketUtils.sendPacket(player, new PacketPlayOutAttachEntity(0, supportEntityID, getId()));
       }else {
           ScoreboardTeam team = new ScoreboardTeam(((CraftScoreboard) Bukkit.getScoreboardManager().getMainScoreboard()).getHandle(),
                   getDisplayName());

           team.setNameTagVisibility(ScoreboardTeamBase.EnumNameTagVisibility.NEVER);

           PacketUtils.sendPacket(player, new PacketPlayOutScoreboardTeam(team, 1));
           PacketUtils.sendPacket(player, new PacketPlayOutScoreboardTeam(team, 0));
           PacketUtils.sendPacket(player, new PacketPlayOutScoreboardTeam(team, new ArrayList<String>(){{add(getDisplayName());}}, 3));
       }
    }

    protected void showNamePlate(Player player){
        if (PlayerUtil.isLegacy(player)){
            PacketUtils.sendPacket(player, new PacketPlayOutAttachEntity(0, supportEntityID, 0));
            PacketUtils.sendPacket(player, new PacketPlayOutEntityDestroy(supportEntityID));
            supportEntityID = 0;
        }else {
            ScoreboardTeam team = new ScoreboardTeam(((CraftScoreboard) Bukkit.getScoreboardManager().getMainScoreboard()).getHandle(),
                    getDisplayName());

            team.setNameTagVisibility(ScoreboardTeamBase.EnumNameTagVisibility.ALWAYS);

            PacketUtils.sendPacket(player, new PacketPlayOutScoreboardTeam(team, 1));
            PacketUtils.sendPacket(player, new PacketPlayOutScoreboardTeam(team, 0));
            PacketUtils.sendPacket(player, new PacketPlayOutScoreboardTeam(team, new ArrayList<String>(){{add(getDisplayName());}}, 3));
        }
    }

    @Override
    public void sendDestroyPackets(Player player) {
        PacketUtils.sendPacket(player, new PacketPlayOutEntityDestroy(supportEntityID));
        super.sendDestroyPackets(player);
    }

    @Override
    public void setArmor(Player player) {
        PlayerInventory inventory = player.getInventory();

        setHelmet(inventory.getHelmet());
        setChestplate(inventory.getChestplate());
        setLeggings(inventory.getLeggings());
        setBoots(inventory.getBoots());
    }

    @Override
    public void setArmor(ItemStack[] armor) {
        setHelmet(armor[3]);
        setChestplate(armor[2]);
        setLeggings(armor[1]);
        setBoots(armor[0]);
    }

    @Override
    public void setArmor(ItemStack helmet, ItemStack chestplate, ItemStack leggings, ItemStack boots) {
        setHelmet(helmet);
        setChestplate(chestplate);
        setLeggings(leggings);
        setBoots(boots);
    }

    @Override
    public void setItemInHand(ItemStack itemInHand) {
        this.itemInHand = itemInHand;

        updateItemInHand(itemInHand);
    }

    @Override
    public void setDisplayName(String displayName) {
        this.displayName = CC.translate(displayName);

        this.gameProfile = new GameProfile(uuid, CC.translate(displayName));

        if (skin != null) {
            gameProfile.getProperties().put("textures", new Property("textures", skin.getValue(), skin.getSignature()));
        }

        resendForCurrentWatchers();
    }
}
