package cc.stormworth.meetup.providers;

import cc.stormworth.core.CorePlugin;
import cc.stormworth.core.util.chat.CC;
import cc.stormworth.core.util.scoreboard.ScoreboardAdapter;
import cc.stormworth.meetup.managers.GameManager;
import cc.stormworth.meetup.managers.TeamManager;
import cc.stormworth.meetup.managers.UserManager;
import cc.stormworth.meetup.scenarios.Scenario;
import cc.stormworth.meetup.scenarios.impl.NoCleanPlusScenario;
import cc.stormworth.meetup.states.Mode;
import cc.stormworth.meetup.states.PlayerState;
import cc.stormworth.meetup.team.Team;
import cc.stormworth.meetup.user.UserData;
import cc.stormworth.meetup.util.EloCalculator;
import cc.stormworth.meetup.util.ServerUtil;
import cc.stormworth.meetup.util.TimeUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.TimeZone;

public class ScoreboardProvider implements ScoreboardAdapter {

    public String getTitle(Player p) {
        return CC.B_PRIMARY + " Meetup";
    }

    @Override
    public List<String> getLines(Player player) {
        final LinkedList<String> board = new LinkedList<>();

        UserData user = UserManager.getInstance().getUser(player.getUniqueId());

        int elo = user == null ? 1400 : user.getStatistics().getElo();
        EloCalculator.Division division = GameManager.getInstance().getEloCalculator().getDivision(elo);

        switch (GameManager.getInstance().getGameState()) {

            case WAITING:
                board.add("Players: " + CC.SECONDARY + Bukkit.getOnlinePlayers().size());
                board.add("Mode: " + CC.SECONDARY + GameManager.getInstance().getMode().toString());
                board.add("Division: " + CC.SECONDARY + division.getColor() + division.getName());
                board.add(" ");
                board.add("Waiting for ");
                board.add(CC.SECONDARY + (GameManager.getInstance().getRequiredPlayers() - Bukkit.getOnlinePlayers().size()) + " more player" + (GameManager.getInstance().getRequiredPlayers() - Bukkit.getOnlinePlayers().size() > 1 ? "s" : ""));
                break;
            case STARTING:
                board.add("Players: " + CC.SECONDARY + Bukkit.getOnlinePlayers().size());
                board.add("Mode: " + CC.SECONDARY + GameManager.getInstance().getMode().toString());
                board.add("Scattering in: " + CC.SECONDARY + GameManager.getInstance().getScatteringIn());
                break;
            case SCATTER:
                board.add("Players: " + CC.SECONDARY + Bukkit.getOnlinePlayers().size());
                board.add("Mode: " + CC.SECONDARY + GameManager.getInstance().getMode().toString());
                board.add("Starting in: " + CC.SECONDARY + GameManager.getInstance().getStartingIn());
                board.add("Rerolls: " + CC.SECONDARY + (user == null ? 0 : user.getStatistics().getRerolls()));
                break;
            case STARTED:
                board.add("Game Time: " + CC.SECONDARY + TimeUtil.format(GameManager.getInstance().getGameTime()));
                board.add(" ");
                board.add("Rating: " + CC.SECONDARY + division.getColor().toString() + division.getName() + CC.GRAY + " (" + elo + ")");
                if (GameManager.getInstance().getMode() == Mode.TO2) {
                    board.add("Players: " + CC.SECONDARY + GameManager.getInstance().getAlivePlayers().size() + "/" + GameManager.getInstance().getTotalPlayers());
                    board.add("Teams: " + CC.SECONDARY + TeamManager.getInstance().getAliveTeams().size());

                    if (user != null && user.getPlayerState() == PlayerState.INGAME) {
                        Team team = TeamManager.getInstance().getTeam(user.getTeamNumber());

                        board.add("Kills: " + CC.SECONDARY + user.getKills());
                        board.add("Team Kills: " + CC.SECONDARY + team.getKills());

                        if (Scenario.getByName("NoClean").isActive() && user.isInvincible()) {
                            double timer = Double.parseDouble(user.getInvincibilityTimer().getTimeLeft());
                            board.add("Invincibility: " + CC.SECONDARY + timer);
                        }
                    }
                } else {
                    board.add("Players: " + CC.SECONDARY + GameManager.getInstance().getAlivePlayers().size() + "/" + GameManager.getInstance().getTotalPlayers());
                    if (user != null && user.getPlayerState() == PlayerState.INGAME) {
                        board.add("Kills: " + CC.SECONDARY + user.getKills());

                        if (Scenario.getByName("NoClean").isActive() && user.isInvincible()) {
                            double timer = Double.parseDouble(user.getInvincibilityTimer().getTimeLeft());
                            board.add("Invincibility: " + CC.SECONDARY + timer);
                        } else if (Scenario.getByName("NoClean+").isActive()
                                && NoCleanPlusScenario.isDisturbActive(player.getUniqueId())) {
                            double timer = NoCleanPlusScenario.getDisturbMillisecondsLeft(player.getUniqueId()) * 0.001;
                            board.add("Linked: " + CC.SECONDARY + Math.round(timer * 10.0) / 10.0);
                        }
                    }

                }
                board.add("Border: " + CC.SECONDARY + (GameManager.getInstance().getBorder().getSize()
                        + GameManager.getInstance().getFormattedBorderStatus()));
                break;
            case ENDING:
                if (GameManager.getInstance().getMode() == Mode.TO2) {
                    board.add("Winning Team:");
                    GameManager.getInstance().getWinningTeam().getMembersAsOfflinePlayers().forEach(m -> board.add(CC.GRAY + " ● " + CC.SECONDARY + m.getName()));
                } else {
                    board.add("Winner: " + CC.SECONDARY + GameManager.getInstance().getWinner());
                }
                board.add("Game Time: " + CC.SECONDARY + TimeUtil.format(GameManager.getInstance().getFinalGameTime()));
                board.add("Total Players: " + CC.SECONDARY + GameManager.getInstance().getTotalPlayers());

                if (GameManager.getInstance().getMode() == Mode.TO2) {
                    board.add("Total Teams: " + CC.SECONDARY + GameManager.getInstance().getTotalTeams());
                }

                if (user != null && user.getPlayerState() == PlayerState.INGAME) {
                    board.add("Kills: " + CC.SECONDARY + user.getKills());

                    if (GameManager.getInstance().getMode() == Mode.TO2) {
                        board.add("Team Kills: " + CC.SECONDARY + TeamManager.getInstance().getTeam(user.getTeamNumber()).getKills());
                    }
                }
                break;

        }

        board.add(" ");
        boolean utc = CorePlugin.getInstance().getConfigFile().getConfig().getString("proxy").equals("eu");
        date.setTime(System.currentTimeMillis());
        format.setTimeZone(TimeZone.getTimeZone(utc ? "UTC" : "EST"));

        board.add(CC.GRAY + format.format(date) + (utc ? " UTC" : " EST"));

        board.addFirst("&2&7&m--------------------");
        board.add("&3&7&m--------------------");
        board.add(CC.PRIMARY + ServerUtil.getIP());

        return board;
    }
    private final Date date = new Date();
    private final SimpleDateFormat format = new SimpleDateFormat("MMMM d, HH:mm");

}