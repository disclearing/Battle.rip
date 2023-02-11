package cc.stormworth.meetup.user.statistics;

import cc.stormworth.core.util.chat.CC;
import cc.stormworth.meetup.managers.UserManager;
import cc.stormworth.meetup.user.UserData;
import cc.stormworth.meetup.util.ItemBuilder;
import cc.stormworth.meetup.util.menu.Menu;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class StatisticsMenu extends Menu {

    private UUID target = null;

    public StatisticsMenu(Player p) {
        super(p, "Your Stats", 3, true);
    }

    public StatisticsMenu(Player p, UUID target) {
        super(p, Bukkit.getPlayer(target).getName() + "'s Stats", 3, true);
        this.target = target;
        this.updateInventory(p);
    }

    @Override
    public void updateInventory(Player p) {
        UserData user = UserManager.getInstance().getUser(this.target == null ? p.getUniqueId() : this.target);

        this.set(13, new ItemBuilder(Material.APPLE)
                .setName(CC.PRIMARY + "Meetup")
                .setLore(CC.DARK_GRAY + "Statistics",
                        "",
                        CC.GRAY + "Ranking: " + CC.WHITE + (user.findLeaderboardUser() == null ? "Not placed" : user.findLeaderboardUser().getStatistics().getRanking()),
                        CC.GRAY + "Elo: " + CC.WHITE + user.getStatistics().getElo(),
                        "",
                        CC.GRAY + "Matches: " + CC.WHITE + user.getStatistics().getGamesPlayed(),
                        CC.GRAY + "Wins: " + CC.WHITE + user.getStatistics().getWins(),
                        "",
                        CC.GRAY + "Kills: " + CC.WHITE + user.getStatistics().getKills(),
                        CC.GRAY + "Deaths: " + CC.WHITE + user.getStatistics().getDeaths(),
                        CC.GRAY + "K/D Ratio: " + CC.WHITE + user.getStatistics().getKillDeathRatio()
                )
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
