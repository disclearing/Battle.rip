package cc.stormworth.meetup.listener;

import cc.stormworth.core.CorePluginAPI;
import cc.stormworth.core.profile.Profile;
import cc.stormworth.core.util.chat.CC;
import cc.stormworth.meetup.database.MeetupMongo;
import cc.stormworth.meetup.events.PlayerWinEvent;
import cc.stormworth.meetup.events.TeamWinEvent;
import cc.stormworth.meetup.managers.GameManager;
import cc.stormworth.meetup.managers.PlayerManager;
import cc.stormworth.meetup.managers.TeamManager;
import cc.stormworth.meetup.managers.UserManager;
import cc.stormworth.meetup.scenarios.Scenario;
import cc.stormworth.meetup.scenarios.impl.TimebombScenario;
import cc.stormworth.meetup.states.GameState;
import cc.stormworth.meetup.states.Mode;
import cc.stormworth.meetup.states.PlayerState;
import cc.stormworth.meetup.style.Style;
import cc.stormworth.meetup.team.Team;
import cc.stormworth.meetup.user.UserData;
import cc.stormworth.meetup.util.*;
import net.minecraft.server.v1_7_R4.EntityLiving;
import net.minecraft.server.v1_7_R4.EntityPlayer;
import org.apache.commons.lang.WordUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftLivingEntity;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;

import cc.stormworth.core.CorePlugin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DeathListener implements Listener {

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e) {
        Player p = e.getEntity();

        UserData user = UserManager.getInstance().getUser(p.getUniqueId());

        Player k = p.getKiller();
        UserData targetUser = k != null ? UserManager.getInstance().getUser(k.getUniqueId()) : null;

        user.setPlayerState(PlayerState.SPECTATOR);
        if (GameManager.getInstance().getGameState() != GameState.ENDING) {
            user.getStatistics().setDeaths(user.getStatistics().getDeaths() + 1);
        }

        if (targetUser != null) {
            targetUser.setKills(targetUser.getKills() + 1);
            targetUser.getStatistics().setKills(targetUser.getStatistics().getKills() + 1);

            if (GameManager.getInstance().getMode() == Mode.TO2) {
                Team team = TeamManager.getInstance().getTeam(targetUser.getTeamNumber());
                team.setKills(team.getKills() + 1);
            }

            k.playSound(k.getLocation(), Sound.LEVEL_UP, 1, 1);
        }

        handleDeathMessage(e);
        handleScenarios(e);

        if (GameManager.getInstance().getGameState() != GameState.ENDING) {
            if (targetUser == null)
                NumberUtil.handleEloChange(1400, user, p);
            else
                NumberUtil.handleEloChange(targetUser, user);
        }

        GameManager.getInstance().getAlivePlayers().remove(p.getUniqueId());

        TaskUtil.runAsync(() -> {
            MeetupMongo.getInstance().storeStatistics(
                    user.getName(),
                    user.getUniqueId(),
                    user.getStatistics(),
                    user.getHcfSortation().serialize()
            );

            if (targetUser != null) {
                MeetupMongo.getInstance().storeStatistics(
                        targetUser.getName(),
                        targetUser.getUniqueId(),
                        targetUser.getStatistics(),
                        targetUser.getHcfSortation().serialize()
                );
            }
        });

        if (GameManager.getInstance().getMode() == Mode.TO2) {
            if (TeamManager.getInstance().getAliveTeams().size() == 1) {
                Team winningTeam = TeamManager.getInstance().getAliveTeams().get(0);
                Bukkit.getServer().getPluginManager().callEvent(new TeamWinEvent(winningTeam));
            }

            return;
        }

        if (GameManager.getInstance().getAlivePlayers().size() == 1) {
            UserData winningUser = UserManager.getInstance().getUser(GameManager.getInstance().getAlivePlayers().get(0));
            Bukkit.getServer().getPluginManager().callEvent(new PlayerWinEvent(winningUser));
        }

        PlayerUtil.animateDeath(p);

        if (k != null) {
            CorePluginAPI.getAvailableProfile(k.getUniqueId()).addCoins(25);
            k.sendMessage(CC.PRIMARY + "You have earned 25 coins for killing this player!");
        }
    }

    @EventHandler
    public void onRespawn(PlayerRespawnEvent e) {
        Player p = e.getPlayer();

        e.setRespawnLocation(p.getLocation());
        PlayerManager.getInstance().enableSpectatorMode(p);
    }


    public void handleDeathMessage(PlayerDeathEvent e) {
        String message = e.getDeathMessage();
        if (message == null || message.isEmpty())
            return;
        boolean logger = message.contains(";LOGGER");
        if (logger)
            message = message.replace(";LOGGER", "");
        e.setDeathMessage(null);
        Player player = e.getEntity();

        if (player.getKiller() != null) {
            player.getKiller().sendMessage(Profile.getByPlayer(player).getColoredUsername() + "'s " + CC.YELLOW + "rating is " + CC.WHITE + UserManager.getInstance().getUser(player.getUniqueId()).getStatistics().getElo() + "elo" + CC.YELLOW + ".");
        }

        if (GameManager.getInstance().getGameState() == GameState.STARTED) {
            EntityDamageEvent cause = player.getLastDamageCause();
            if (cause != null) {
                EntityDamageEvent.DamageCause damageCause = cause.getCause();
                if (damageCause == EntityDamageEvent.DamageCause.PROJECTILE) {
                    Projectile projectile = (Projectile) ((EntityDamageByEntityEvent) cause).getDamager();
                    if (projectile.getShooter() instanceof Player) {
                        Player shooter = (Player) projectile.getShooter();
                        int distance = (int) Math.round(shooter.getLocation().distance(player.getLocation()));
                        sendMessage(logger, message, e,
                                (shooter.getItemInHand() != null && shooter.getItemInHand().getType() != Material.AIR
                                        && shooter.getItemInHand().getType() == Material.BOW)
                                        ? (Colors.SECONDARY + " from " + Colors.BLUE + distance
                                        + ((distance == 1) ? " block" : " blocks"))
                                        : "");
                        return;
                    }
                }
            }
            sendMessage(logger, message, e, "");
        }
    }

    public void handleDeathMessage(EntityDeathEvent e, String name) {
        Player killer = e.getEntity().getKiller();
        String message = killer == null ? name + " died" : name + " was slain by " + killer.getDisplayName();
        LivingEntity player = e.getEntity();

        if (GameManager.getInstance().getGameState() == GameState.STARTED) {
            EntityDamageEvent cause = player.getLastDamageCause();
            if (cause != null) {
                EntityDamageEvent.DamageCause damageCause = cause.getCause();
                if (damageCause == EntityDamageEvent.DamageCause.PROJECTILE) {
                    Projectile projectile = (Projectile) ((EntityDamageByEntityEvent) cause).getDamager();
                    if (projectile.getShooter() instanceof Player) {
                        Player shooter = (Player) projectile.getShooter();
                        int distance = (int) Math.round(shooter.getLocation().distance(player.getLocation()));
                        sendMessage(true, message, e,
                                (shooter.getItemInHand() != null && shooter.getItemInHand().getType() != Material.AIR
                                        && shooter.getItemInHand().getType() == Material.BOW)
                                        ? (Colors.SECONDARY + " from " + Colors.BLUE + distance
                                        + ((distance == 1) ? " block" : " blocks"))
                                        : "",
                                name);
                        return;
                    }
                }
            }
            sendMessage(true, message, e, "", name);
        }
    }

    private String getDeathMessage(String logger, String input, Entity entity, Entity killer, String pColor,
                                   String kColor, String slainType) {
        input = input.replaceFirst("\\[", Colors.GRAY + "[" + Colors.GRAY);
        input = input.replaceFirst("(?s)](?!.*?])", Colors.GRAY + "]" + Colors.GRAY);
        if (entity != null)
            input = input.replaceFirst("(?i)" + getEntityName(entity),
                    pColor + getNiceDisplayName(entity) + logger + Colors.SECONDARY);
        if (killer instanceof Player && !killer.equals(entity)) {
            input = input.replaceFirst("(?i)" + getEntityName(killer),
                    kColor + getNiceDisplayName(killer) + Colors.SECONDARY);
            if (input.contains("was slain by"))
                input = input.replace("was slain by", slainType);
        }
        return input;
    }

    private String getEntityName(Entity entity) {
        return (entity instanceof Player) ? ((Player) entity).getName() : ((CraftEntity) entity).getHandle().getName();
    }

    private String getNiceDisplayName(Entity entity) {
        if (entity instanceof Player) {
            Player player = (Player) entity;
            return player.getName() + Colors.GRAY + '[' + Colors.RESET
                    + UserManager.getInstance().getUser(player.getUniqueId()).getKills() + Colors.GRAY + ']';
        }
        return WordUtils.capitalizeFully(entity.getType().name().replace('_', ' '));
    }

    private CraftEntity getKiller(EntityDeathEvent event) {
        EntityLiving lastAttacker = ((CraftLivingEntity) event.getEntity()).getHandle().aX();
        return (lastAttacker == null) ? null : lastAttacker.getBukkitEntity();
    }

    private void sendMessage(boolean logger, String message, PlayerDeathEvent event, String toAdd) {
        if (event.getEntity().getKiller() != null) {
            toAdd = toAdd + Colors.GRAY + " (" + Colors.RED
                    + (Math.ceil(event.getEntity().getKiller().getHealth() / 2.0D)) + Colors.RED + "❤" + Colors.GRAY
                    + ")";
            // Added a space and made the amount of hearts red (looks better)
        }

        toAdd += Colors.SECONDARY + ".";

        String slainType = "was slain by";
        Player killer = event.getEntity().getKiller();
        if (killer == null) {
            slainType = "killed by unknown";
        }
        String combatLogger = logger ? (Colors.GRAY + " (Combat-Logger)") : "";
        for (Player player : Bukkit.getOnlinePlayers()) {
            String color1 = (event.getEntity().getName().equals(player.getName())
                    || (GameManager.getInstance().getMode() == Mode.TO2
                    && UserManager.getInstance().getUser(player.getUniqueId()).hasTeam()
                    && TeamManager.getInstance().getTeam(UserManager.getInstance().getUser(player.getUniqueId()).getTeamNumber()).getMembers()
                    .contains(event.getEntity().getUniqueId()))) ? Colors.GREEN : Colors.RED;
            EntityPlayer entityPlayer = ((CraftPlayer) event.getEntity()).getHandle();
            String color2 = (entityPlayer.getLastDamager() != null)
                    ? ((entityPlayer.getLastDamager().getName().equals(player.getName())
                    || (GameManager.getInstance().getMode() == Mode.TO2
                    && UserManager.getInstance().getUser(player.getUniqueId()).hasTeam()
                    && TeamManager.getInstance().getTeam(UserManager.getInstance().getUser(player.getUniqueId()).getTeamNumber()).getMembers()
                    .contains(entityPlayer.getLastDamager().getUniqueID()))) ? Colors.GREEN
                    : Colors.RED)
                    : Colors.RED;
            player.sendMessage(getDeathMessage(combatLogger, message, event.getEntity(),
                    getKiller(event), color1, color2, slainType) + toAdd);
        }
    }

    private void sendMessage(boolean logger, String message, EntityDeathEvent event, String toAdd, String name) {
        if (event.getEntity().getKiller() != null) {
            toAdd = toAdd + Colors.GRAY + " (" + Colors.RED
                    + (Math.ceil(event.getEntity().getKiller().getHealth() / 2.0D)) + Colors.RED + "❤" + Colors.GRAY
                    + ")";
            // Added a space and made the amount of hearts red (looks better)
        }

        toAdd += Colors.SECONDARY + ".";

        String slainType = "was slain by";
        Player killer = event.getEntity().getKiller();
        if (killer != null) {
            slainType = "killed by unknown";

        }
        String combatLogger = logger ? (Colors.GRAY + " (Combat-Logger)") : "";
        for (Player player : Bukkit.getOnlinePlayers()) {
            String color1 = (name.equals(player.getName()) || (GameManager.getInstance().getMode() == Mode.TO2
                    && UserManager.getInstance().getUser(player.getUniqueId()).hasTeam() && TeamManager.getInstance().getTeam(UserManager.getInstance().getUser(player.getUniqueId()).getTeamNumber())
                    .getMembers().contains(event.getEntity().getUniqueId())))
                    ? Colors.GREEN
                    : Colors.RED;
            EntityLiving entityPlayer = ((CraftLivingEntity) event.getEntity()).getHandle();
            String color2 = (entityPlayer.getLastDamager() != null)
                    ? ((entityPlayer.getLastDamager().getName().equals(player.getName())
                    || (GameManager.getInstance().getMode() == Mode.TO2
                    && UserManager.getInstance().getUser(player.getUniqueId()).hasTeam()
                    && TeamManager.getInstance().getTeam(UserManager.getInstance().getUser(player.getUniqueId()).getTeamNumber())
                    .getMembers().contains(entityPlayer.getLastDamager().getUniqueID()))) ? Colors.GREEN
                    : Colors.RED)
                    : Colors.RED;
            player.sendMessage(getDeathMessage(combatLogger, message, event.getEntity(),
                    getKiller(event), color1, color2, slainType) + toAdd);
        }
    }

    public void handleScenarios(PlayerDeathEvent e) {
        Player p = e.getEntity();

        boolean timebomb = Scenario.getByName("Timebomb").isActive();
        boolean safeLoot = (Scenario.getByName("SafeLoot").isActive() || Scenario.getByName("NoClean+").isActive());

        if (!timebomb && !safeLoot) {

            if (GameManager.getInstance().getStyle() == Style.UHC && !Scenario.getByName("Hypixel Heads").isActive()) {
                LocationUtil.spawnHead(p);
                p.getLocation().getWorld().dropItemNaturally(p.getLocation(),
                        new ItemBuilder(Material.GOLD_INGOT).setAmount(8).build());

                p.getLocation().getWorld().dropItemNaturally(p.getLocation(),
                        new ItemBuilder(Material.EXP_BOTTLE).setAmount(32).build());
            }
        } else {
            List<ItemStack> items = new ArrayList<>();
            Arrays.stream(p.getInventory().getArmorContents())
                    .filter(stack -> (stack != null && stack.getType() != Material.AIR)).forEach(items::add);
            Arrays.stream(p.getInventory().getContents())
                    .filter(stack -> (stack != null && stack.getType() != Material.AIR)).forEach(items::add);

            if (timebomb && safeLoot) {
                TimebombScenario.handleTimebomb(p, e.getDrops(), items, true, true);
            } else if (timebomb) {
                TimebombScenario.handleTimebomb(p, e.getDrops(), items, true, false);
            } else {
                TimebombScenario.handleTimebomb(p, e.getDrops(), items, false, true);
            }

            e.getDrops().clear();
        }
    }

    public void handleScenarios(EntityDeathEvent e, List<ItemStack> drops) {
        LivingEntity p = e.getEntity();

        boolean timebomb = Scenario.getByName("Timebomb").isActive();
        boolean safeLoot = (Scenario.getByName("SafeLoot").isActive() || Scenario.getByName("NoClean+").isActive());

        if (!timebomb && !safeLoot) {

            if (GameManager.getInstance().getStyle() == Style.UHC && !Scenario.getByName("Hypixel Heads").isActive()) {
                LocationUtil.spawnHead(p);
                p.getLocation().getWorld().dropItemNaturally(p.getLocation(),
                        new ItemBuilder(Material.GOLD_INGOT).setAmount(8).build());
            }
        } else {
            List<ItemStack> items = new ArrayList<>();
            drops.stream().filter(stack -> (stack != null && stack.getType() != Material.AIR)).forEach(items::add);

            if (timebomb && safeLoot) {
                TimebombScenario.handleTimebomb(p, e.getDrops(), items, true, true);
            } else if (timebomb) {
                TimebombScenario.handleTimebomb(p, e.getDrops(), items, true, false);
            } else {
                TimebombScenario.handleTimebomb(p, e.getDrops(), items, false, true);
            }

            e.getDrops().clear();
        }
    }
}
