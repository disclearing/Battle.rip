package cc.stormworth.hcf.misc.gkits.event;

import cc.stormworth.hcf.misc.gkits.Kit;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

public class KitApplyEvent extends PlayerEvent implements Cancellable {
    private static final HandlerList handlers;

    static {
        handlers = new HandlerList();
    }

    private final Kit kit;
    private final boolean force;
    private boolean cancelled;

    public KitApplyEvent(final Kit kit, final Player player, final boolean force) {
        super(player);
        this.cancelled = false;
        this.kit = kit;
        this.force = force;
    }

    public static HandlerList getHandlerList() {
        return KitApplyEvent.handlers;
    }

    public Kit getKit() {
        return this.kit;
    }

    public boolean isForce() {
        return this.force;
    }

    public boolean isCancelled() {
        return this.cancelled;
    }

    public void setCancelled(final boolean cancelled) {
        this.cancelled = cancelled;
    }

    public HandlerList getHandlers() {
        return KitApplyEvent.handlers;
    }
}
