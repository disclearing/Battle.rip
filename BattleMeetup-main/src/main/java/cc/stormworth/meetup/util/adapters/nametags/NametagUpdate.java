package cc.stormworth.meetup.util.adapters.nametags;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@Getter
@AllArgsConstructor
final class NametagUpdate {

    private UUID toRefresh, refreshFor;
}