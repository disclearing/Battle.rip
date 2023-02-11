package cc.stormworth.meetup.database;

import cc.stormworth.meetup.Meetup;
import cc.stormworth.meetup.style.hcf.kit.HCFKitSortation;
import cc.stormworth.meetup.user.UserData;
import cc.stormworth.meetup.user.statistics.Statistics;
import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;
import lombok.Getter;
import org.bson.Document;

import java.util.Collections;
import java.util.UUID;

public class MeetupMongo {

    @Getter
    private static final MeetupMongo instance = new MeetupMongo();
    @Getter
    private static MongoCollection<Document> players;
    private static final ReplaceOptions REPLACE_OPTIONS = new ReplaceOptions().upsert(true);
    @Getter
    private MongoDatabase database;

    public void init() {
        if (!Meetup.getInstance().getConfig().contains("mongo.host")
                || !Meetup.getInstance().getConfig().contains("mongo.port")
                || !Meetup.getInstance().getConfig().contains("mongo.database")
                || !Meetup.getInstance().getConfig().contains("mongo.authentication.enabled")
                || !Meetup.getInstance().getConfig().contains("mongo.authentication.username")
                || !Meetup.getInstance().getConfig().contains("mongo.authentication.password")
                || !Meetup.getInstance().getConfig().contains("mongo.authentication.database")) {
            throw new RuntimeException("Missing configuration option");
        }

        MongoClient client;
        if (Meetup.getInstance().getConfig().getBoolean("mongo.authentication.enabled")) {
            final MongoCredential credential = MongoCredential.createCredential(
                    Meetup.getInstance().getConfig().getString("mongo.authentication.username"),
                    Meetup.getInstance().getConfig().getString("mongo.authentication.database"),
                    Meetup.getInstance().getConfig().getString("mongo.authentication.password").toCharArray()
            );

            client = new MongoClient(new ServerAddress(Meetup.getInstance().getConfig().getString("mongo.host"),
                    Meetup.getInstance().getConfig().getInt("mongo.port")), Collections.singletonList(credential));
        } else {
            client = new MongoClient(new ServerAddress(Meetup.getInstance().getConfig().getString("mongo.host"),
                    Meetup.getInstance().getConfig().getInt("mongo.port")));
        }

        database = client.getDatabase(Meetup.getInstance().getConfig().getString("mongo.database"));

        players = database.getCollection(Meetup.getInstance().getConfig().getString("mongo.userstorage", "players"));
    }

    public void storeStatistics(String name, UUID uuidParsed, Statistics statistics, Document serialized) {
        players.replaceOne(Filters.eq("uuid", uuidParsed.toString()),
                new Document(serialized)
                        .append("uuid", uuidParsed.toString())
                        .append("name", name)
                        .append("gamesPlayed", statistics.getGamesPlayed())
                        .append("elo", statistics.getElo())
                        .append("wins", statistics.getWins())
                        .append("kills", statistics.getKills())
                        .append("deaths", statistics.getDeaths())
                        .append("rerolls", statistics.getRerolls())
                        .append("inventory", statistics.getInventory()), REPLACE_OPTIONS
        );
    }

    public void resetStats(UUID uuidParsed) {
        players.replaceOne(Filters.eq("uuid", uuidParsed.toString()),
                new Document("uuid", uuidParsed.toString())
                        .append("name", "")
                        .append("gamesPlayed", 0)
                        .append("elo", 0)
                        .append("wins", 0)
                        .append("kills", 0)
                        .append("deaths", 0)
                        .append("rerolls", 0)
                        .append("inventory", new Document())
        );
    }

    public UserData fetchUserStorage(UUID uuidParsed) {
        Document document = players.find(Filters.eq("uuid", uuidParsed.toString())).first();

        if (document == null) {
            return new UserData(
                    uuidParsed,
                    new Statistics(
                            -1,
                            0,
                            1400,
                            0,
                            0,
                            0,
                            0,
                            ""
                    ),
                    new HCFKitSortation().unserialize(null)
            );
        }

        return new UserData(
                uuidParsed,
                new Statistics(
                        document.getInteger("ranking", -1),
                        document.getInteger("gamesPlayed", 0),
                        document.getInteger("elo", 1400),
                        document.getInteger("wins", 0),
                        document.getInteger("kills", 0),
                        document.getInteger("deaths", 0),
                        document.getInteger("rerolls", 0),
                        document.containsKey("inventory") && document.getString("inventory") != null ? document.getString("inventory") : ""
                ),
                new HCFKitSortation().unserialize(document)
        );
    }
}