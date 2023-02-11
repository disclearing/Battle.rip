package cc.stormworth.meetup.user.statistics;

import cc.stormworth.core.util.chat.CC;
import cc.stormworth.meetup.Meetup;
import cc.stormworth.meetup.util.ItemBuilder;
import cc.stormworth.meetup.util.menu.Menu;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class LeaderboardMenu extends Menu {

    public LeaderboardMenu(Player p) {
        super(p, "Leaderboard", 3, true);
    }

    @Override
    public void updateInventory(Player p) {
        this.getInventory().clear();
        this.surround(new ItemBuilder(Material.STAINED_GLASS_PANE)
                .setName(" ")
                .setDurability(7)
                .build());

        // Bad shit lol
        List<String> eloLeaderboard = new ArrayList<>();
        List<String> winsLeaderboard = new ArrayList<>();
        List<String> killsLeaderboard = new ArrayList<>();

        Stream.of(eloLeaderboard, winsLeaderboard, killsLeaderboard).forEach(leaderboard -> {
            leaderboard.add(CC.DARK_GRAY + "Leaderboard");
            leaderboard.add("");
        });

        Meetup.getInstance().getLeaderboards().get(LeaderboardType.ELO).getUsers().stream().limit(10).forEach(user -> {
            eloLeaderboard.add(CC.GRAY + "#" + user.getRanking() + ": " + CC.RESET + user.getName() + CC.GRAY + " - " + CC.SECONDARY +
                    user.getStatistics().getElo());
        });

        Meetup.getInstance().getLeaderboards().get(LeaderboardType.WINS).getUsers().stream().limit(10).forEach(user -> {
            winsLeaderboard.add(CC.GRAY + "#" + user.getRanking() + ": " + CC.RESET + user.getName() + CC.GRAY + " - " + CC.SECONDARY + user.getStatistics().getWins());
        });

        Meetup.getInstance().getLeaderboards().get(LeaderboardType.KILLS).getUsers().stream().limit(10).forEach(user -> {
            killsLeaderboard.add(CC.GRAY + "#" + user.getRanking() + ": " + CC.RESET + user.getName() + CC.GRAY + " - " + CC.SECONDARY + user.getStatistics().getKills());
        });

        this.set(11, new ItemBuilder(Material.NETHER_STAR)
                .setName(CC.PRIMARY + "Top ELO")
                .setLore(eloLeaderboard)
                .build());

        this.set(13, new ItemBuilder(Material.GOLDEN_APPLE)
                .setName(CC.PRIMARY + "Top Wins")
                .setLore(winsLeaderboard)
                .build());

        this.set(15, new ItemBuilder(Material.DIAMOND_SWORD)
                .setName(CC.PRIMARY + "Top Kills")
                .setLore(killsLeaderboard)
                .build());

        this.fill(new ItemBuilder(Material.STAINED_GLASS_PANE)
                .setName(" ")
                .setDurability(7)
                .build());
    }

    @Override
    public void onClickItem(Player p, ItemStack item, boolean b) {

    }
}
