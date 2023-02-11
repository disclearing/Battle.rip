package cc.stormworth.meetup.user.statistics;

import cc.stormworth.meetup.database.MeetupMongo;
import cc.stormworth.meetup.util.TaskUtil;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Sorts;
import lombok.Getter;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

public class Leaderboard {

    @Getter
    private final List<LeaderboardUser> users = new ArrayList<>();

    @Getter
    private boolean loaded;

    private final LeaderboardType type;

    public Leaderboard(LeaderboardType type) {
        this.type = type;
    }

    public void load(boolean async) {
        if (async)
            TaskUtil.runAsync(() -> this.load(this.type));
        else
            this.load(this.type);
    }

    private void load(LeaderboardType type) {
        try (MongoCursor<Document> documents = MeetupMongo.getPlayers().find()
                .sort(Sorts.descending(type.toString().toLowerCase()))
                .iterator()) {
            int ranking = 1;

            while (documents.hasNext()) {
                Document result = documents.next();

                if (result.containsKey("name") &&
                        result.containsKey("elo") &&
                        result.containsKey("wins") &&
                        result.containsKey("kills") &&
                        result.containsKey("deaths") &&
                        result.containsKey("gamesPlayed")) {

                    if (users.size() >= 10) {
                        break;
                    }

                    LeaderboardUser leaderboardUser = new LeaderboardUser(UUID.fromString(result.getString("uuid")),
                            result.getString("name"),
                            ranking,
                            result.getInteger("gamesPlayed"),
                            result.getInteger("elo"),
                            result.getInteger("wins"),
                            result.getInteger("kills"),
                            result.getInteger("deaths"));

                    this.users.add(leaderboardUser);

                    ranking++;
                }
            }
        }
    }
}
