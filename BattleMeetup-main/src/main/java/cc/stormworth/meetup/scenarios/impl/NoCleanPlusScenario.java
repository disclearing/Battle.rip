package cc.stormworth.meetup.scenarios.impl;

import cc.stormworth.core.CorePlugin;
import cc.stormworth.core.CorePluginAPI;
import cc.stormworth.core.util.chat.CC;
import cc.stormworth.meetup.Meetup;
import cc.stormworth.meetup.managers.GameManager;
import cc.stormworth.meetup.managers.TeamManager;
import cc.stormworth.meetup.managers.UserManager;
import cc.stormworth.meetup.scenarios.Scenario;
import cc.stormworth.meetup.states.Mode;
import cc.stormworth.meetup.team.Team;
import cc.stormworth.meetup.user.UserData;
import cc.stormworth.meetup.util.Colors;
import cc.stormworth.meetup.util.Cooldown;
import cc.stormworth.meetup.util.PlayerUtil;
import cc.stormworth.meetup.util.TaskUtil;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.Cancellable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

public class NoCleanPlusScenario extends Scenario implements Listener {

    @Getter
    private static Map<UUID, String> USERS;

    private static Map<UUID, Long> DND_COOLDOWN;

    private static int TASK;

    public NoCleanPlusScenario() {
        super("NoClean+", new ItemStack(Material.DIAMOND_SWORD), false, "Upon hitting a player, you are linked to them for 30 seconds.", "You are invincible for 30 seconds upon a kill", "and loot is put into a locked chest.");
    }

    public static void start() {
        USERS = new HashMap<>();
        DND_COOLDOWN = new HashMap<>();

        TASK = (new BukkitRunnable() {
            public void run() {
                if (NoCleanPlusScenario.USERS.isEmpty())
                    return;
                Iterator<Map.Entry<UUID, Long>> iterator = NoCleanPlusScenario.DND_COOLDOWN.entrySet().iterator();
                while (iterator.hasNext()) {
                    Map.Entry<UUID, Long> next = iterator.next();
                    UUID uuid = next.getKey();
                    if (uuid == null)
                        continue;
                    long time = next.getValue().longValue();
                    if (time - System.currentTimeMillis() <= 0L) {
                        String name = NoCleanPlusScenario.USERS.remove(uuid);
                        if (name != null) {
                            Player player = Bukkit.getPlayer(name);
                            if (player != null) {
                                if (NoCleanPlusScenario.DND_COOLDOWN.get(player.getUniqueId()) != null) {
                                    player.sendMessage(CC.SECONDARY + "You can now fight with other players.");
                                }
                                Player damager = Bukkit.getPlayer(uuid);
                                if (damager != null) {
                                    CorePlugin.getInstance().getNametagEngine().reloadPlayer(player);
                                    CorePlugin.getInstance().getNametagEngine().reloadOthersFor(damager);
                                    CorePlugin.getInstance().getNametagEngine().reloadPlayer(damager);
                                    CorePlugin.getInstance().getNametagEngine().reloadOthersFor(player);
                                }
                            }
                            iterator.remove();
                        }
                    }
                }
            }
        }).runTaskTimerAsynchronously(Meetup.getInstance(), 0L, 20L).getTaskId();
    }

    public static boolean isDisturbActive(UUID user) {
        return (DND_COOLDOWN.containsKey(user) && System.currentTimeMillis() < DND_COOLDOWN.get(user).longValue());
    }

    public static long getDisturbMillisecondsLeft(UUID user) {
        return DND_COOLDOWN.containsKey(user) ? Math.max(DND_COOLDOWN.get(user).longValue() - System.currentTimeMillis(), 0L) : 0L;
    }

    public static void cleanup() {
        USERS.clear();
        USERS = null;
        DND_COOLDOWN.clear();
        DND_COOLDOWN = null;
        UserManager.getInstance().getUsers().forEach(data -> {
            if (data.isInvincible()) {
                data.setInvincibilityTimer(new Cooldown(0));
                data.setInvincible(false);
            }
        });
        Bukkit.getScheduler().cancelTask(TASK);
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerDeathEvent(PlayerDeathEvent event) {

        if (!(event.getEntity().getKiller() != null || event.getEntity().getKiller() != null)) return;

        Player player = event.getEntity().getKiller();
        UserData uhcPlayer = UserManager.getInstance().getUser(player.getUniqueId());

        if (!uhcPlayer.isAlive()) {
            return;
        }

        Cooldown cooldown = new Cooldown(30);
        uhcPlayer.setInvincibilityTimer(cooldown);
        uhcPlayer.setInvincible(true);

        player.sendMessage(CC.SECONDARY + "You are now invincible for " + CC.PRIMARY + "30 seconds" + CC.SECONDARY + ".");

        CorePlugin.getInstance().getNametagEngine().reloadPlayer(player);

        TaskUtil.runLater(() -> {

            if (player != null) {

                if (uhcPlayer.isInvincible()) {
                    player.sendMessage(CC.SECONDARY + "You are no longer invincible.");
                    uhcPlayer.setInvincibilityTimer(new Cooldown(0));
                    uhcPlayer.setInvincible(false);
                    CorePlugin.getInstance().getNametagEngine().reloadPlayer(player);
                }
            }
        }, 30 * 20L);
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onEntityDamage(EntityDamageEvent event) {
        Entity entity = event.getEntity();

        if (!(entity instanceof Player)) return;
        if (event.getCause() == EntityDamageEvent.DamageCause.SUICIDE) return;

        final Player player = (Player) event.getEntity();
        UserData uhcPlayer = UserManager.getInstance().getUser(player.getUniqueId());

        if (!uhcPlayer.isAlive()) {
            return;
        }

        Player target = PlayerUtil.getAttacker(event, true);

        if (target != null) {
            UserData uhcPlayerTarget = UserManager.getInstance().getUser(target.getUniqueId());

            if (uhcPlayerTarget.isInvincible() && uhcPlayerTarget.isAlive()) {
                uhcPlayerTarget.setInvincibilityTimer(new Cooldown(0));
                uhcPlayerTarget.setInvincible(false);
                target.sendMessage(CC.SECONDARY + "You are no longer invincible.");
                CorePlugin.getInstance().getNametagEngine().reloadPlayer(player);
            }
        }

        if (uhcPlayer.isInvincible()) {

            if (target != null) {
                target.sendMessage(CC.RED + "This player has an invincibility timer.");
            }

            event.setCancelled(true);
            return;
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerBucketEmptyEvent(PlayerBucketEmptyEvent event) {
        Player player = event.getPlayer();
        UserData uhcPlayer = UserManager.getInstance().getUser(player.getUniqueId());

        if (!uhcPlayer.isAlive()) {
            return;
        }

        if (event.getBucket() == Material.LAVA_BUCKET && uhcPlayer.isInvincible()) {
            uhcPlayer.setInvincibilityTimer(new Cooldown(0));
            uhcPlayer.setInvincible(false);
            player.sendMessage(CC.SECONDARY + "You are no longer invincible.");
            CorePlugin.getInstance().getNametagEngine().reloadPlayer(player);
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        UserData uhcPlayer = UserManager.getInstance().getUser(player.getUniqueId());

        if (!uhcPlayer.isAlive()) {
            return;
        }

        if (event.getBlock().getType() == Material.FIRE || event.getBlock().getType() == Material.TNT) {

            if (uhcPlayer.isInvincible()) {
                uhcPlayer.setInvincibilityTimer(new Cooldown(0));
                uhcPlayer.setInvincible(false);
                player.sendMessage(CC.SECONDARY + "You are no longer invincible.");
                CorePlugin.getInstance().getNametagEngine().reloadPlayer(player);
            }
        }
    }

    @EventHandler
    public void onEntityDamageByEntityDnD(EntityDamageByEntityEvent event) {

        if (GameManager.getInstance().isLobby()) {
            return;
        }

        if (event.getEntity() instanceof Player)
            if (event.getDamager() instanceof Player) {
                handleCheck((Player) event.getEntity(), (Player) event.getDamager(), event);
            } else if (event.getDamager() instanceof Projectile) {
                Projectile d1 = (Projectile) event.getDamager();
                if (!(d1.getShooter() instanceof Player))
                    return;
                Player damager = (Player) d1.getShooter();
                if (damager == event.getEntity())
                    return;
                handleCheck((Player) event.getEntity(), damager, event);
            }
    }

    private void handleCheck(Player player, Player damager, Cancellable event) {

        if (GameManager.getInstance().isLobby()) {
            return;
        }

        if (notPlaying(player) || notPlaying(damager))
            return;
        if (!isDisturbActive(damager.getUniqueId()) && USERS.get(damager.getUniqueId()) != null)
            USERS.remove(damager.getUniqueId());
        if (!isDisturbActive(player.getUniqueId()) && USERS.get(player.getUniqueId()) != null)
            USERS.remove(player.getUniqueId());
        if (USERS.get(damager.getUniqueId()) == null && USERS.get(player.getUniqueId()) == null) {
            applyDisturb(damager.getUniqueId(), player.getName());
            applyDisturb(player.getUniqueId(), damager.getName());
            damager.sendMessage(CC.SECONDARY + "You are now linked to " + CorePluginAPI.getProfile(player.getUniqueId()).getColoredUsername() + CC.SECONDARY + '.');
            player.sendMessage(CC.SECONDARY + "You are now linked to " + CorePluginAPI.getProfile(damager.getUniqueId()).getColoredUsername() + CC.SECONDARY + '.');
            CorePlugin.getInstance().getNametagEngine().reloadPlayer(player);
            CorePlugin.getInstance().getNametagEngine().reloadPlayer(damager);
        } else if ((USERS.get(damager.getUniqueId()) != null && !USERS.get(damager.getUniqueId()).equals(player.getName()) && isDisturbActive(damager.getUniqueId())) || (USERS.get(player.getUniqueId()) != null && !USERS.get(player.getUniqueId()).equals(damager.getName()) && isDisturbActive(player.getUniqueId()))) {
            event.setCancelled(true);
            damager.sendMessage(CC.RED + "This player is not linked to you.");
        } else {
            applyDisturb(damager.getUniqueId(), player.getName());
            applyDisturb(player.getUniqueId(), damager.getName());
        }
    }

    private void applyDisturb(UUID user, String target) {
        USERS.put(user, target);
        DND_COOLDOWN.put(user, Long.valueOf(System.currentTimeMillis() + 30000L));
    }

    @EventHandler(ignoreCancelled = true)
    public void onBlockBreak(BlockBreakEvent event) {
        if (notPlaying(event.getPlayer()))
            return;
        Block block = event.getBlock();
        if (block.getType() == Material.CHEST) {
            Chest chest = (Chest) block.getState();
            if (chest.hasMetadata("SafeLoot") && !chest.getMetadata("SafeLoot").get(0).asString().equals(event.getPlayer().getName())) {
                String owner = chest.getMetadata("SafeLoot").get(0).asString();
                if (GameManager.getInstance().getMode() == Mode.TO2) {
                    Team ownerTeam = TeamManager.getInstance().getTeam(UserManager.getInstance().getUser(Bukkit.getOfflinePlayer(owner).getUniqueId()).getTeamNumber()) != null ? TeamManager.getInstance().getTeam(UserManager.getInstance().getUser(Bukkit.getOfflinePlayer(owner).getUniqueId()).getTeamNumber()) : TeamManager.getInstance().getTeam(UserManager.getInstance().getUser(Bukkit.getPlayer(owner).getUniqueId()).getTeamNumber());
                    Team team = TeamManager.getInstance().getTeam(UserManager.getInstance().getUser(event.getPlayer().getUniqueId()).getTeamNumber());
                    if (ownerTeam.equals(team))
                        return;
                }
                event.setCancelled(true);
                event.getPlayer().sendMessage(Colors.RED + "This chest is protected.");
            }
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (notPlaying(event.getPlayer()) || !event.hasBlock())
            return;
        Block block = event.getClickedBlock();
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK && block.getType() == Material.CHEST) {
            Chest chest = (Chest) block.getState();
            if (chest.hasMetadata("SafeLoot") && !chest.getMetadata("SafeLoot").get(0).asString().equals(event.getPlayer().getName())) {
                String owner = chest.getMetadata("SafeLoot").get(0).asString();
                if (GameManager.getInstance().getMode() == Mode.TO2) {
                    Team ownerTeam = TeamManager.getInstance().getTeam(UserManager.getInstance().getUser(Bukkit.getOfflinePlayer(owner).getUniqueId()).getTeamNumber()) != null ? TeamManager.getInstance().getTeam(UserManager.getInstance().getUser(Bukkit.getOfflinePlayer(owner).getUniqueId()).getTeamNumber()) : TeamManager.getInstance().getTeam(UserManager.getInstance().getUser(Bukkit.getPlayer(owner).getUniqueId()).getTeamNumber());
                    Team team = TeamManager.getInstance().getTeam(UserManager.getInstance().getUser(event.getPlayer().getUniqueId()).getTeamNumber());
                    if (ownerTeam.equals(team))
                        return;
                }
                event.setCancelled(true);
                event.getPlayer().sendMessage(Colors.RED + "This chest is protected.");
            }
        }
    }
}
