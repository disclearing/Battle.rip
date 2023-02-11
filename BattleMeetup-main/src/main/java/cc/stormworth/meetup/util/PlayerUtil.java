package cc.stormworth.meetup.util;

import cc.stormworth.core.CorePluginAPI;
import cc.stormworth.core.rank.Rank;
import cc.stormworth.core.util.chat.CC;
import cc.stormworth.meetup.Meetup;
import cc.stormworth.meetup.managers.GameManager;
import cc.stormworth.meetup.managers.KitManager;
import cc.stormworth.meetup.managers.UserManager;
import cc.stormworth.meetup.nms.NMSHelper;
import cc.stormworth.meetup.scenarios.Scenario;
import cc.stormworth.meetup.style.Style;
import cc.stormworth.meetup.style.hcf.kit.Kit;
import cc.stormworth.meetup.user.UserData;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import net.minecraft.server.v1_7_R4.*;
import net.minecraft.util.com.mojang.authlib.GameProfile;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_7_R4.CraftServer;
import org.bukkit.craftbukkit.v1_7_R4.CraftWorld;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.projectiles.ProjectileSource;
import org.bukkit.util.Vector;

import java.util.*;

public class PlayerUtil {

    private static final Map<UUID, Integer> frozenPlayers = new HashMap<>();
    private static int fakeEntityId = -1;

    public static EntityPlayer getNMSPlayer(Player p) {
        return ((CraftPlayer) p).getHandle();
    }

    public static boolean testPermission(Player player, Rank rank) {
        return CorePluginAPI.getProfile(player.getUniqueId()).getRank().getWeight() >= rank.getWeight();
    }

    public static void clearPlayer(Player p) {
        p.setHealth(20.0D);
        p.setFoodLevel(20);
        p.setSaturation(12.8F);
        p.setMaximumNoDamageTicks(20);
        p.setFireTicks(0);
        p.setFallDistance(0.0F);
        p.setLevel(0);
        p.setExp(0.0F);
        p.setWalkSpeed(0.2F);
        p.setFlySpeed(0.1F);
        p.setAllowFlight(false);
        p.getInventory().clear();
        p.getInventory().setArmorContents(null);
        p.closeInventory();
        p.setGameMode(GameMode.SURVIVAL);
        p.getActivePotionEffects().stream().map(PotionEffect::getType).forEach(p::removePotionEffect);
        ((CraftPlayer) p).getHandle().getDataWatcher().watch(9, (byte) 0);
        p.getOpenInventory().getTopInventory().clear();
        p.updateInventory();
    }

    public static void clearPlayer(Player p, GameMode gameMode) {
        p.setHealth(20.0D);
        p.setFoodLevel(20);
        p.setSaturation(12.8F);
        p.setMaximumNoDamageTicks(20);
        p.setFireTicks(0);
        p.setFallDistance(0.0F);
        p.setLevel(0);
        p.setExp(0.0F);
        p.setWalkSpeed(0.2F);
        p.setFlySpeed(0.1F);
        p.setAllowFlight(false);
        p.getInventory().clear();
        p.getInventory().setArmorContents(null);
        p.closeInventory();
        p.setGameMode(gameMode);
        p.getActivePotionEffects().stream().map(PotionEffect::getType).forEach(p::removePotionEffect);
        ((CraftPlayer) p).getHandle().getDataWatcher().watch(9, Byte.valueOf((byte) 0));
        p.getOpenInventory().getTopInventory().clear();
        p.updateInventory();
    }

    public static void scatterPlayer(Player player) {
        player.teleport(LocationUtil.getRandomScatterLocation(Bukkit.getWorld("game_world"),
                GameManager.getInstance().getBorder().getSize()));

        clearPlayer(player);
        freezePlayer(player);

        UserData user = UserManager.getInstance().getUser(player.getUniqueId());
        user.getStatistics().setGamesPlayed(user.getStatistics().getGamesPlayed() + 1);

        if (GameManager.getInstance().getStyle() == Style.UHC) {
            if (Scenario.getByName("OP Kits").isActive()) {
                KitManager.getInstance().giveKitItemContainerToPlayer(KitManager.getInstance().getRandomOPKit(), player);
            } else {
                KitManager.getInstance().giveKitItemContainerToPlayer(user.getKitItemContainer(), player);
                player.getInventory().addItem(new ItemStack(Material.EXP_BOTTLE, 32));
            }
        } else {
            Kit kit = Kit.findAny();

            kit.apply(player);
        }
    }

    public static void scatterPlayer(Player player, Location scatterLocation) {
        player.teleport(scatterLocation);

        clearPlayer(player);
        freezePlayer(player);

        UserData user = UserManager.getInstance().getUser(player.getUniqueId());
        user.getStatistics().setGamesPlayed(user.getStatistics().getGamesPlayed() + 1);

        if (GameManager.getInstance().getStyle() == Style.UHC) {
            if (Scenario.getByName("OP Kits").isActive()) {
                KitManager.getInstance().giveKitItemContainerToPlayer(KitManager.getInstance().getRandomOPKit(), player);
            } else {
                KitManager.getInstance().giveKitItemContainerToPlayer(user.getKitItemContainer(), player);
            }
        } else {
            Kit kit = Kit.findAny();

            kit.apply(player);
        }
    }

    public static void freezePlayer(Player p) {
        Block block = p.getLocation().getBlock();
        Location center = block.getLocation().add((block.getLocation().getX() < 0.0) ? -0.5 : 0.5, 0.0,
                (block.getLocation().getZ() < 0.0) ? -0.5 : 0.5);

        EntityBat bat = new EntityBat(((CraftWorld) p.getWorld()).getHandle());
        bat.setPosition(center.getX(), center.getY(), center.getZ());
        bat.setInvisible(true);
        bat.setHealth(20.0F);

        frozenPlayers.put(p.getUniqueId(), bat.getId());

        EntityPlayer entityPlayer = ((CraftPlayer) p).getHandle();
        entityPlayer.playerConnection.sendPacket(new PacketPlayOutSpawnEntityLiving(bat));
        entityPlayer.playerConnection.sendPacket(new PacketPlayOutAttachEntity(0, entityPlayer, bat));
    }

    private static void unfreezePlayer(Player p) {

        if (!frozenPlayers.containsKey(p.getUniqueId())) {
            return;
        }

        PacketPlayOutEntityDestroy destroyPacket = new PacketPlayOutEntityDestroy(frozenPlayers.get(p.getUniqueId()));
        sendPacket(p, destroyPacket);
        frozenPlayers.remove(p.getUniqueId());
        p.closeInventory();
    }

    public static void unfreezeAll() {
        Set<UUID> uuids = new HashSet<>(frozenPlayers.keySet());

        for (UUID uuid : uuids) {
            Player p = Bukkit.getPlayer(uuid);

            if (p == null) {
                frozenPlayers.remove(uuid);
            } else {
                unfreezePlayer(p);
            }
        }
    }

    public static void randomTeleport(Player p) {
        List<Player> players = new ArrayList<>();

        for (Player all : Bukkit.getOnlinePlayers()) {
            UserData user = UserManager.getInstance().getUser(all.getUniqueId());

            if (user.isAlive()) {
                players.add(all);
            }
        }

        if (players.size() == 0) {
            p.sendMessage(Colors.RED + "No player found.");
            return;
        }

        Collections.shuffle(players);
        Player target = players.get(0);
        p.teleport(target);
        p.sendMessage(Colors.SECONDARY + "You have been teleported to " + Colors.PRIMARY + target.getName()
                + Colors.SECONDARY + ".");
    }

    public static int getFakeEntityId() {
        return fakeEntityId--;
    }

    public static void animateDeath(Player p) {
        final Location location = p.getLocation();

        p.setVelocity(new Vector(0, 1, 0));

        MinecraftServer server = ((CraftServer) Bukkit.getServer()).getServer();
        WorldServer world = ((CraftWorld) location.getWorld()).getHandle();
        GameProfile gameProfile = new GameProfile(UUID.randomUUID(),
                Colors.GRAY + (p.getName().length() > 14 ? p.getName().substring(0, 13) : p.getName()));
        gameProfile.getProperties().putAll(getNMSPlayer(p).getProfile().getProperties());
        EntityPlayer fakePlayer = new EntityPlayer(server, world, gameProfile, new PlayerInteractManager(world));
        fakePlayer.setLocation(location.getBlockX() + 0.5, location.getY() + 0.15, location.getBlockZ() + 0.5,
                location.getYaw() * 256 / 360, location.getPitch());

        PacketPlayOutPlayerInfo tabAddPacket = PacketPlayOutPlayerInfo.addPlayer(fakePlayer);
        PacketPlayOutNamedEntitySpawn spawnPacket = new PacketPlayOutNamedEntitySpawn(fakePlayer);
        PacketPlayOutEntityHeadRotation headRotationPacket = new PacketPlayOutEntityHeadRotation(fakePlayer,
                (byte) (fakePlayer.yaw * 256 / 360));
        PacketPlayOutPlayerInfo tabRemovePacket = PacketPlayOutPlayerInfo.removePlayer(fakePlayer);
        PacketPlayOutEntityStatus deathAnimationPacket = new PacketPlayOutEntityStatus();

        int entityId = getFakeEntityId();

        try {
            NMSHelper.setValueStatic(spawnPacket, "a", entityId);
            NMSHelper.setValueStatic(deathAnimationPacket, "a", Integer.valueOf(entityId));
            NMSHelper.setValueStatic(deathAnimationPacket, "b", Byte.valueOf((byte) 3));

            int radius = MinecraftServer.getServer().getPlayerList().d();
            Set<UUID> sentTo = new HashSet<>();

            for (org.bukkit.entity.Entity entity : p.getNearbyEntities(radius, radius, radius)) {

                if (!(entity instanceof Player))
                    continue;

                Player watcher = (Player) entity;

                if (watcher.getUniqueId().equals(p.getUniqueId()))
                    continue;

                // Spawn the fake player:
                sendPacket(watcher, tabAddPacket);
                sendPacket(watcher, spawnPacket);
                sendPacket(watcher, headRotationPacket);
                sendPacket(watcher, tabRemovePacket);

                TaskUtil.runLater(() -> {
                    sendPacket(watcher, deathAnimationPacket);
                    sentTo.add(entity.getUniqueId());
                }, 3L);
            }

            TaskUtil.runLater(() -> {
                for (UUID uuidParsed : sentTo) {
                    Player target = Bukkit.getPlayer(uuidParsed);

                    if (target == null) continue;

                    sendPacket(target, new PacketPlayOutEntityDestroy(entityId));
                }
            }, 18L);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static Player getAttacker(EntityDamageEvent entityDamageEvent, boolean ignoreSelf) {
        Player attacker = null;
        if (!(entityDamageEvent instanceof EntityDamageByEntityEvent))
            return null;

        Projectile projectile;
        ProjectileSource shooter;
        EntityDamageByEntityEvent event = (EntityDamageByEntityEvent) entityDamageEvent;
        org.bukkit.entity.Entity damager = event.getDamager();
        if (event.getDamager() instanceof Player)
            attacker = (Player) damager;
        else if (event.getDamager() instanceof Projectile
                && (shooter = ((Projectile) damager).getShooter()) instanceof Player)
            attacker = (Player) shooter;

        if (attacker != null && ignoreSelf && event.getEntity().equals(attacker))
            attacker = null;

        return attacker;
    }

    public static void sendPacket(Packet packet) {

        for (Player all : Bukkit.getOnlinePlayers()) {
            ((CraftPlayer) all).getHandle().playerConnection.sendPacket(packet);
        }
    }

    public static void sendPacket(Player p, Packet packet) {
        ((CraftPlayer) p).getHandle().playerConnection.sendPacket(packet);
    }

    public static void connect(Player p, String serverName) {
        sendPluginMessage(p, "Connect", serverName);
    }

    private static void sendPluginMessage(Player p, String subchannel, String server) {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF(subchannel);
        out.writeUTF(server);

        p.sendPluginMessage(Meetup.getInstance(), "BungeeCord", out.toByteArray());
    }
}
