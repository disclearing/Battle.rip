package dev.nulledcode.spigot.events;

import org.bukkit.entity.Entity;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityEvent;

public class AirdropDieEvent extends EntityEvent {

    private static final HandlerList handlers = new HandlerList();

    public AirdropDieEvent(Entity entity) {
        super(entity);
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }
}
