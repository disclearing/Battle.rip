package cc.stormworth.meetup.listener;

import cc.stormworth.core.CorePlugin;
import cc.stormworth.core.profile.Profile;
import cc.stormworth.core.rank.Rank;
import cc.stormworth.meetup.managers.GameManager;
import cc.stormworth.meetup.managers.PlayerManager;
import cc.stormworth.meetup.managers.UserManager;
import cc.stormworth.meetup.style.Style;
import cc.stormworth.meetup.util.TaskUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public final class PlayerJoinListener implements Listener {

    @EventHandler
    public void onPlayerJoinEvent(PlayerJoinEvent ev) {
        Player player = ev.getPlayer();

        UserManager.getInstance().loadUser(player.getUniqueId());

        TaskUtil.run(() -> CorePlugin.getInstance().nametagEngine.reloadPlayer(player));

        switch (GameManager.getInstance().getGameState()) {
            case STARTING:
            case WAITING:
                GameManager.getInstance().handleLobbyJoin();
                PlayerManager.getInstance().handleLobbyJoin(player);

                break;

            case SCATTER:
                PlayerManager.getInstance().handleScatterJoin(player);

                break;

            case STARTED:
            case ENDING:
                PlayerManager.getInstance().handleIngameJoin(player);

                break;
        }

        if (!Style.getCurrentlyVoted().getPlayersVoted().contains(player.getUniqueId())) {
            UserManager.getInstance().getUser(player.getUniqueId()).setVotePower(Profile.getByPlayer(player).getActiveRank().isAboveOrEqual(Rank.WARRIOR) ? 2 : 1);
        }

        ev.setJoinMessage(null);
    }
}