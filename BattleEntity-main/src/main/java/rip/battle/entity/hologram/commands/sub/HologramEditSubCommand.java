package rip.battle.entity.hologram.commands.sub;

import com.jonahseguin.drink.annotation.Command;
import com.jonahseguin.drink.annotation.Sender;
import lombok.RequiredArgsConstructor;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import rip.battle.entity.EntityPlugin;
import rip.battle.entity.hologram.HologramManager;
import rip.battle.entity.hologram.api.Hologram;
import rip.battle.entity.hologram.menu.HologramEditMenu;

@RequiredArgsConstructor
public class HologramEditSubCommand {

    private final EntityPlugin plugin;
    private final HologramManager hologramManager;

    @Command(name = "edit", desc = "Edit a hologram")
    public void edit(@Sender Player player, String name){

        Hologram hologram = hologramManager.getHologram(name);

        if (hologram == null){
            player.sendMessage(ChatColor.RED + "Hologram not found.");
            return;
        }

        new HologramEditMenu(plugin, hologram, null).open(player);
    }

}
