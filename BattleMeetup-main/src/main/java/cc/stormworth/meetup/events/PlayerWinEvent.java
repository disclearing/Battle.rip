package cc.stormworth.meetup.events;

import cc.stormworth.meetup.user.UserData;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlayerWinEvent extends Event {

    private static final HandlerList handlers = new HandlerList();
    private final UserData winner;
    public PlayerWinEvent(UserData winner) {
        this.winner = winner;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public UserData getWinner() {
        return winner;
    }
}
