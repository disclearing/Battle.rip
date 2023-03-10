package cc.stormworth.meetup.profile.struct;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.Location;

@AllArgsConstructor
@Getter
public class PearlLocation {

    private long endAt;
    private Location location;

    public boolean isExpired() {
        return System.currentTimeMillis() > endAt;
    }

}