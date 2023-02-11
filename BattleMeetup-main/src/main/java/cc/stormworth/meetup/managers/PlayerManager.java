package cc.stormworth.meetup.managers;

import cc.stormworth.core.CorePluginAPI;
import cc.stormworth.core.profile.Profile;
import cc.stormworth.core.rank.Rank;
import cc.stormworth.core.util.chat.CC;
import cc.stormworth.core.util.onedoteight.TitleBuilder;
import cc.stormworth.meetup.Meetup;
import cc.stormworth.meetup.scenarios.Scenario;
import cc.stormworth.meetup.scenarios.impl.TimebombScenario;
import cc.stormworth.meetup.states.GameState;
import cc.stormworth.meetup.states.PlayerState;
import cc.stormworth.meetup.style.Style;
import cc.stormworth.meetup.user.UserData;
import cc.stormworth.meetup.util.*;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PlayerManager {

    public static final Location SPAWN_LOCATION = LocationUtil
            .deserializeLocation(Meetup.getInstance().getConfig().getString("Locations.Spawn-Location")) != null
            ? LocationUtil
            .deserializeLocation(Meetup.getInstance().getConfig().getString("Locations.Spawn-Location"))
            : new Location(Bukkit.getWorld("world"), 0.5D, 4.0D, 0.5D, 0.0F, 0.0F);
    public static final Location CENTER_LOCATION = new Location(Bukkit.getWorld("game_world"), 0,
            Bukkit.getWorld("game_world").getHighestBlockYAt(0, 0) + 10, 0);
    private static PlayerManager instance;

    public static PlayerManager getInstance() {

        if (instance == null) {
            instance = new PlayerManager();
        }

        return instance;
    }

    public void handleLobbyJoin(Player player) {
        UserData user = UserManager.getInstance().getUser(player.getUniqueId());
        user.setPlayerState(PlayerState.LOBBY);

        TitleBuilder titleBuilder = new TitleBuilder(CC.translate("&6&lMeetup!"), CC.translate("&e/gamestyle"), 10, 10, 10);
        titleBuilder.send(player);

        player.teleport(SPAWN_LOCATION);

        PlayerUtil.clearPlayer(player);

        InventoryManager.getInstance().setupLobbyInventory(player);

        Profile profile = Profile.getByUuidIfAvailable(player.getUniqueId());

        if (profile == null) return;

        Msg.sendMessage(profile.getRank().getColor() + player.getDisguisedName() + Colors.SECONDARY + " has joined the game " + Colors.GRAY + "("
                + Colors.PRIMARY + Bukkit.getOnlinePlayers().size() + "/" + GameManager.getInstance().getSlots()
                + Colors.GRAY + ")" + Colors.SECONDARY + ".");

        if (GameManager.getInstance().getGameState() == GameState.WAITING) {
            int requiredPlayers = GameManager.getInstance().getRequiredPlayers() - Bukkit.getOnlinePlayers().size();
            Msg.sendMessage(Colors.PRIMARY + requiredPlayers + Colors.SECONDARY + " more player"
                    + (requiredPlayers > 1 ? "s are" : " is") + " required to start the game.");
        }
    }

    public void handleLobbyLeave(Player p) {
        Msg.sendMessage(CorePluginAPI.getProfile(p.getUniqueId()).getColoredUsername() + Colors.SECONDARY + " has left the game " + Colors.GRAY + "("
                + Colors.PRIMARY + (Bukkit.getOnlinePlayers().size() - 1) + "/" + GameManager.getInstance().getSlots()
                + Colors.GRAY + ")" + Colors.SECONDARY + ".");

        if (GameManager.getInstance().getGameState() == GameState.WAITING) {
            int requiredPlayers = GameManager.getInstance().getRequiredPlayers() - Bukkit.getOnlinePlayers().size() + 1;
            Msg.sendMessage(Colors.PRIMARY + requiredPlayers + Colors.SECONDARY + " more player"
                    + (requiredPlayers > 1 ? "s are" : " is") + " required to start the game.");
        }

        for (Style style : Style.values()) {
            style.removeVote(p);
        }
    }

    public void handleScatterJoin(Player p) {
        UserData user = UserManager.getInstance().getUser(p.getUniqueId());

        if (PlayerUtil.testPermission(p, Rank.WARRIOR)) {
            user.setPlayerState(PlayerState.SCATTERED);
            GameManager.getInstance().getAlivePlayers().add(p.getUniqueId());
            GameManager.getInstance().setTotalPlayers(GameManager.getInstance().getTotalPlayers() + 1);

            PlayerUtil.scatterPlayer(p);
            p.sendMessage(Colors.SECONDARY + "You have been scattered.");
        } else {
            p.teleport(CENTER_LOCATION);
            enableSpectatorMode(p);
        }
    }

    public void handleScatterLeave(Player p) {
        UserData user = UserManager.getInstance().getUser(p.getUniqueId());

        switch (user.getPlayerState()) {

            case SCATTERED:
                Bukkit.broadcastMessage(Colors.RED + p.getDisguisedName() + Colors.GRAY + "[" + Colors.WHITE + user.getKills()
                        + Colors.GRAY + "] " + Colors.SECONDARY + "disconnected and has been disqualified.");
                List<ItemStack> drops = new ArrayList<>();
                drops.addAll(Arrays.asList(p.getInventory().getContents()));
                drops.addAll(Arrays.asList(p.getInventory().getArmorContents()));

                p.setMetadata("leftWhileScatter", new FixedMetadataValue(Meetup.getInstance(), true));

                PlayerDeathEvent playerDeathEvent = new PlayerDeathEvent(p, drops, p.getLevel(), "");
                Bukkit.getServer().getPluginManager().callEvent(playerDeathEvent);

                if (!Scenario.getByName("Timebomb").isActive() && !Scenario.getByName("SafeLoot").isActive()
                        && !Scenario.getByName("NoClean+").isActive()) {
                    drops.stream().filter(item -> item != null).filter(item -> item.getType() != Material.AIR)
                            .forEach(item -> p.getLocation().getWorld().dropItemNaturally(p.getLocation(), item));
                }
                break;
            case SPECTATOR:
                GameManager.getInstance().getSpectators().remove(p.getUniqueId());
                break;
            default:
                break;
        }
    }

    public void handleIngameJoin(Player p) {
        UserData user = UserManager.getInstance().getUser(p.getUniqueId());

        Bukkit.getOnlinePlayers().stream().map(all -> UserManager.getInstance().getUser(all.getUniqueId()))
                .filter(allUser -> !allUser.isAlive())
                .forEach(spectator -> p.hidePlayer(Bukkit.getPlayer(spectator.getUniqueId())));

        p.teleport(CENTER_LOCATION);
        enableSpectatorMode(p);
    }

    public void handleIngameLeave(Player p) {
        UserData user = UserManager.getInstance().getUser(p.getUniqueId());

        switch (user.getPlayerState()) {

            case INGAME:
                Bukkit.broadcastMessage(Colors.RED + p.getName() + Colors.GRAY + "[" + Colors.WHITE + user.getKills()
                        + Colors.GRAY + "] " + Colors.SECONDARY + "disconnected and has been disqualified.");
                List<ItemStack> drops = new ArrayList<>();
                drops.addAll(Arrays.asList(p.getInventory().getContents()));
                drops.addAll(Arrays.asList(p.getInventory().getArmorContents()));

                PlayerDeathEvent playerDeathEvent = new PlayerDeathEvent(p, drops, p.getLevel(), "");
                Bukkit.getServer().getPluginManager().callEvent(playerDeathEvent);

                if (!Scenario.getByName("Timebomb").isActive() && !Scenario.getByName("SafeLoot").isActive()
                        && !Scenario.getByName("NoClean+").isActive()) {
                    drops.stream().filter(item -> item != null).filter(item -> item.getType() != Material.AIR)
                            .forEach(item -> p.getLocation().getWorld().dropItemNaturally(p.getLocation(), item));
                }

                break;
            case SPECTATOR:
                GameManager.getInstance().getSpectators().remove(p.getUniqueId());
                break;
            default:
                break;
        }
    }

    public void enableSpectatorMode(Player p) {
        UserData user = UserManager.getInstance().getUser(p.getUniqueId());
        user.setPlayerState(PlayerState.SPECTATOR);
        GameManager.getInstance().getSpectators().add(p.getUniqueId());

        Bukkit.getOnlinePlayers().forEach(all -> all.hidePlayer(p));

        PlayerUtil.clearPlayer(p, GameMode.CREATIVE);
        p.setAllowFlight(true);
        p.setFlying(true);
        InventoryManager.getInstance().setupSpectatorInventory(p);
        //Meetup.getInstance().getNametagManager().reloadPlayer(p, true);

        p.sendMessage(Colors.SECONDARY + "You are now in spectator mode.");
    }

    private void handleScenarios(Player p) {

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
            Arrays.stream(p.getInventory().getArmorContents())
                    .filter(stack -> (stack != null && stack.getType() != Material.AIR)).forEach(items::add);
            Arrays.stream(p.getInventory().getContents())
                    .filter(stack -> (stack != null && stack.getType() != Material.AIR)).forEach(items::add);

            List<ItemStack> drops = new ArrayList<>(Arrays.asList(p.getInventory().getContents()));

            if (timebomb && safeLoot) {
                TimebombScenario.handleTimebomb(p, drops, items, true, true);
            } else if (timebomb) {
                TimebombScenario.handleTimebomb(p, drops, items, true, false);
            } else {
                TimebombScenario.handleTimebomb(p, drops, items, false, true);
            }

            p.getInventory().clear();
        }
    }
}
