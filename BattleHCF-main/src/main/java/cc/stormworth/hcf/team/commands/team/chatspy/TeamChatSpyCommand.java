package cc.stormworth.hcf.team.commands.team.chatspy;

import cc.stormworth.core.util.command.annotations.Command;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class TeamChatSpyCommand {
    @Command(names = {"team chatspy", "t chatspy", "f chatspy", "faction chatspy", "fac chatspy"}, permission = "MODPLUS")
    public static void teamChatSpy(final Player sender) {
        sender.sendMessage(ChatColor.RED + "/team chatspy list - views teams who you are spying on");
        sender.sendMessage(ChatColor.RED + "/team chatspy add - starts spying on a team");
        sender.sendMessage(ChatColor.RED + "/team chatspy del - stops spying on a team");
        sender.sendMessage(ChatColor.RED + "/team chatspy clear - stops spying on all teams");
    }
}
