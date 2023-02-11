package cc.stormworth.meetup.listener;

import cc.stormworth.core.CorePluginAPI;
import cc.stormworth.core.util.chat.CC;
import cc.stormworth.core.util.holograms.Holograms;
import cc.stormworth.core.util.onedoteight.TitleBuilder;
import cc.stormworth.meetup.Meetup;
import cc.stormworth.meetup.database.MeetupMongo;
import cc.stormworth.meetup.events.PlayerWinEvent;
import cc.stormworth.meetup.events.TeamWinEvent;
import cc.stormworth.meetup.managers.GameManager;
import cc.stormworth.meetup.managers.UserManager;
import cc.stormworth.meetup.scenarios.Scenario;
import cc.stormworth.meetup.states.GameState;
import cc.stormworth.meetup.team.Team;
import cc.stormworth.meetup.user.UserData;
import cc.stormworth.meetup.util.*;
import org.apache.commons.lang.StringUtils;
import org.bukkit.*;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityCombustEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Comparator;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class GameListener implements Listener {

    @EventHandler
    public void onPlayerGameWin(PlayerWinEvent e) {

        if (GameManager.getInstance().getGameState() == GameState.ENDING) return;

        UserData winner = e.getWinner();
        winner.getStatistics().setRerolls(winner.getStatistics().getRerolls() + 1);
        winner.getStatistics().setWins(winner.getStatistics().getWins() + 1);

        TaskUtil.runAsync(() -> MeetupMongo.getInstance().storeStatistics(
                winner.getName(),
                winner.getUniqueId(),
                winner.getStatistics(),
                winner.getHcfSortation().serialize()
        ));

        GameManager.getInstance().setWinner(winner.getName());
        GameManager.getInstance().setFinalGameTime(GameManager.getInstance().getGameTime());

        //UserManager.getInstance().getUsers().forEach(user -> user.getStatistics().save(true));

        TitleBuilder titleBuilder = new TitleBuilder(CC.translate("&6&lCongratulations!"), CC.translate("&eWinner: " +  winner.getName()), 10, 10, 10);
        Bukkit.getOnlinePlayers().forEach(titleBuilder::send);

        Msg.sendMessage(Colors.GRAY + Colors.STRIKE_THROUGH + "--------------------------------------------------");
        Msg.sendMessage(Colors.PRIMARY + Colors.BOLD + "                                  Meetup");
        Msg.sendMessage("");
        Msg.sendMessage(StringUtils.center("Winner: " + Colors.WHITE + winner.getName(), 85));
        Clickable clickable = new Clickable(Colors.GRAY + "                              [Winner's Inventory]", Colors.SECONDARY + "Click to view inventory!", "/uinv " + winner.getName());
        Bukkit.getOnlinePlayers().forEach(clickable::sendToPlayer);
        Msg.sendMessage("");
        Msg.sendMessage(Colors.PRIMARY + "                                       Top Kills");
        AtomicInteger ranking = new AtomicInteger(1);
        UserManager.getInstance().getUsers().stream()
                .sorted(Comparator.comparing(UserData::getKills).reversed())
                .limit(3L)
                .forEach((users -> Msg.sendMessage(StringUtils.center(Colors.GRAY + '#' + ranking.getAndIncrement() + ' ' + users.getName() + Colors.GRAY + " - " + Colors.RESET + users.getKills() + " Kills", 90))));
        Msg.sendMessage(Colors.GRAY + Colors.STRIKE_THROUGH + "--------------------------------------------------");

        NumberUtil.handleEloChange(winner, 3000);

        /*Bukkit.getOnlinePlayers().forEach(o ->
                ClientAPI.sendTitle(o, CC.B_PRIMARY + "Meetup", CC.SECONDARY + "Winner: " + CC.PRIMARY + winner.getName(), 15.0F));*/

        GameManager.getInstance().endGame();

        CorePluginAPI.getProfile(winner.getUniqueId()).addCoins(250);

        Player target = Bukkit.getPlayer(winner.getUniqueId());

        if (target == null) return;
        new BukkitRunnable() {
            int fireworks = 0;

            @Override
            public void run() {
                if (fireworks++ > 15) {
                    this.cancel();

                    return;
                }

                if (!target.isOnline()) {
                    this.cancel();

                    return;
                }

                Firework firework = target.getWorld().spawn(target.getLocation(), Firework.class);
                FireworkMeta fireworkMeta = firework.getFireworkMeta();
                fireworkMeta.addEffect(FireworkEffect.builder().flicker(false).trail(true).with(FireworkEffect.Type.BURST).withColor(Color.ORANGE).withFade(Color.YELLOW).build());
                fireworkMeta.setPower(3);
                firework.setFireworkMeta(fireworkMeta);
            }
        }.runTaskTimer(Meetup.getInstance(), 0L, 20L);

        target.sendMessage(CC.PRIMARY + "You have earned 250 coins for winning the game!");
    }

    @EventHandler
    public void onTeamWinEvent(TeamWinEvent e) {
        if (GameManager.getInstance().getGameState() == GameState.ENDING) return;

        Team winningTeam = e.getWinningTeam();
        winningTeam.getMembers().stream().map(uuid -> UserManager.getInstance().getUser(uuid)).forEach(user -> user.getStatistics().setWins(user.getStatistics().getWins() + 1));
        GameManager.getInstance().setWinningTeam(winningTeam);

        //UserManager.getInstance().getUsers().forEach(user -> user.getStatistics().save(true));
        winningTeam.getMembers().forEach(member -> {
            UserData targetUser = UserManager.getInstance().getUser(member);

            if (targetUser == null) return;

            TaskUtil.runAsync(() -> MeetupMongo.getInstance().storeStatistics(
                    targetUser.getName(),
                    targetUser.getUniqueId(),
                    targetUser.getStatistics(),
                    targetUser.getHcfSortation().serialize()
            ));
        });

        Msg.sendMessage(CC.GRAY + CC.STRIKE_THROUGH + "--------------------------------------------------");
        Msg.sendMessage(Colors.PRIMARY + Colors.BOLD + "                              Meetup");
        Msg.sendMessage("");
        Msg.sendMessage(StringUtils.center("Winner" + ((winningTeam.getMembersAsOfflinePlayers().size() > 1) ? "s" : "") + ": " + CC.WHITE + StringUtil.niceBuilder(winningTeam.getMembersAsOfflinePlayers().stream().map((OfflinePlayer::getName)).collect(Collectors.toList()), CC.WHITE + ", ", CC.WHITE + ", ", ""), 85));
        Clickable clickable = new Clickable(CC.GRAY + "                              [Top Kills Inventory]", CC.SECONDARY + "Click to view inventory!", "/inventory " + (winningTeam.getAliveMembers().stream().map(uuid -> UserManager.getInstance().getUser(uuid)).max(Comparator.comparing(UserData::getKills)).get()).getName());
        Bukkit.getOnlinePlayers().forEach(clickable::sendToPlayer);
        Msg.sendMessage("");
        Msg.sendMessage(CC.PRIMARY + "                                       Top Kills");
        AtomicInteger ranking = new AtomicInteger(1);
        UserManager.getInstance().getUsers().stream().sorted(Comparator.comparing(UserData::getKills).reversed()).limit(3L).filter(user -> user != null).forEach(user -> {
            Msg.sendMessage(StringUtils.center(CC.GRAY + '#' + ranking.getAndIncrement() + ' ' + user.getName() + CC.GRAY + " - " + CC.WHITE + user.getKills() + " Kills", 90));
        });
        Msg.sendMessage(CC.GRAY + CC.STRIKE_THROUGH + "--------------------------------------------------");

        winningTeam.getMembers().stream().map(uuid -> UserManager.getInstance().getUser(uuid)).forEach(winnerUser -> {
            NumberUtil.handleEloChange(winnerUser, 3000);

            CorePluginAPI.getProfile(winnerUser.getUniqueId()).addCoins(250);
            if (Bukkit.getPlayer(winnerUser.getUniqueId()) != null)
                Bukkit.getPlayer(winnerUser.getUniqueId()).sendMessage(CC.PRIMARY + "You have earned 250 coins for winning the game!");
        });

        /*Bukkit.getOnlinePlayers().forEach(o ->
                ClientAPI.sendTitle(o, CC.B_PRIMARY + "Meetup", CC.SECONDARY + "Winner" + ((winningTeam.getMembersAsOfflinePlayers().size() > 1) ? "s" : "") + ": " + CC.PRIMARY + StringUtil.niceBuilder(winningTeam.getMembersAsOfflinePlayers().stream().map((OfflinePlayer::getName)).collect(Collectors.toList()), CC.PRIMARY + ", ", CC.PRIMARY + ", ", ""), 15.0F));*/

        new BukkitRunnable() {
            int fireworks = 0;

            @Override
            public void run() {

                if (fireworks++ > 15) {
                    this.cancel();
                    return;
                }

                for (Player target : winningTeam.getOnlineMembers().stream()
                        .filter(OfflinePlayer::isOnline)
                        .filter(targetAgain -> !UserManager.getInstance().isSpectator(targetAgain))
                        .collect(Collectors.toSet())) {
                    Firework firework = target.getWorld().spawn(target.getLocation(), Firework.class);
                    FireworkMeta fireworkMeta = firework.getFireworkMeta();

                    fireworkMeta.addEffect(FireworkEffect.builder()
                            .flicker(false)
                            .trail(true)
                            .with(FireworkEffect.Type.BURST)
                            .withColor(Color.ORANGE)
                            .withFade(Color.YELLOW)
                            .build()
                    );
                    fireworkMeta.setPower(3);
                    firework.setFireworkMeta(fireworkMeta);
                }
            }
        }.runTaskTimer(Meetup.getInstance(), 0L, 20L);

        GameManager.getInstance().endGame();
    }

    @EventHandler(ignoreCancelled = true)
    public void onItemConsume(PlayerItemConsumeEvent e) {
        Player p = e.getPlayer();

        if (e.isCancelled() || e.getItem() == null || e.getItem().getType() != Material.GOLDEN_APPLE || e.getItem().getItemMeta() == null || !e.getItem().getItemMeta().hasDisplayName() || !e.getItem().getItemMeta().getDisplayName().equalsIgnoreCase(Colors.GOLD + Colors.BOLD + "Golden Head"))
            return;

        p.removePotionEffect(PotionEffectType.REGENERATION);
        p.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 200, 1));
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (event.isCancelled()) return;
        if (!(event.getEntity() instanceof Player)) return;
        Player entity = (Player) event.getEntity();
        if (!(event.getDamager() instanceof Arrow)) return;
        Arrow arrow = (Arrow) event.getDamager();
        if (!(arrow.getShooter() instanceof Player)) return;
        Player shooter = (Player) arrow.getShooter();
        if (entity.getName().equals(shooter.getName())) return;

        double hearts = entity.getHealth() / 2;
        double absorptionHearts = ((CraftPlayer) entity).getHandle().getAbsorptionHearts() / 2;
        double finalDamage = event.getFinalDamage() / 2;
        double health = hearts + absorptionHearts - finalDamage;

        if (health > 0.0D)
            shooter.sendMessage(CorePluginAPI.getProfile(entity.getUniqueId()).getColoredUsername() + Colors.SECONDARY + " is now at " + Colors.RED + roundToHalf(health) + '❤' + Colors.SECONDARY + '.');
    }

    private double roundToHalf(double number) {
        return Math.round(number / 0.5) * 0.5;
    }

    @EventHandler
    public void onWorldChange(PlayerChangedWorldEvent e) {
        Player p = e.getPlayer();

        if (p.getWorld().getName().equals("game_world")) {
            /*Tasks.runLater(() -> {
                ClientAPI.removeBorder(p);
                ClientAPI.sendBorder(p, GameManager.getInstance().getBorder().getSize());
            }, 5L);*/
        }
    }

    @EventHandler
    public void onLoggerTarget(EntityTargetEvent e) {

        if (!(e.getEntity() instanceof Zombie)) return;

        if (((Zombie) e.getEntity()).getCustomName().contains("Combat-Logger")) e.setCancelled(true);
    }

    @EventHandler
    public void onHorseSpawn(CreatureSpawnEvent event) {
        if (event.getEntityType() != EntityType.HORSE) return;
        if(Scenario.getByName("Horse Mania").isActive()) return;

        Horse horse = (Horse) event.getEntity();

        horse.setAdult();
        horse.setTamed(true);

        Entity entity = horse.getNearbyEntities(3, 3, 3).stream().filter(found -> found instanceof Player).findFirst().orElse(null);
        if (entity == null) return;
        Player player = (Player) entity;

        horse.setOwner(new AnimalTamer() {
            @Override
            public String getName() {
                return player.getName();
            }

            @Override
            public UUID getUniqueId() {
                return player.getUniqueId();
            }
        });

        int chance = ThreadLocalRandom.current().nextInt(2);

        horse.getInventory().setArmor(new ItemStack(chance == 1 ? Material.GOLD_BARDING : Material.IRON_BARDING));
        horse.getInventory().setSaddle(new ItemStack(Material.SADDLE));

        horse.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 20 * 60 * 3, 0));
    }

    @EventHandler
    public void onLoggerBurn(EntityCombustEvent e) {

        if (!(e.getEntity() instanceof Zombie)) return;

        if (((Zombie) e.getEntity()).getCustomName().contains("Combat-Logger")) e.setCancelled(true);
    }
}
