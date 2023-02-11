package cc.stormworth.meetup.team;

import cc.stormworth.core.CorePluginAPI;
import cc.stormworth.core.util.chat.CC;
import cc.stormworth.meetup.managers.TeamManager;
import cc.stormworth.meetup.util.Clickable;
import cc.stormworth.meetup.util.TaskUtil;
import lombok.Getter;
import org.bukkit.entity.Player;

@Getter
public class TeamInvitation {

    private final Team team;
    private final Player target;

    public TeamInvitation(Team team, Player target) {
        this.team = team;
        this.target = target;

        TaskUtil.runLater(() -> this.expire(), 60 * 20L);
    }

    public void send() {
        this.team.getLeaderAsPlayer().sendMessage(CC.SECONDARY + "You have sent a team invitation to " + CorePluginAPI.getProfile(this.target.getUniqueId()).getColoredUsername() + CC.SECONDARY + ".");
        this.target.sendMessage(CorePluginAPI.getProfile(this.team.getLeaderAsPlayer().getUniqueId()).getColoredUsername() + CC.SECONDARY + " has invited you to team" + CC.PRIMARY + " #" + this.team.getNumber() + CC.SECONDARY + ".");
        new Clickable(CC.SECONDARY + "Click on this message or type" + CC.PRIMARY + " '/team accept'" + CC.SECONDARY + " to accept.", CC.SECONDARY + "Click to join this team!", "/team accept")
                .sendToPlayer(this.target);

        TeamManager.getInstance().getTeamInvitations().put(target.getUniqueId(), this);
    }

    public void accept() {
        this.team.addMember(this.target);

        TeamManager.getInstance().getTeamInvitations().remove(target.getUniqueId());
    }

    public void deny() {
        this.target.sendMessage(CC.SECONDARY + "You have denied the team invitation from team" + CC.PRIMARY + " #" + this.team.getNumber() + CC.SECONDARY + ".");
        this.team.getLeaderAsPlayer().sendMessage(CorePluginAPI.getProfile(target.getUniqueId()).getColoredUsername() + CC.SECONDARY + " has denied your team invitation.");

        TeamManager.getInstance().getTeamInvitations().remove(target.getUniqueId());
    }

    public void expire() {

        if (TeamManager.getInstance().getTeamInvitations().containsKey(target.getUniqueId())) {
            this.target.sendMessage(CC.SECONDARY + "The team invitation from " + CorePluginAPI.getProfile(this.team.getLeaderAsPlayer().getUniqueId()).getColoredUsername() + CC.GRAY + " [" + this.team.getColor() + "#" + this.team.getNumber() + CC.GRAY + "]" + CC.SECONDARY + " has expired.");
            this.team.getLeaderAsPlayer().sendMessage(CC.SECONDARY + "Your team invitation to " + CorePluginAPI.getProfile(this.target.getUniqueId()).getColoredUsername() + CC.SECONDARY + " has expired.");

            TeamManager.getInstance().getTeamInvitations().remove(target.getUniqueId());
        }
    }
}
