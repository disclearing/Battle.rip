package cc.stormworth.meetup.util.adapters;

import cc.stormworth.meetup.util.adapters.nametags.NametagInfo;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public interface NametagAdapter {

    default List<NametagInfo> getNametags() {
        return new ArrayList<>();
    }

    void updateNametags(Player toRefersh, Player refreshFor);
}
