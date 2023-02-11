package rip.battle.entity.living.events;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import rip.battle.entity.living.LivingEntity;

@RequiredArgsConstructor
@Getter
public class LivingEntityDeathEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    private final LivingEntity entity;
    private final org.bukkit.entity.Entity killer;

    public static HandlerList getHandlerList() {
        return handlers;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }
}
