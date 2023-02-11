package cc.stormworth.meetup.managers;

import cc.stormworth.meetup.Meetup;
import cc.stormworth.meetup.states.GameState;
import cc.stormworth.meetup.util.TaskUtil;
import cc.stormworth.meetup.util.file.FileUtil;
import org.bukkit.Bukkit;
import org.bukkit.Difficulty;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;

import java.io.File;
import java.util.concurrent.ThreadLocalRandom;
import java.util.logging.Level;

public class WorldManager {

    private static WorldManager instance;

    public static WorldManager getInstance() {

        if (instance == null) {
            instance = new WorldManager();
        }

        return instance;
    }

    public void deleteGameWorld() {
        World world = Bukkit.getWorld("game_world");

        if (world == null) {
            Bukkit.getLogger().log(Level.WARNING, "[Meetup] Unable to unload world 'game_world'!");
            Bukkit.getLogger().log(Level.WARNING, "[Meetup] Attempting to delete the directory regardless...");

            File file = new File(Bukkit.getWorldContainer(), "game_world");

            if (file == null) {
                Bukkit.getLogger().log(Level.WARNING, "[Meetup] Unable to delete directory 'game_world'!");
                return;
            }

            FileUtil.deleteDirectory(file);
            Bukkit.getLogger().log(Level.WARNING, "[Meetup] Deleted directory 'game_world'!");
            return;
        }

        Bukkit.unloadWorld(world.getName(), false);
        File file = new File(Bukkit.getWorldContainer(), world.getName());
        FileUtil.deleteDirectory(file);
        Bukkit.getLogger().log(Level.WARNING, "[Meetup] Deleted directory 'game_world'!");
    }

    public void generateGameWorld() {
        int netherMeetup = ThreadLocalRandom.current().nextInt(100);
        int caveMeetup = ThreadLocalRandom.current().nextInt(100);

        TaskUtil.runLater(() -> {
            String gameType;
            World.Environment environment;
            if (Meetup.getInstance().getConfig().getBoolean("Game.Nether", false) || netherMeetup <= 10) {
                gameType = "NETHER";
                environment = World.Environment.NETHER;
            } else if (Meetup.getInstance().getConfig().getBoolean("Game.Cave", false) || caveMeetup <= 10) {
                gameType = "CAVE";
                environment = World.Environment.NORMAL;
            } else {
                gameType = "NORMAL";
                environment = World.Environment.NORMAL;
            }
            World world = Bukkit.createWorld(new WorldCreator("game_world").environment(environment));
            world.setMetadata("gameType", new FixedMetadataValue(Meetup.getInstance(), gameType));
            world.setGameRuleValue("announceAdvancements", "false");
            world.setGameRuleValue("doDaylightCycle", "false");
            world.setGameRuleValue("doFireTick", "false");
            // TODO: HCF?
            world.setGameRuleValue("naturalRegeneration", "false");
            world.setDifficulty(Difficulty.NORMAL);

            loadGameWorld();
        }, 3 * 20L);
    }

    private void loadGameWorld() {
        Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "wb game_world set 250 250 0 0");
        Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "wb shape square");
        Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "wb whoosh off");
        Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "wb portal off");
        Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "wb denypearl on");
        Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "wb setmsg &cYou have reached the border.");
        GameManager.getInstance().setGameState(GameState.WAITING);
    }

    public boolean isNetherGame() {
        return Bukkit.getWorld("game_world").getEnvironment() == World.Environment.NETHER;
    }
}
