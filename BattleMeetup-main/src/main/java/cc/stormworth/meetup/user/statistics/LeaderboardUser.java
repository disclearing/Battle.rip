package cc.stormworth.meetup.user.statistics;

import lombok.Getter;

import java.util.UUID;

@Getter
public class LeaderboardUser {

    private final UUID uuid;
    private final String name;
    private final int ranking;
    private final Statistics statistics;

    public LeaderboardUser(UUID uuid, String name, int ranking, int gamesPlayed, int elo, int wins, int kills, int deaths) {
        this.uuid = uuid;
        this.name = name;
        this.ranking = ranking;

        this.statistics = new Statistics(ranking, gamesPlayed, elo, wins, kills, deaths, 0, "");
    }
}