package cc.stormworth.meetup.listener;

import cc.stormworth.meetup.database.MeetupMongo;
import cc.stormworth.meetup.managers.GameManager;
import cc.stormworth.meetup.managers.PlayerManager;
import cc.stormworth.meetup.managers.UserManager;
import cc.stormworth.meetup.user.UserData;
import cc.stormworth.meetup.util.TaskUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public final class PlayerQuitListener implements Listener {

    @EventHandler
    public void onPlayerQuitEvent(PlayerQuitEvent ev) {
        Player player = ev.getPlayer();

        UserData userData = UserManager.getInstance().getUser(player.getUniqueId());

        ev.setQuitMessage(null);

        if (userData == null) return;

        TaskUtil.runAsync(() -> MeetupMongo.getInstance().storeStatistics(
                userData.getName(),
                userData.getUniqueId(),
                userData.getStatistics(),
                userData.getHcfSortation().serialize()
        ));

        switch (GameManager.getInstance().getGameState()) {
            case WAITING:
            case STARTING:
                GameManager.getInstance().handleLobbyLeave();

                PlayerManager.getInstance().handleLobbyLeave(player);

                break;
            case SCATTER:
                PlayerManager.getInstance().handleScatterLeave(player);

                break;
            case STARTED:
            case ENDING:
                PlayerManager.getInstance().handleIngameLeave(player);

                break;
        }
    }
}