package rip.battle.entity;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.server.v1_8_R3.PacketPlayOutAnimation;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityDestroy;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityStatus;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityTeleport;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import rip.battle.entity.hologram.api.Hologram;
import rip.battle.entity.utils.AngleUtils;
import rip.battle.entity.utils.PacketUtils;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@Getter @Setter
public abstract class Entity {

    private static int ENTITY_ID = -1337;
    private final int id;
    private final UUID uuid;
    private Location location;

    private boolean initialized = false;
    private boolean hidden = false;

    private boolean persistent = true;

    private boolean root = true;

    private Set<UUID> currentWatchers = Sets.newHashSet();
    private Set<UUID> hiddeFor = Sets.newHashSet();

    private Set<String> commands = Sets.newHashSet();

    private Consumer<Player> onLeftClick;
    private Consumer<Player> onRightClick;

    public Entity(Location location) {
        this.id = nextId();
        this.location = location;
        this.uuid = UUID.randomUUID();

        EntityManager.addEntity(this);
    }

    public abstract String getTypeName();

    public void initializeData() {
        this.initialized = true;
    }

    public void onDelete(){
        destroyForCurrentWatchers();

        EntityManager.removeEntity(this);
    }

    public void updateLocation(Location location) {
        this.location = location;

        getCurrentWatchersPlayers().forEach(this::sendRePositionPackets);
    }

    public Hologram getAttachedHologram(){ //Add HologramEntity
        return null;
    }

    public boolean isVisible() {
        return !hidden;
    }

    public boolean isVisibleTo(Player player) {
        return !hidden && location.getWorld() == player.getWorld() && location.distance(player.getLocation()) <= 32.0 && !hiddeFor.contains(player.getUniqueId());
    }

    public void updateVisibility(boolean hidden){
        boolean hasChanged = this.hidden != hidden;

        this.hidden = hidden;

        if(hasChanged){
            if(hidden){
                destroyForCurrentWatchers();
            }else{

                Bukkit.getOnlinePlayers().forEach(player -> {
                    if (!currentWatchers.contains(player.getUniqueId()) && isVisibleTo(player)) {
                        currentWatchers.add(player.getUniqueId());
                    }
                });

                spawnForCurrentWatchers();
            }
        }

        if (getAttachedHologram() != null) {
            getAttachedHologram().updateVisibility(hidden);
        }
    }

    public void updateVisibilityFor(Player player, boolean hidden) {
        boolean hasChanged = currentWatchers.contains(player.getUniqueId()) != hidden;

        if(hasChanged){

            if(hidden){
                hiddeFor.add(player.getUniqueId());
                currentWatchers.remove(player.getUniqueId());
            }else{
                currentWatchers.add(player.getUniqueId());
            }

            if(hidden) {
                sendDestroyPackets(player);
            }else {
                sendSpawnPackets(player);
            }

            if (getAttachedHologram() != null) {
                getAttachedHologram().updateVisibilityFor(player, hidden);
            }
        }
    }

    public void spawnForCurrentWatchers() {
        getCurrentWatchersPlayers().stream().filter(this::isVisibleTo).forEach(this::sendSpawnPackets);
    }

    public void onLeftClick(Player player) {
        if (!commands.isEmpty()) {
            for (String command : commands) {
                player.performCommand(command.replace("{player}", player.getName()));
            }
        }else if (onLeftClick != null) {
            onLeftClick.accept(player);
        }
    }

    public void onRightClick(Player player) {
        if (onRightClick != null) {
            onRightClick.accept(player);
        }
    }

    public void spawn(){
        Bukkit.getOnlinePlayers().forEach(player -> {
            if(isVisibleTo(player)){
                spawn(player);
            }
        });
    }

    public boolean isDamageable() {
        return false;
    }

    public void spawn(Player player){

        /*if (currentWatchers.contains(player.getUniqueId())) {
            return;
        }*/

        currentWatchers.add(player.getUniqueId());

        sendSpawnPackets(player);
        sendUpdatePackets(player);
    }

    public void update(Player player){

        if (!currentWatchers.contains(player.getUniqueId())) {
            return;
        }

        sendUpdatePackets(player);
    }

    public void destroy(Player player) {
        currentWatchers.remove(player.getUniqueId());

        sendDestroyPackets(player);
    }

    public boolean hasAnyWatchers() {
        return currentWatchers.size() > 0;
    }

    public List<Player> getCurrentWatchersPlayers() {
        return currentWatchers.stream().map(Bukkit::getPlayer).filter(Objects::nonNull).collect(Collectors.toList());
    }

    public boolean isWatcher(Player player){
        return currentWatchers.contains(player.getUniqueId());
    }

    public void updateForCurrentWatchers() {
        for (Player player : getCurrentWatchersPlayers()) {
            update(player);
        }
    }

    public void destroyForCurrentWatchers() {
        for (Player player : getCurrentWatchersPlayers()) {
            sendDestroyPackets(player);
        }
    }

    public void resendForCurrentWatchers() {
        for (Player player : getCurrentWatchersPlayers()) {
            destroy(player);
            spawn(player);
        }
    }

    public boolean isMultiPartEntity(){
        return false;
    }

    public boolean isRootOfMultiPartEntity(){
        return root;
    }

    public void sendSpawnPackets(Player player) {}

    public void sendDestroyPackets(Player player) {
        PacketUtils.sendPacket(player, new PacketPlayOutEntityDestroy(id));
    }

    public void sendUpdatePackets(Player player) {}

    public void sendRePositionPackets(Player player) {

        PacketPlayOutEntityTeleport playOutEntityTeleport = new PacketPlayOutEntityTeleport(
                this.id,
                (int) (this.location.getX() * 32.0D),
                (int) (this.location.getY() * 32.0D),
                (int) (this.location.getZ() * 32.0D),
                AngleUtils.yawToBytes(location.getYaw()),
                AngleUtils.yawToBytes(location.getPitch()),
                false
        );

        PacketUtils.sendPacket(player, playOutEntityTeleport);
    }

    public void sendStatusPacket(byte status){
        PacketPlayOutEntityStatus packet = new PacketPlayOutEntityStatus(id, status);

        PacketUtils.sendPacket(getCurrentWatchersPlayers(), packet);
    }

    public void sendAnimationPacket(int animation){
        PacketPlayOutAnimation packet = new PacketPlayOutAnimation(id, animation);

        PacketUtils.sendPacket(getCurrentWatchersPlayers(), packet);
    }

    public static int nextId() {
        return ENTITY_ID--;
    }

}
