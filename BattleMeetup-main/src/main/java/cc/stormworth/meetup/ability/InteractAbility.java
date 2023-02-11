package cc.stormworth.meetup.ability;

import cc.stormworth.core.util.cooldown.CooldownAPI;
import cc.stormworth.core.util.time.TimeUtil;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public abstract class InteractAbility extends Ability {

    public InteractAbility(String name, String displayName, List<String> description,
                           ItemStack item, long cooldown) {
        super(name, displayName, description, item, cooldown);
    }

    public void onInteract(PlayerInteractEvent event) {
        CooldownAPI.setCooldown(event.getPlayer(), getName(), getCooldown());
        sendActivation(event.getPlayer());
        event.setCancelled(true);
        consume(event.getPlayer());
        CooldownAPI.setCooldown(event.getPlayer(), "Global", TimeUtil.parseTimeLong("10s"));
    }
}