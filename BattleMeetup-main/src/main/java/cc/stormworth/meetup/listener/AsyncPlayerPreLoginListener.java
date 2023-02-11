package cc.stormworth.meetup.listener;

import cc.stormworth.core.profile.Profile;
import cc.stormworth.core.punishment.Punishment;
import cc.stormworth.meetup.managers.GameManager;
import cc.stormworth.meetup.managers.UserManager;
import cc.stormworth.meetup.states.GameState;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;

public final class AsyncPlayerPreLoginListener implements Listener {

    @EventHandler
    public void onAsyncPlayerPreLoginEvent(AsyncPlayerPreLoginEvent event) {
        Profile profile = Profile.getWithoutLoad(event.getUniqueId());

        Punishment punishment = profile.getServerBlacklistedPunishment();

        if (punishment == null) punishment = profile.getBannedPunishment();

        if (punishment != null) {
            event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, punishment.getType().getMessage()
                    .replace("%REASON%", punishment.getAddedReason())
                    .replace("%DURATION%", punishment.getTimeLeft())
            );

            return;
        }

        if (GameManager.getInstance().getGameState() == GameState.OFFLINE) {
            event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, ChatColor.RED + "This server is currently offline.");
            return;
        }

        UserManager.getInstance().loadUser(event.getUniqueId());
    }
}