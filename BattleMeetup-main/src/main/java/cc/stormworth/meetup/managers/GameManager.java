package cc.stormworth.meetup.managers;

import cc.stormworth.core.CorePlugin;
import cc.stormworth.core.util.chat.CC;
import cc.stormworth.meetup.Meetup;
import cc.stormworth.meetup.border.Border;
import cc.stormworth.meetup.scenarios.Scenario;
import cc.stormworth.meetup.states.GameState;
import cc.stormworth.meetup.states.Mode;
import cc.stormworth.meetup.states.PlayerState;
import cc.stormworth.meetup.style.Style;
import cc.stormworth.meetup.style.hcf.enchantment.task.CustomEnchantsTask;
import cc.stormworth.meetup.tasks.*;
import cc.stormworth.meetup.team.Team;
import cc.stormworth.meetup.user.UserData;
import cc.stormworth.meetup.util.*;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

public class GameManager {

    @Getter
    private static final GameManager instance = new GameManager();
    @Getter
    private final int slots = 32;
    private GameState gameState = GameState.OFFLINE;
    private Mode mode = ServerUtil.getMode();
    @Getter
    @Setter
    private int requiredPlayers = this.mode == Mode.FFA ? 6 : 8;
    private Style style;
    @Getter
    private final EloCalculator eloCalculator = new EloCalculator(35, // k power
            2,
            25,
            2,
            25);
    @Getter
    @Setter
    private int scatteringIn = 60;
    private int startingIn = 20;
    private int gameTime = 0;
    private final int borderTime = 120;
    private int restartingIn = 30;

    private Border border = null;

    private final List<UUID> alivePlayers = new ArrayList<>();
    private final List<UUID> spectators = new ArrayList<>();


    private String winner = "None";
    private Team winningTeam = null;
    private int finalGameTime = 0;
    private int totalPlayers = 0;
    private int totalTeams = 0;

    public GameState getGameState() {
        return gameState;
    }

    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }

    public Mode getMode() {
        return mode;
    }

    public void setMode(Mode mode) {
        this.mode = mode;
    }

    public Style getStyle() {
        return style;
    }

    public void setStyle(Style style) {
        this.style = style;
    }

    public int getStartingIn() {
        return startingIn;
    }

    public void setStartingIn(int startingIn) {
        this.startingIn = startingIn;
    }

    public int getGameTime() {
        return gameTime;
    }

    public void setGameTime(int gameTime) {
        this.gameTime = gameTime;
    }

    public int getBorderTime() {
        return borderTime;
    }

    public int getRestartingIn() {
        return restartingIn;
    }

    public void setRestartingIn(int restartingIn) {
        this.restartingIn = restartingIn;
    }

    public String getFormattedBorderStatus() {
        String output = " ";
        int borderTimeDifference = GameManager.getInstance().getBorderTime() - GameTask.getShrink();

        if (borderTimeDifference >= 60) {
            output += Colors.GRAY + "(" + Colors.RED + borderTimeDifference / 60 + "m" + Colors.GRAY + ")";
        } else {
            output += Colors.GRAY + "(" + Colors.RED + borderTimeDifference + "s" + Colors.GRAY + ")";
        }

        if (borderTimeDifference == 0) {
            output = "";
        }

        return output;
    }

    public Border getBorder() {

        if (border == null) {
            border = new Border(Bukkit.getWorld("world"), 1000);
        }

        return border;
    }

    public void setBorder(Border border) {
        this.border = border;
    }

    public List<UUID> getAlivePlayers() {
        return alivePlayers;
    }

    public List<UUID> getSpectators() {
        return spectators;
    }

    public String getWinner() {
        return winner;
    }

    public void setWinner(String winner) {
        this.winner = winner;
    }

    public Team getWinningTeam() {
        return winningTeam;
    }

    public void setWinningTeam(Team winningTeam) {
        this.winningTeam = winningTeam;
    }

    public int getFinalGameTime() {
        return finalGameTime;
    }

    public void setFinalGameTime(int finalGameTime) {
        this.finalGameTime = finalGameTime;
    }

    public int getTotalPlayers() {
        return totalPlayers;
    }

    public void setTotalPlayers(int totalPlayers) {
        this.totalPlayers = totalPlayers;
    }

    public int getTotalTeams() {
        return totalTeams;
    }

    public void setTotalTeams(int totalTeams) {
        this.totalTeams = totalTeams;
    }

    public boolean isLobby() {

        return this.gameState == GameState.WAITING || this.gameState == GameState.STARTING;
    }

    public boolean isIngame() {

        return this.gameState == GameState.STARTED || this.gameState == GameState.ENDING;
    }

    public void handleLobbyJoin() {
        this.border = new Border(Bukkit.getWorld("game_world"), BorderUtil.calculateBorderSize());

        if (Bukkit.getOnlinePlayers().size() == getRequiredPlayers()) {
            startCountdown();
        }
    }

    public void handleLobbyLeave() {
        this.border = new Border(Bukkit.getWorld("game_world"), BorderUtil.calculateBorderSize());
    }

    public void startCountdown() {

        if (getGameState() == GameState.WAITING) {
            System.out.println("STARTING :D");
            CorePlugin.getInstance().getRedisManager().writeAlertClickable(CC.GRAY + "[" + CC.PRIMARY + "✪" + CC.GRAY + "] " + CC.B_PRIMARY
                    + CorePlugin.getInstance().getServerId().toUpperCase() + CC.SECONDARY
                    + " is starting in " + GameManager.getInstance().getScatteringIn() + " seconds. " + CC.GRAY
                    + CC.ITALIC + "(Click to join)", "/joinsv " + CorePlugin.getInstance().getServerId(), false);

            setGameState(GameState.STARTING);
            new CountdownTask();

            /*Bukkit.getOnlinePlayers().forEach(o ->
                    ClientAPI.sendTitle(o, CC.B_PRIMARY + "Meetup", CC.SECONDARY + "Countdown started!", 3.5F));*/
        }
    }

    public void startScatter() {

        if (getGameState() == GameState.STARTING) {
            this.border.shrink();

            setGameState(GameState.SCATTER);
            this.style = Style.getCurrentlyVoted();
            Msg.sendMessage(CC.SECONDARY + "Game style " + CC.PRIMARY + this.style.getName() + CC.SECONDARY + " was chosen for this match.");

            if (this.style == Style.UHC) {
                if (Bukkit.getOnlinePlayers().size() <= 6)
                    if (!Scenario.getByName("OP Kits").isActive())
                        Scenario.getByName("OP Kits").toggle(null, false);
                Scenario.enableRandomScenarios();
                Msg.sendMessage(CC.SECONDARY + "Scenarios " + CC.PRIMARY + StringUtil.niceBuilder(Scenario.getEnabledScenarios().stream().map(Scenario::getName).collect(Collectors.toList()), CC.SECONDARY + ", " + CC.PRIMARY, CC.SECONDARY + " and " + CC.PRIMARY, "") + CC.SECONDARY + " are active in this match.");
            }

            if (getMode() == Mode.TO2) {
                new TeamScatterTask();
                return;
            }

            new ScatterTask();
        }
    }

    public void startGame() {

        if (getGameState() == GameState.SCATTER) {
            PlayerUtil.unfreezeAll();
            Bukkit.getOnlinePlayers().forEach(p -> {
                CorePlugin.getInstance().getNametagEngine().reloadPlayer(p);
                CorePlugin.getInstance().getNametagEngine().reloadOthersFor(p);

                UserData user = UserManager.getInstance().getUser(p.getUniqueId());

                if (user.getPlayerState() == PlayerState.SCATTERED) {
                    user.setPlayerState(PlayerState.INGAME);
                }

                if (GameManager.getInstance().getStyle() == Style.UHC) {
                    Scoreboard scoreboard = p.getScoreboard();

                    if (scoreboard.getObjective("h") == null) {
                        Objective objective = scoreboard.registerNewObjective("h", "health");
                        objective.setDisplayName(Colors.DARK_RED + "❤");
                        objective.setDisplaySlot(DisplaySlot.BELOW_NAME);
                    }
                }
            });

            setGameState(GameState.STARTED);

            new GameTask();
            new AlivePlayersCheckTask();
            if (GameManager.getInstance().getStyle() == Style.HCF) {
                new CustomEnchantsTask();
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "kb setglobal hcf");
            } else {
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "kb setglobal Default");
            }

            if (Scenario.getByName("Horse Mania").isActive()) {
                if(!WorldManager.getInstance().isNetherGame()) {
                    UserManager.getInstance().getAliveUsers().forEach(user -> {
                        Player player = Bukkit.getPlayer(user.getUniqueId());
                        World world = player.getWorld();

                        Horse horse = (Horse) world.spawnEntity(player.getLocation().clone().add(1, 0, 0), EntityType.HORSE);

                        horse.setAdult();
                        horse.setOwner(player);
                        horse.setTamed(true);

                        horse.getInventory().setArmor(new ItemStack(ThreadLocalRandom.current().nextBoolean() ? Material.DIAMOND_BARDING : Material.IRON_BARDING));
                        horse.getInventory().setSaddle(new ItemStack(Material.SADDLE));

                        horse.setPassenger(player);
                    });
                } else {
                    Msg.sendMessage(CC.PRIMARY + "Horse Mania " + CC.SECONDARY + "wasn't enabled because the game world is nether.");
                }
            }
        }
    }

    public void endGame() {

        if (getGameState() == GameState.ENDING) {
            return;
        }

        //UserManager.getInstance().getUsers().forEach(user -> user.getStatistics().save(true));

        setGameState(GameState.ENDING);
        new EndTask();
    }
}
