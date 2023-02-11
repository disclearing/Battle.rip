package cc.stormworth.meetup.events;

import cc.stormworth.meetup.team.Team;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class TeamWinEvent extends Event {

    private static final HandlerList handlers = new HandlerList();
    private final Team winningTeam;
    public TeamWinEvent(Team winningTeam) {
        this.winningTeam = winningTeam;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public Team getWinningTeam() {
        return winningTeam;
    }
}
