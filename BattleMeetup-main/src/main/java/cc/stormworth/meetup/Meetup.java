package cc.stormworth.meetup;

import cc.stormworth.core.CorePlugin;
import cc.stormworth.core.util.command.rCommandHandler;
import cc.stormworth.core.util.scoreboard.ScoreboardHandler;
import cc.stormworth.core.util.scoreboard.ScoreboardStyle;
import cc.stormworth.meetup.ability.Ability;
import cc.stormworth.meetup.ability.impl.blinder.BlinderRunnable;
import cc.stormworth.meetup.border.glass.GlassMoveHandler;
import cc.stormworth.meetup.commands.*;
import cc.stormworth.meetup.database.MeetupMongo;
import cc.stormworth.meetup.kits.KitEditorMenu;
import cc.stormworth.meetup.listener.*;
import cc.stormworth.meetup.managers.UserManager;
import cc.stormworth.meetup.managers.WorldManager;
import cc.stormworth.meetup.profile.ProfileListener;
import cc.stormworth.meetup.providers.MeetupNametagProvider;
import cc.stormworth.meetup.providers.MeetupServerDataProvider;
import cc.stormworth.meetup.providers.ScoreboardProvider;
import cc.stormworth.meetup.providers.TablistProvider;
import cc.stormworth.meetup.scenarios.ScenarioManager;
import cc.stormworth.meetup.style.hcf.enchantment.task.CustomEnchantsTask;
import cc.stormworth.meetup.style.hcf.kit.Kit;
import cc.stormworth.meetup.style.hcf.kit.menu.HCFEditorListener;
import cc.stormworth.meetup.tasks.BroadcastCheckTask;
import cc.stormworth.meetup.tasks.LeaderboardUpdateTask;
import cc.stormworth.meetup.tasks.ServerStatusTask;
import cc.stormworth.meetup.user.UserData;
import cc.stormworth.meetup.user.statistics.Leaderboard;
import cc.stormworth.meetup.user.statistics.LeaderboardHandler;
import cc.stormworth.meetup.user.statistics.LeaderboardType;
import cc.stormworth.meetup.user.statistics.Statistics;
import cc.stormworth.meetup.util.Colors;
import cc.stormworth.meetup.util.ItemBuilder;
import cc.stormworth.meetup.util.file.ConfigFile;
import cc.stormworth.meetup.util.menu.MenuListeners;
import eu.vortexdev.battlespigot.BattleSpigot;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.plugin.java.JavaPlugin;
import org.spigotmc.SpigotConfig;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

@Getter
@Setter
public class Meetup extends JavaPlugin {

    @Getter
    private static Meetup instance;
    private final Map<LeaderboardType, Leaderboard> leaderboards = new HashMap<>();
    private ConfigFile config;
    private KitEditorMenu kitEditorMenu;
    private DeathListener deathListener;
    private LeaderboardUpdateTask leaderboardUpdateTask = new LeaderboardUpdateTask();
    private LeaderboardHandler leaderboardHandler;

    @Override
    public void onEnable() {
        instance = this;
        loadConfig();

        MeetupMongo.getInstance().init();

        loadProviders();
        loadCommands();
        loadHCF();
        loadListeners();
        loadScenarios();
        loadTasks();
        loadOther();

        this.getServer().getPluginManager().registerEvents(new AsyncPlayerPreLoginListener(), this);
        this.getServer().getPluginManager().registerEvents(new PlayerJoinListener(), this);
        this.getServer().getPluginManager().registerEvents(new PlayerQuitListener(), this);

        WorldManager.getInstance().deleteGameWorld();
        WorldManager.getInstance().generateGameWorld();
        BattleSpigot.INSTANCE.addMovementHandler(new GlassMoveHandler());

        CorePlugin.getInstance().setServerDataProvider(new MeetupServerDataProvider());
        leaderboardUpdateTask.runTaskTimerAsynchronously(this, 40L, 5L * 60L * 20L);

        loadLeaderboard();

        Bukkit.getLogger().log(Level.INFO, "[Meetup] The plugin has been enabled.");
    }

    @Override
    public void onDisable() {
        Bukkit.getOnlinePlayers().forEach(online -> {
            UserData userData = UserManager.getInstance().getUser(online.getUniqueId());

            MeetupMongo.getInstance().storeStatistics(online.getName(), online.getUniqueId(), userData.getStatistics(), userData.getHcfSortation().serialize());
        });
    }

    private void loadCommands() {
        getCommand("config").setExecutor(new ConfigCommand());
        getCommand("forcestart").setExecutor(new ForceStartCommand());
        getCommand("invsee").setExecutor(new InvSeeCommand());
        getCommand("editkit").setExecutor(new KitCommand());
        getCommand("scenarios").setExecutor(new ScenariosCommand());
        getCommand("stats").setExecutor(new StatsCommand());
        getCommand("reroll").setExecutor(new RerollCommand());
        getCommand("leaderboard").setExecutor(new LeaderboardCommand());
        getCommand("health").setExecutor(new HealthCommand());
        getCommand("team").setExecutor(new TeamCommand());
        getCommand("teaminventory").setExecutor(new TeamInventoryCommand());
        getCommand("gamestyle").setExecutor(new GameStyleCommand());
        getCommand("addstats").setExecutor(new AddStatsCommand());
        getCommand("setrotativehologram").setExecutor(new SetRotativeHologramCommand());

        rCommandHandler.registerPackage(this, "cc.stormworth.meetup.style.hcf.kit.commands");
        rCommandHandler.registerPackage(this, "cc.stormworth.meetup.style.hcf.enchantment.command");
    }

    private void loadConfig() {
        this.config = new ConfigFile(this, "config.yml");

        SpigotConfig.instantRespawn = true;
    }

    private void loadHCF() {
        Ability.init();
        Kit.loadKits();
        //new HCFKitManager();
        Bukkit.getScheduler().runTaskTimer(this, new BlinderRunnable(), 0, 5L);
    }

    private void loadLeaderboard() {
        for (LeaderboardType type : LeaderboardType.values())
            this.leaderboards.put(type, new Leaderboard(type));

        this.leaderboards.values().forEach(leaderboard -> {
            leaderboard.load(true);
            System.out.println("loading leaderboards");
        });

        this.leaderboardHandler = new LeaderboardHandler();
    }

    private void loadListeners() {
        this.deathListener = new DeathListener();
        Arrays.asList(new MenuListeners(),
                new AsyncPlayerChatListener(),
                deathListener, new GameListener(),
                new InventoryListener(),
                new ItemListener(),
                new LobbyListener(),
                new SpectatorListener(),
                new HCFEditorListener(),
                new ProfileListener(),
                kitEditorMenu = new KitEditorMenu(),
                new EnderPearlCooldown()
        ).forEach(listener -> getServer().getPluginManager().registerEvents(listener, this));
    }

    private void loadProviders() {

        CorePlugin.getInstance().getNametagEngine().registerProvider(new MeetupNametagProvider());

        ScoreboardHandler scoreboard = new ScoreboardHandler(this, new ScoreboardProvider());
        scoreboard.setTicks(2);
        scoreboard.setStyle(ScoreboardStyle.MODERN);

//        CorePlugin.setScoreboardHandler(scoreboard);

        CorePlugin.getInstance().getTabEngine().setLayoutProvider(new TablistProvider());
    }

    private void loadScenarios() {
        new ScenarioManager();
    }

    private void loadTasks() {
        new ServerStatusTask();
        new BroadcastCheckTask();

        new CustomEnchantsTask();
    }

    private void loadOther() {
        Bukkit.addRecipe((new ShapedRecipe(
                (new ItemBuilder(Material.GOLDEN_APPLE)).setName(Colors.GOLD + Colors.BOLD + "Golden Head").build()))
                .shape("EEE", "ERE", "EEE").setIngredient('E', Material.GOLD_INGOT)
                .setIngredient('R', Material.SKULL_ITEM, 3));
    }
}
