package cc.stormworth.meetup.team;

import cc.stormworth.core.CorePlugin;
import cc.stormworth.core.CorePluginAPI;
import cc.stormworth.core.util.chat.CC;
import cc.stormworth.meetup.managers.TeamManager;
import cc.stormworth.meetup.managers.UserManager;
import cc.stormworth.meetup.user.UserData;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Getter
public class Team {

    private final int number;
    private final ChatColor color;
    private final Inventory backpack;
    @Setter
    private int kills;

    @Setter
    private UUID leader;
    private final List<UUID> members;
    private final List<UUID> aliveMembers;

    public Team(UUID leader, int number) {
        this.leader = leader;
        this.number = number;
        this.color = TeamManager.getInstance().getRandomColor();
        this.backpack = Bukkit.createInventory(null, 27, CC.SECONDARY + "Backpack " + CC.PRIMARY + "#" + this.number);

        this.kills = 0;
        this.members = new ArrayList<>();
        this.aliveMembers = new ArrayList<>();

        TeamManager.getInstance().getTeams().put(this.number, this);
    }

    public List<Player> getOnlineMembers() {
        return this.members.stream()
                .map(uuid -> Bukkit.getPlayer(uuid))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    public List<UUID> getAliveMembers() {
        return this.members.stream()
                .map(uuid -> UserManager.getInstance().getUser(uuid))
                .filter(user -> user.isAlive())
                .map(user -> user.getUniqueId())
                .collect(Collectors.toList());
    }

    public boolean isAlive() {
        return (this.getAliveMembers().size() > 0);
    }

    public boolean isOnline() {
        return (this.getOnlineMembers().size() > 0);
    }

    public Player getLeaderAsPlayer() {
        return Bukkit.getPlayer(this.leader);
    }

    public List<OfflinePlayer> getMembersAsOfflinePlayers() {
        return this.members.stream().map(uuid -> Bukkit.getOfflinePlayer(uuid)).collect(Collectors.toList());
    }

    public void addMember(Player member) {
        UserData user = UserManager.getInstance().getUser(member.getUniqueId());

        if (this.members.size() == 2) {
            member.sendMessage(CC.RED + "This team is already full.");
            return;
        }

        user.setTeamNumber(this.number);
        this.members.add(member.getUniqueId());

        this.getOnlineMembers().forEach(p -> {
            p.sendMessage(CorePluginAPI.getProfile(member.getUniqueId()).getColoredUsername() + CC.SECONDARY + " has joined the team" + CC.GRAY + " (" + CC.PRIMARY + this.members.size() + "/" + 2 + CC.GRAY + ")" + CC.SECONDARY + ".");


            CorePlugin.getInstance().getNametagEngine().reloadPlayer(p);
            CorePlugin.getInstance().getNametagEngine().reloadPlayer(member);
            //ClientAPI.sendTeammates(p, Iterables.toArray(this.getOnlineMembers(), Player.class));
        });
    }

    public void removeMember(OfflinePlayer offlineMember, boolean kick) {
        UserData user = UserManager.getInstance().getUser(offlineMember.getUniqueId());

        user.setTeamNumber(-1);
        this.members.remove(offlineMember.getUniqueId());
        boolean disband = (this.members.size() == 0);

        if (offlineMember.isOnline()) {
            offlineMember.getPlayer().sendMessage(CorePluginAPI.getProfile(offlineMember.getUniqueId()).getColoredUsername() + CC.SECONDARY + " has" + (kick ? " been kicked from" : " left") + " the team" + CC.GRAY + " (" + CC.PRIMARY + this.members.size() + "/" + 2 + CC.GRAY + ")" + CC.SECONDARY + ".");
            //ClientAPI.removeTeammates(offlineMember.getPlayer());
            if (disband) offlineMember.getPlayer().sendMessage(CC.SECONDARY + "Your team has been disbanded.");
        }

        this.getOnlineMembers().forEach(p -> {
            p.sendMessage(CorePluginAPI.getProfile(offlineMember.getUniqueId()).getColoredUsername() + CC.SECONDARY + " has" + (kick ? " been kicked from" : " left") + " the team" + CC.GRAY + " (" + CC.PRIMARY + this.members.size() + "/" + 2 + CC.GRAY + ")" + CC.SECONDARY + ".");

            if (offlineMember.isOnline()) {

                CorePlugin.getInstance().getNametagEngine().reloadPlayer(p);
                CorePlugin.getInstance().getNametagEngine().reloadPlayer(offlineMember.getPlayer());

                //ClientAPI.sendTeammates(p, Iterables.toArray(this.getOnlineMembers(), Player.class));
            }
        });

        if (disband) {
            this.disband();
            return;
        }

        if (offlineMember.getUniqueId().equals(this.leader)) {
            this.leader = this.getOnlineMembers().get(0).getUniqueId();
            this.broadcast(CorePluginAPI.getProfile(this.leader).getColoredUsername() + CC.SECONDARY + " is now the leader of your team.");
        }
    }

    public void sendInvitation(Player target) {
        TeamInvitation invitation = new TeamInvitation(this, target);
        invitation.send();
    }

    public void broadcast(String message) {
        this.getOnlineMembers().forEach(o -> o.sendMessage(message));
    }

    private void disband() {
        TeamManager.getInstance().getTeams().remove(this.number);
        TeamManager.getInstance().decrementTeamCount();
    }

    public boolean isMember(UUID uniqueId) {
        return this.members.contains(uniqueId);
    }

    public void chat(Player player, String message) {
        this.getOnlineMembers().forEach(p -> p.sendMessage(CC.SECONDARY + player.getName() + CC.PRIMARY + ": " + message));
    }
}
