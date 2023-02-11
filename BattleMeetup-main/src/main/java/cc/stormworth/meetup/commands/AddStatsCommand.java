package cc.stormworth.meetup.commands;

import cc.stormworth.core.profile.Profile;
import cc.stormworth.core.rank.Rank;
import cc.stormworth.core.util.chat.CC;
import cc.stormworth.core.util.general.NumberUtils;
import cc.stormworth.meetup.database.MeetupMongo;
import cc.stormworth.meetup.managers.UserManager;
import cc.stormworth.meetup.user.UserData;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class AddStatsCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {


        if (sender instanceof Player && !Profile.getByPlayer((Player) sender).getActiveRank().isAboveOrEqual(Rank.ADMINISTRATOR)) {
            sender.sendMessage(CC.RED + "You don't have permissions to perform this action.");
            return true;
        }

        if (args.length < 3) {
            sender.sendMessage(new String[]{
                    CC.RED + "/addstats <player> elo <amount>",
                    CC.RED + "/addstats <player> deaths <amount>",
                    CC.RED + "/addstats <player> wins <amount>",
                    CC.RED + "/addstats <player> kills <amount>"
            });

            return true;
        }

        String arg0 = args[0];
        Player player = Bukkit.getPlayer(arg0);

        if (player == null) {
            sender.sendMessage(CC.RED + "The player you are looking for is offline.");
            return true;
        }

        if (!NumberUtils.isInteger(args[2])) {
            sender.sendMessage(CC.RED + "The amount must be a number (can be negative)");
            return true;
        }

        int amount = Integer.parseInt(args[2]);

        UserData user = UserManager.getInstance().getUser(player.getUniqueId());
        switch (args[1].toLowerCase()) {
            case "elo":
                user.getStatistics().setElo(user.getStatistics().getElo() + amount);
                break;
            case "deaths":
                user.getStatistics().setDeaths(user.getStatistics().getDeaths() + amount);
                break;
            case "kills":
                user.getStatistics().setKills(user.getStatistics().getKills() + amount);
                break;
            case "wins":
                user.getStatistics().setWins(user.getStatistics().getWins() + amount);
                break;
        }

        MeetupMongo.getInstance().storeStatistics(user.getName(), user.getUniqueId(),
                user.getStatistics(),
                user.getHcfSortation().serialize());

        return true;
    }
}
