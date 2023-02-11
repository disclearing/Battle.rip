package org.bukkit.event.entity;

import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Projectile;
import org.bukkit.event.HandlerList;

/**
 * Called when a projectile hits an object
 */
public class ProjectileHitEvent extends EntityEvent {
    private static final HandlerList handlers = new HandlerList();

    private final Entity target;

    public ProjectileHitEvent(final Projectile projectile, final Entity target) {
        super(projectile);
        this.target = target;
    }

    @Override
    public Projectile getEntity() {
        return (Projectile) entity;
    }

    public Entity getTarget() {
        return target;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

}
