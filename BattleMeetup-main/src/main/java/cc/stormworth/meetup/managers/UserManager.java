package cc.stormworth.meetup.managers;

import cc.stormworth.meetup.database.MeetupMongo;
import cc.stormworth.meetup.states.PlayerState;
import cc.stormworth.meetup.user.UserData;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class UserManager {

    @Getter
    private static final UserManager instance = new UserManager();

    private final Map<UUID, UserData> users = new ConcurrentHashMap<>();

    public Collection<UserData> getUsers() {
        return users.values();
    }

    public Collection<UserData> getOnlineUsers() {
        return Bukkit.getOnlinePlayers().stream().map(online -> UserManager.getInstance().getUser(online.getUniqueId())).collect(Collectors.toList());
    }

    public Collection<UserData> getAliveUsers() {
        return Bukkit.getOnlinePlayers().stream().map(online -> UserManager.getInstance().getUser(online.getUniqueId())).filter(UserData::isAlive).collect(Collectors.toList());
    }

    public void loadUser(UUID uuidParsed) {
        if (this.users.containsKey(uuidParsed)) {
            this.users.get(uuidParsed);
            return;
        }

        this.users.put(uuidParsed, MeetupMongo.getInstance().fetchUserStorage(uuidParsed));
    }

    public UserData getUser(UUID uuid) {
        return users.get(uuid);
    }

    public boolean isSpectator(Player player) {
        UserData userData = this.users.get(player.getUniqueId());

        return userData == null || userData.getPlayerState() == PlayerState.SPECTATOR;
    }
}
