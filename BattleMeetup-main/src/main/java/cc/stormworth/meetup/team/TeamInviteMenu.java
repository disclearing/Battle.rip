package cc.stormworth.meetup.team;

import cc.stormworth.core.util.chat.CC;
import cc.stormworth.meetup.managers.TeamManager;
import cc.stormworth.meetup.managers.UserManager;
import cc.stormworth.meetup.user.UserData;
import cc.stormworth.meetup.util.ItemBuilder;
import cc.stormworth.meetup.util.menu.Menu;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class TeamInviteMenu extends Menu {

    public TeamInviteMenu(Player p) {
        super(p, "Invite Player", 4, true);
    }

    @Override
    public void updateInventory(Player p) {
        this.getInventory().clear();
        this.surround(new ItemBuilder(Material.STAINED_GLASS_PANE)
                .setName(" ")
                .setDurability(7)
                .build());

        Bukkit.getOnlinePlayers().stream()
                .filter(o -> !UserManager.getInstance().getUser(o.getUniqueId()).hasFullTeam())
                .filter(o -> !o.getUniqueId().equals(p.getUniqueId()))
                .forEach(o -> {
                    UserData targetUser = UserManager.getInstance().getUser(o.getUniqueId());

                    boolean invited = false;
                    if (TeamManager.getInstance().getTeamInvitations().get(o.getUniqueId()) != null)
                        if (TeamManager.getInstance().getTeamInvitations().get(o.getUniqueId()).getTeam().getNumber() == UserManager.getInstance().getUser(p.getUniqueId()).getTeamNumber())
                            invited = true;

                    this.add(new ItemBuilder(Material.SKULL_ITEM)
                            .setName(CC.PRIMARY + o.getName())
                            .setLore(CC.DARK_GRAY + "Player",
                                    "",
                                    CC.GRAY + "Statistics:",
                                    CC.GRAY + " ● " + CC.WHITE + "Ranking: " + CC.GRAY + (targetUser.findLeaderboardUser() == null ? "Not placed" : targetUser.findLeaderboardUser().getStatistics().getRanking()),
                                    CC.GRAY + " ● " + CC.WHITE + "Wins: " + CC.GRAY + targetUser.getStatistics().getWins(),
                                    CC.GRAY + " ● " + CC.WHITE + "K/D Ratio: " + CC.GRAY + targetUser.getStatistics().getKillDeathRatio(),
                                    "",
                                    (invited ? CC.GREEN + "You have invited this player!" : CC.SECONDARY + "Click to invite!"))
                            .setDurability(3)
                            .build());
                });

        this.fill(new ItemBuilder(Material.STAINED_GLASS_PANE)
                .setName(" ")
                .setDurability(7)
                .build());
    }

    @Override
    public void onClickItem(Player p, ItemStack item, boolean b) {

        if (item.getType() == Material.SKULL_ITEM) {
            Player target = Bukkit.getPlayer(ChatColor.stripColor(item.getItemMeta().getDisplayName()));

            if (target != null) {
                p.performCommand("team invite " + target.getName());
            }
        }
    }
}
