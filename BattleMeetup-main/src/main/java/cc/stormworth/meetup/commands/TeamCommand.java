package cc.stormworth.meetup.commands;

import cc.stormworth.core.CorePluginAPI;
import cc.stormworth.core.util.chat.CC;
import cc.stormworth.meetup.managers.GameManager;
import cc.stormworth.meetup.managers.TeamManager;
import cc.stormworth.meetup.managers.UserManager;
import cc.stormworth.meetup.states.Mode;
import cc.stormworth.meetup.team.Team;
import cc.stormworth.meetup.team.TeamInvitation;
import cc.stormworth.meetup.user.UserData;
import cc.stormworth.meetup.util.Msg;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TeamCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {

        if (!(commandSender instanceof Player)) {
            Msg.logConsole(CC.SECONDARY + "This" + CC.PRIMARY + " Meetup" + CC.SECONDARY + " command can only be performed by players.");
            return false;
        }

        Player p = (Player) commandSender;
        UserData user = UserManager.getInstance().getUser(p.getUniqueId());

        if (GameManager.getInstance().getMode() == Mode.FFA) {
            p.sendMessage(CC.RED + "Teams are disabled.");
            return false;
        }

        if (args.length == 0) {
            p.sendMessage(CC.PRIMARY + "Team Commands");
            p.sendMessage(CC.GRAY + " " + '●' + CC.SECONDARY + " /team invite <player>");
            p.sendMessage(CC.GRAY + " " + '●' + CC.SECONDARY + " /team accept");
            p.sendMessage(CC.GRAY + " " + '●' + CC.SECONDARY + " /team deny");
            p.sendMessage(CC.GRAY + " " + '●' + CC.SECONDARY + " /team leave");
            p.sendMessage(CC.GRAY + " " + '●' + CC.SECONDARY + " /team kick <player>");
            p.sendMessage(CC.GRAY + " " + '●' + CC.SECONDARY + " /team promote <player>");
            p.sendMessage(CC.GRAY + " " + '●' + CC.SECONDARY + " /team list <player>");
            p.sendMessage(CC.GRAY + " " + '●' + CC.SECONDARY + " /team chat");
            return false;
        }

        switch (args[0].toLowerCase()) {
            default:
                break;
            case "invite":

                if (!GameManager.getInstance().isLobby()) {
                    p.sendMessage(CC.RED + "You cannot use this command right now.");
                    return false;
                }

                if (args.length != 2) {
                    p.sendMessage(CC.RED + "Usage: /team invite <player>");
                    return false;
                }

                Player target = Bukkit.getPlayer(args[1]);

                if (target == null) {
                    p.sendMessage(CC.RED + "This player is offline.");
                    return false;
                }

                if (target.getUniqueId().equals(p.getUniqueId())) {
                    p.sendMessage(CC.RED + "You cannot invite yourself.");
                    return false;
                }

                if (!user.hasTeam()) {
                    TeamManager.getInstance().incrementTeamCount();

                    Team team = new Team(p.getUniqueId(), TeamManager.getInstance().getTeamCount());
                    team.addMember(p);
                }

                Team team = TeamManager.getInstance().getTeam(user.getTeamNumber());

                if (TeamManager.getInstance().getTeamInvitations().get(target.getUniqueId()) != null) {
                    TeamInvitation teamInvitation = TeamManager.getInstance().getTeamInvitations().get(target.getUniqueId());

                    if (teamInvitation.getTeam().getNumber() == team.getNumber()) {
                        p.sendMessage(CC.RED + "You have already sent a team invitation to this player.");
                        return false;
                    }
                }

                team.sendInvitation(target);
                break;
            case "accept":

                if (!GameManager.getInstance().isLobby()) {
                    p.sendMessage(CC.RED + "You cannot use this command right now.");
                    return false;
                }

                if (user.hasTeam()) {
                    p.sendMessage(CC.RED + "You are already in a team.");
                    return false;
                }

                TeamInvitation teamInvitation = TeamManager.getInstance().getTeamInvitations().get(p.getUniqueId());

                if (teamInvitation == null) {
                    p.sendMessage(CC.RED + "You don't have any pending team invitations.");
                    return false;
                }

                teamInvitation.accept();
                break;
            case "deny":

                if (!GameManager.getInstance().isLobby()) {
                    p.sendMessage(CC.RED + "You cannot use this command right now.");
                    return false;
                }

                TeamInvitation teamInvitation1 = TeamManager.getInstance().getTeamInvitations().get(p.getUniqueId());

                if (teamInvitation1 == null) {
                    p.sendMessage(CC.RED + "You don't have any pending team invitations.");
                    return false;
                }

                teamInvitation1.deny();
                break;
            case "leave":

                if (!GameManager.getInstance().isLobby()) {
                    p.sendMessage(CC.RED + "You cannot use this command right now.");
                    return false;
                }

                if (!user.hasTeam()) {
                    p.sendMessage(CC.RED + "You are not in a team.");
                    return false;
                }

                Team team1 = TeamManager.getInstance().getTeam(user.getTeamNumber());
                team1.removeMember(p, false);
                break;
            case "kick":

                if (!GameManager.getInstance().isLobby()) {
                    p.sendMessage(CC.RED + "You cannot use this command right now.");
                    return false;
                }

                if (args.length != 2) {
                    p.sendMessage(CC.RED + "Usage: /team kick <player>");
                    return false;
                }

                if (!user.hasTeam()) {
                    p.sendMessage(CC.RED + "You are not in a team.");
                    return false;
                }

                Team team2 = TeamManager.getInstance().getTeam(user.getTeamNumber());

                if (!team2.getLeader().equals(p.getUniqueId())) {
                    p.sendMessage(CC.RED + "Only the team leader can kick players from the team.");
                    return false;
                }

                OfflinePlayer offlineTarget = Bukkit.getOfflinePlayer(args[1]);

                if (!team2.getMembers().contains(offlineTarget.getUniqueId())) {
                    p.sendMessage(CC.RED + "This player is not in your team.");
                    return false;
                }

                if (offlineTarget.getUniqueId().equals(p.getUniqueId())) {
                    p.sendMessage(CC.RED + "You cannot kick yourself from the team.");
                    return false;
                }

                team2.removeMember(offlineTarget, true);
                break;
            case "promote":

                if (args.length != 2) {
                    p.sendMessage(CC.RED + "Usage: /team promote <player>");
                    return false;
                }

                if (!user.hasTeam()) {
                    p.sendMessage(CC.RED + "You are not in a team.");
                    return false;
                }

                Team team3 = TeamManager.getInstance().getTeam(user.getTeamNumber());

                if (!team3.getLeader().equals(p.getUniqueId())) {
                    p.sendMessage(CC.RED + "Only the team leader can promote players.");
                    return false;
                }

                OfflinePlayer offlineTarget1 = Bukkit.getOfflinePlayer(args[1]);

                if (!team3.getMembers().contains(offlineTarget1.getUniqueId())) {
                    p.sendMessage(CC.RED + "This player is not in your team.");
                    return false;
                }

                if (offlineTarget1.getUniqueId().equals(p.getUniqueId())) {
                    p.sendMessage(CC.RED + "You cannot promote yourself.");
                    return false;
                }

                team3.setLeader(offlineTarget1.getUniqueId());
                team3.broadcast(CC.PRIMARY + offlineTarget1.getName() + CC.SECONDARY + " is now the leader of your team.");
                break;
            case "list":

                if (args.length == 1) {

                    if (!user.hasTeam()) {
                        p.sendMessage(CC.RED + "You are not in a team.");
                        return false;
                    }

                    Team team4 = TeamManager.getInstance().getTeam(user.getTeamNumber());
                    StringBuilder sb = new StringBuilder();

                    team4.getMembersAsOfflinePlayers().forEach(o -> {
                        if (sb.length() > 0)
                            sb.append(CC.SECONDARY).append(", ");
                        UserData memberUser = UserManager.getInstance().getUser(o.getUniqueId());
                        sb.append(memberUser.isAlive() ? CC.GREEN : CC.RED).append(memberUser.getName()).append(CC.GRAY).append("[").append(CC.WHITE).append(memberUser.getKills()).append(CC.GRAY).append("]");
                    });

                    p.sendMessage("");
                    p.sendMessage(team4.getColor() + "Team #" + team4.getNumber() + CC.GRAY + " [" + team4.getMembers().size() + "/" + 2 + "]" + CC.GRAY + " -" + CC.SECONDARY + " Kills: " + CC.PRIMARY + team4.getKills());
                    p.sendMessage(CC.SECONDARY + "Members: " + sb);
                    p.sendMessage("");
                    return false;
                }

                OfflinePlayer offlineTarget2 = Bukkit.getOfflinePlayer(args[1]);
                UserData targetUser = UserManager.getInstance().getUser(offlineTarget2.getUniqueId());

                if (targetUser == null) {
                    p.sendMessage(CC.RED + "This player could not be found.");
                    return false;
                }

                if (!targetUser.hasTeam()) {
                    p.sendMessage(CC.RED + "This player is not in a team.");
                    return false;
                }

                Team targetTeam = TeamManager.getInstance().getTeam(targetUser.getTeamNumber());
                StringBuilder sb = new StringBuilder();

                targetTeam.getMembersAsOfflinePlayers().forEach(o -> {
                    if (sb.length() > 0)
                        sb.append(CC.SECONDARY).append(", ");
                    UserData memberUser = UserManager.getInstance().getUser(o.getUniqueId());
                    sb.append(memberUser.isAlive() ? CC.GREEN : CC.RED).append(memberUser.getName()).append(CC.GRAY).append("[").append(CC.WHITE).append(memberUser.getKills()).append(CC.GRAY).append("]");
                });

                p.sendMessage("");
                p.sendMessage(targetTeam.getColor() + "Team #" + targetTeam.getNumber() + CC.GRAY + " [" + targetTeam.getMembers().size() + "/" + 2 + "]" + CC.GRAY + " -" + CC.SECONDARY + " Kills: " + CC.PRIMARY + targetTeam.getKills());
                p.sendMessage(CC.SECONDARY + "Members: " + sb);
                p.sendMessage("");
                break;
            case "chat":

                if (args.length == 1) {
                    p.sendMessage(CC.RED + "Usage: /team chat <message>");
                    return false;
                }

                if (!user.hasTeam()) {
                    p.sendMessage(CC.RED + "You are not in a team.");
                    return false;
                }

                StringBuilder sb1 = new StringBuilder();

                for (int i = 1; i < args.length; i++) {
                    sb1.append(args[i]);
                }

                Team team5 = TeamManager.getInstance().getTeam(user.getTeamNumber());
                String message = CC.GRAY + "[" + CC.PRIMARY + "Team" + CC.PRIMARY + CC.GRAY + "] " + CorePluginAPI.getProfile(p.getUniqueId()).getColoredUsername() + CC.GRAY + ": " + CC.SECONDARY + sb1;
                team5.broadcast(message);
                Msg.logConsole(message);
                break;
        }

        return false;
    }
}
