package cc.stormworth.meetup.tasks;

import cc.stormworth.meetup.Meetup;
import cc.stormworth.meetup.managers.GameManager;
import cc.stormworth.meetup.managers.TeamManager;
import cc.stormworth.meetup.managers.UserManager;
import cc.stormworth.meetup.states.PlayerState;
import cc.stormworth.meetup.team.Team;
import cc.stormworth.meetup.user.UserData;
import cc.stormworth.meetup.util.Colors;
import cc.stormworth.meetup.util.LocationUtil;
import cc.stormworth.meetup.util.PlayerUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;

public class TeamScatterTask extends BukkitRunnable {

    private final List<Team> teamsToScatter = new ArrayList<>();
    private int teamsScattered = 0;
    private final int scatterFrequency;

    public TeamScatterTask() {
        teamsToScatter.addAll(TeamManager.getInstance().getOnlineTeams());
        this.scatterFrequency = teamsToScatter.size() > 15 ? 10 : 20;

        new StartingTask();

        runTaskTimer(Meetup.getInstance(), 0L, this.scatterFrequency);
    }

    @Override
    public void run() {

        if (this.teamsScattered == this.teamsToScatter.size()) {
            Bukkit.getLogger().log(Level.INFO,
                    "[Meetup] Successfully finished the scatter! Teams scattered: " + teamsScattered);
            this.cancel();
            return;
        }

        Team t = this.teamsToScatter.get(this.teamsScattered);

        if (t == null) {
            this.teamsScattered++;
            return;
        }

        Location loc = LocationUtil.getRandomScatterLocation(Bukkit.getWorld("game_world"),
                GameManager.getInstance().getBorder().getSize());
        for (UUID u : t.getMembers()) {
            Player p = Bukkit.getPlayer(u);
            if (p == null) continue;
            UserData user = UserManager.getInstance().getUser(u);
            user.setPlayerState(PlayerState.SCATTERED);
            GameManager.getInstance().getAlivePlayers().add(u);
            GameManager.getInstance().setTotalPlayers(GameManager.getInstance().getTotalPlayers() + 1);

            PlayerUtil.scatterPlayer(p, loc);
            p.sendMessage(Colors.SECONDARY + "You have been scattered.");
        }

        this.teamsScattered++;
        GameManager.getInstance().setTotalTeams(GameManager.getInstance().getTotalTeams() + 1);
    }
}
