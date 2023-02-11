package rip.battle.entity;

import com.google.common.collect.Maps;
import lombok.Getter;

import java.util.Map;

public class EntityManager {

    @Getter private static final Map<Integer, Entity> entities = Maps.newHashMap();

    public static void addEntity(Entity entity) {
        entities.put(entity.getId(), entity);
    }

    public static void removeEntity(Entity entity) {
        entities.remove(entity.getId());
    }

    public static Entity getEntity(int id) {
        return entities.get(id);
    }

}
