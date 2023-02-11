package org.contrum.lobby.adapters.board;

import cc.stormworth.core.group.Group;
import cc.stormworth.core.group.grants.Grant;
import cc.stormworth.core.profile.Profile;
import cc.stormworth.core.util.TimeUtil;
import cc.stormworth.core.util.scoreboard.ScoreboardAdapter;
import org.contrum.lobby.LobbyPlugin;
import lombok.AllArgsConstructor;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.List;

/**
 * @author UKry
 * Created: 15/11/2022
 * Project BattleHub
 **/

@AllArgsConstructor
public class LobbyScoreboard implements ScoreboardAdapter {

    private LobbyPlugin plugin;

    @Override
    public String getTitle(Player player) {
        return ChatColor.translateAlternateColorCodes('&', "&6&lBattle Network");
    }

    @Override
    public List<String> getLines(Player player) {
        List<String> lines = new ArrayList<>();
        lines.add("&7&m--------------------");
        lines.add("&fOnline:");
        lines.add("&e" + plugin.getServerManager().getTotalCount() + "&7/&e5,000");
        lines.add("");
        Profile profile = plugin.getBattleAPI().getProfileManager().getProfile(player);
        lines.add("Rank: " + profile.getHighestAssigned().getRankName());
        lines.add("Expires: &e" + getExpireTime(profile, profile.getHighestAssigned()));
        lines.add("Gold: &e" + profile.getCurrencyData().getGold());
        lines.add(" ");
        lines.add("&7 " + DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG).format(ZonedDateTime.of(LocalDate.now(), LocalTime.now(), ZoneId.systemDefault())));
        lines.add("&6&7&m--------------------");
        lines.add("&6battle.rip");
        return lines;
    }

    private String getExpireTime(Profile profile, Group rank) {
        for (Grant grant : profile.getGrantsData().getUserGrants()) {
            if (grant.getRemover() == null)
                if (grant.getGroup().equals(rank))
                    return grant.getEndTime() == -1 ? "Never" : TimeUtil.getDate(grant.getEndTime());
        }
        return "Never";
    }
}