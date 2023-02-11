package cc.stormworth.meetup.tasks;

import cc.stormworth.meetup.Meetup;
import cc.stormworth.meetup.managers.GameManager;
import net.minecraft.server.v1_7_R4.MinecraftServer;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

public class ServerStatusTask extends BukkitRunnable {

    public ServerStatusTask() {
        runTaskTimer(Meetup.getInstance(), 20L, 20L);
    }

    @Override
    public void run() {
        String serverStatus = GameManager.getInstance().getGameState().toString() + ";" + GameManager.getInstance().getMode().toString() + ";" + Bukkit.getOnlinePlayers().size() + ";";
        MinecraftServer.getServer().setMotd(serverStatus);
    }
}
