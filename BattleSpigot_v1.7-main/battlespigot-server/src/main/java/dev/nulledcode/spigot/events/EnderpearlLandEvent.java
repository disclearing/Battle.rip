package dev.nulledcode.spigot.events;

import org.bukkit.entity.EnderPearl;
import org.bukkit.entity.Entity;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityEvent;

public class EnderpearlLandEvent extends EntityEvent implements Cancellable {
    private static final HandlerList handlers;

    static {
        handlers = new HandlerList();
    }

    private final Reason reason;
    private final Entity hit;
    private boolean cancel;

    public EnderpearlLandEvent(final EnderPearl enderPearl, final Reason reason) {
        this(enderPearl, reason, null);
    }

    public EnderpearlLandEvent(final EnderPearl enderPearl, final Reason reason, final Entity hit) {
        super(enderPearl);
        this.reason = reason;
        this.hit = hit;
    }

    public static HandlerList getHandlerList() {
        return EnderpearlLandEvent.handlers;
    }

    @Override
    public EnderPearl getEntity() {
        return (EnderPearl) this.entity;
    }

    public Reason getReason() {
        return this.reason;
    }

    public Entity getHit() {
        return this.hit;
    }

    @Override
    public HandlerList getHandlers() {
        return EnderpearlLandEvent.handlers;
    }

    @Override
    public boolean isCancelled() {
        return this.cancel;
    }

    @Override
    public void setCancelled(final boolean cancel) {
        this.cancel = cancel;
    }

    public enum Reason {
        BLOCK,
        ENTITY;
    }
}