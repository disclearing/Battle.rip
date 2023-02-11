package cc.stormworth.meetup.listener;

import cc.stormworth.core.CorePlugin;
import cc.stormworth.core.CorePluginAPI;
import cc.stormworth.core.profile.Profile;
import cc.stormworth.core.rank.Rank;
import cc.stormworth.core.util.chat.CC;
import cc.stormworth.meetup.managers.GameManager;
import cc.stormworth.meetup.managers.TeamManager;
import cc.stormworth.meetup.managers.UserManager;
import cc.stormworth.meetup.states.GameState;
import cc.stormworth.meetup.states.PlayerState;
import cc.stormworth.meetup.team.Team;
import cc.stormworth.meetup.user.UserData;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class AsyncPlayerChatListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onAsyncPlayerChatEvent(AsyncPlayerChatEvent ev) {
        Player player = ev.getPlayer();

        UserData userData = UserManager.getInstance().getUser(player.getUniqueId());

        if (userData == null) return;

        Profile profile = Profile.getByUuidIfAvailable(player.getUniqueId());

        if (profile == null || profile.isStaffchat() || profile.isAdminchat()) return;

        if ((profile.getMutedPunishment() != null && profile.getMutedPunishment().isActive())
                || CorePlugin.getInstance().getRedisManager().isChatSilenced()) return;

        ev.setCancelled(true);

        String message = String.format(this.fetchMessage(player, userData.getPlayerState() != PlayerState.SPECTATOR), ev.getMessage());

        Bukkit.getConsoleSender().sendMessage(message);

        if (userData.getPlayerState() == PlayerState.SPECTATOR) {
            for (Player target : Bukkit.getOnlinePlayers()) {
                if (GameManager.getInstance().getGameState() != GameState.ENDING && !UserManager.getInstance().isSpectator(target)) {
                    continue;
                }

                target.sendMessage(CC.GRAY + "[Spectator] " + message);
            }

            return;
        }

        ev.getRecipients().forEach(target -> target.sendMessage(message));
    }

    private String fetchMessage(Player player, boolean showAsDisguised) {
        String message = "";

        Team team = TeamManager.getInstance().getTeam(player);

        if (team != null) message += ChatColor.GRAY + "[" + team.getColor() + "#" + team.getNumber() + "] ";

        Profile profile = Profile.getByUuidIfAvailable(player.getUniqueId());

        String prefix = profile == null ? Rank.DEFAULT.getDisplayName() : (profile.getActiveTag() != null ? profile.getColoredActiveTag() + " " : "") + ((player.isDisguised() && showAsDisguised ? Rank.DEFAULT : profile.getRank()).getPrefix());

        return message + CorePluginAPI.publicChatFormat(player, null, prefix);
    }
}