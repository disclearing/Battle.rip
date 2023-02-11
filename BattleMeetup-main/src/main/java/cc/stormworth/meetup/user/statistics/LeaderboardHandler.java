package cc.stormworth.meetup.user.statistics;

import cc.stormworth.core.util.chat.CC;
import cc.stormworth.core.util.holograms.Hologram;
import cc.stormworth.core.util.holograms.Holograms;
import cc.stormworth.meetup.Meetup;
import cc.stormworth.meetup.util.LocationUtil;
import lombok.Data;
import org.apache.commons.lang.WordUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

@Data
public class LeaderboardHandler {

    private Hologram hologram;
    private HologramType hologramType;

    private long currentTime;

    public LeaderboardHandler() {
        this.hologramType = null;

        Bukkit.getScheduler().runTaskTimerAsynchronously(Meetup.getInstance(), () -> {
            if (this.currentTime > System.currentTimeMillis()) {
                this.hologram.setLines(this.getLeaderBoardHologramLines(false));
                return;
            }

            this.hologram.setLines(this.getLeaderBoardHologramLines(true));
            this.hologram.send();

            this.currentTime = (System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(16L));
        }, 20L, 20L);

        Location location = LocationUtil.deserializeLocation(Meetup.getInstance().getConfig().getString("Locations.Top-Location"));

        this.hologram = Holograms.newHologram().addLines(this.getLeaderBoardHologramLines(true)).at(location).build();
        this.hologram.send();

        this.currentTime = (System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(15L));
    }

    public void moveHologram(Location location) {
        this.hologram.destroy();

        this.hologram = Holograms.newHologram().addLines(this.getLeaderBoardHologramLines(true)).at(location).build();
        this.hologram.send();

        this.currentTime = (System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(15L));
    }

    private List<String> getLeaderBoardHologramLines(boolean switches) {
        List<String> lines = new ArrayList<>();

        AtomicInteger id = new AtomicInteger(1);

        if (switches) {
            if (this.hologramType == null) {
                this.hologramType = HologramType.ELO;
            } else if (this.hologramType == HologramType.ELO) {
                this.hologramType = HologramType.WINS;
            } else if (this.hologramType == HologramType.WINS) {
                this.hologramType = HologramType.KILLS;
            } else {
                this.hologramType = HologramType.ELO;
            }
        }

        LeaderboardType leaderboardType = (this.hologramType == HologramType.ELO ? LeaderboardType.WINS : this.hologramType == HologramType.WINS ? LeaderboardType.KILLS : LeaderboardType.ELO);

        lines.add(CC.translate("&6&lTOP " + WordUtils.capitalize(hologramType.name().toLowerCase())));
        lines.add("&7");

        Meetup.getInstance().getLeaderboards().get(leaderboardType).getUsers().stream().limit(10).forEach(user -> {
            lines.add(CC.GRAY + id.getAndIncrement() + ". &e" + user.getName() + " &7- &6" + (leaderboardType == LeaderboardType.KILLS ? user.getStatistics().getKills() : leaderboardType == LeaderboardType.WINS ? user.getStatistics().getWins() : user.getStatistics().getElo()));
        });

        lines.add("&7");
        lines.add("&7• Cycling in &f" + TimeUnit.MILLISECONDS.toSeconds(this.currentTime - System.currentTimeMillis()) + "s &7•");

        return lines;
    }

    private enum HologramType {

        KILLS,
        WINS,
        ELO
    }
}
