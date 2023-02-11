package dev.nulledcode.spigot.events;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.block.BlockEvent;
import org.bukkit.potion.PotionEffect;

public class BeaconEffectEvent extends BlockEvent implements Cancellable {
    private static final HandlerList handlers;

    static {
        handlers = new HandlerList();
    }

    private boolean cancelled;
    private PotionEffect effect;
    private final Player player;
    private final boolean primary;

    public BeaconEffectEvent(final Block block, final PotionEffect effect, final Player player, final boolean primary) {
        super(block);
        this.effect = effect;
        this.player = player;
        this.primary = primary;
    }

    public static HandlerList getHandlerList() {
        return BeaconEffectEvent.handlers;
    }

    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

    @Override
    public void setCancelled(final boolean cancelled) {
        this.cancelled = cancelled;
    }

    public PotionEffect getEffect() {
        return this.effect;
    }

    public void setEffect(final PotionEffect effect) {
        this.effect = effect;
    }

    public Player getPlayer() {
        return this.player;
    }

    public boolean isPrimary() {
        return this.primary;
    }

    @Override
    public HandlerList getHandlers() {
        return BeaconEffectEvent.handlers;
    }
}