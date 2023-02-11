package rip.battle.entity.hologram.commands.sub;

import cc.stormworth.core.util.chat.CC;
import com.jonahseguin.drink.annotation.Command;
import com.jonahseguin.drink.annotation.Sender;
import lombok.RequiredArgsConstructor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import org.bukkit.entity.Player;
import rip.battle.entity.hologram.HologramManager;
import rip.battle.entity.hologram.api.Hologram;

import java.util.Map;

@RequiredArgsConstructor
public class HologramListSubCommand {

    private final HologramManager hologramManager;

    @Command(name = "list", desc = "Allows you to list all holograms", aliases = {"l", "ls"})
    public void list(@Sender Player player) {

        Map<String, Hologram> holograms = hologramManager.getHolograms();

        if (holograms.isEmpty()) {
            player.sendMessage(CC.translate("&cThere are no holograms."));
            return;
        }

        player.sendMessage(CC.translate("&aThere are &e" + holograms.size() + " &aholograms."));

        holograms.forEach((name, hologram) -> {

            ComponentBuilder builder = new ComponentBuilder(CC.translate("&e" + name + " &b[Click to teleport]"));
            builder.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(CC.translate("&7Click to teleport")).create()));
            builder.event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/hologram tp " + name));

            player.spigot().sendMessage(builder.create());
        });
    }

}
