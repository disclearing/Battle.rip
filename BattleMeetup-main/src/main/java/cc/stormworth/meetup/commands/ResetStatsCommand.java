package cc.stormworth.meetup.commands;

import cc.stormworth.core.util.command.annotations.Command;
import cc.stormworth.core.util.command.annotations.Param;
import org.bukkit.entity.Player;

import java.util.UUID;

public class ResetStatsCommand {

    @Command(names = "resetstats", permission = "ADMIN")
    public static void resetstats(Player player, @Param(name = "targer")UUID uuid){

    }

}
