package cc.stormworth.meetup.managers;

import cc.stormworth.meetup.team.Team;
import cc.stormworth.meetup.team.TeamInvitation;
import cc.stormworth.meetup.user.UserData;
import lombok.Getter;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Getter
public class TeamManager {

    private static TeamManager instance;
    private final Map<Integer, Team> teams = new HashMap<>();
    private final Map<UUID, TeamInvitation> teamInvitations = new HashMap<>();
    private int teamCount = 0;
    private int colorNumber = 0;
    private final ChatColor[] teamColors = new ChatColor[]{ChatColor.DARK_GREEN, ChatColor.DARK_PURPLE, ChatColor.GOLD, ChatColor.BLUE, ChatColor.GREEN, ChatColor.RED, ChatColor.LIGHT_PURPLE, ChatColor.YELLOW, ChatColor.WHITE};

    public static TeamManager getInstance() {
        if (instance == null)
            instance = new TeamManager();
        return instance;
    }

    public Team getTeam(int teamNumber) {
        return this.teams.get(teamNumber);
    }

    public Team getTeam(Player player) {
        UserData user = UserManager.getInstance().getUser(player.getUniqueId());

        return getTeam(user.getTeamNumber());
    }

    public List<Team> getAliveTeams() {
        return this.teams.values().stream().filter(Team::isAlive).collect(Collectors.toList());
    }

    public List<Team> getOnlineTeams() {
        return this.teams.values().stream().filter(Team::isOnline).collect(Collectors.toList());
    }

    public ChatColor getRandomColor() {
        return this.teamColors[this.colorNumber++ % this.teamColors.length];
    }

    public void incrementTeamCount() {
        this.teamCount++;
    }

    public void decrementTeamCount() {
        this.teamCount--;
    }
}
