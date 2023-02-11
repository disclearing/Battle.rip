package rip.battle.entity.utils;

import com.viaversion.viaversion.api.Via;
import lombok.experimental.UtilityClass;
import org.bukkit.entity.Player;

@UtilityClass
public class PlayerUtil {

    public boolean isLegacy(Player player){
        return Via.getAPI().getPlayerVersion(player.getUniqueId()) < 47;
    }

}
