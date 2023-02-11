package cc.stormworth.meetup.profile;

import cc.stormworth.meetup.profile.struct.Hit;
import cc.stormworth.meetup.profile.struct.PearlLocation;
import cc.stormworth.meetup.profile.struct.Teleport;
import cc.stormworth.meetup.utils.countdown.Countdown;
import com.google.common.collect.Maps;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
@Setter
@Getter
public class Profile {

    @Getter
    private static final Map<UUID, Profile> profiles = Maps.newHashMap();

    private final UUID uuid;
    private Countdown countdown;
    private Teleport teleport;
    private Hit hit;
    private ItemStack helmet;

    private Player lastDamager;
    private Player lastDamaged;

    private long lastDamagerTime;
    private long lastDamagedTime;

    private boolean teamChat;

    @Setter
    private ItemStack[] hotbar;

    private PearlLocation lastPearlLocation;

    public static Profile get(Player player) {
        return get(player.getUniqueId());
    }

    public static Profile get(UUID uuid) {
        return profiles.get(uuid);
    }

    public boolean isExpiredDamage() {
        return lastDamagerTime + TimeUnit.SECONDS.toMillis(10) < System.currentTimeMillis();
    }

    public boolean isExpiredDamager() {
        return lastDamagerTime + TimeUnit.SECONDS.toMillis(10) < System.currentTimeMillis();
    }
}