package rip.battle.entity.npc.serialization;

import cc.stormworth.core.util.location.LocationUtils;
import cc.stormworth.core.util.serialize.ItemStackAdapter;
import cc.stormworth.core.util.skin.SkinTexture;
import com.google.gson.*;
import org.bukkit.Location;
import rip.battle.entity.hologram.api.Hologram;
import rip.battle.entity.hologram.serialization.HologramTypeAdapter;
import rip.battle.entity.npc.api.NPC;
import rip.battle.entity.npc.NPCEntity;

import java.lang.reflect.Type;

public class NPCTypeAdapter implements JsonDeserializer<NPC>, JsonSerializer<NPC> {
    @Override
    public NPC deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        return null;
    }

    @Override
    public JsonElement serialize(NPC npc, Type type, JsonSerializationContext jsonSerializationContext) {
        return toJson(npc);
    }

    public static JsonObject toJson(NPC npc) {
        if (npc == null) {
            return null;
        }

        JsonObject object = new JsonObject();

        object.addProperty("name", npc.getName());
        object.addProperty("displayName", npc.getDisplayName());

        if (npc.getSkin() != null) {
            object.add("skin", npc.getSkin().toJSON());
        }

        object.addProperty("location", LocationUtils.serializeLocation(npc.getLocation()));

        Hologram hologram = npc.getHologram();

        if (hologram != null) {
            object.add("hologram", HologramTypeAdapter.toJson(hologram));
        }

        object.add("hidden", new JsonPrimitive(!npc.isVisible()));

        if(npc.getHelmet() != null) object.add("helmet", ItemStackAdapter.serialize(npc.getHelmet()));
        if(npc.getChestplate() != null) object.add("chestplate", ItemStackAdapter.serialize(npc.getChestplate()));
        if(npc.getLeggings() != null) object.add("leggings", ItemStackAdapter.serialize(npc.getLeggings()));
        if(npc.getBoots() != null) object.add("boots", ItemStackAdapter.serialize(npc.getBoots()));
        if(npc.getItemInHand() != null) object.add("itemInHand", ItemStackAdapter.serialize(npc.getItemInHand()));

        return object;
    }

    public static NPC fromJson(JsonElement jsonElement) {
        if (jsonElement == null) {
            return null;
        }

        JsonObject object = jsonElement.getAsJsonObject();

        String name = object.get("name").getAsString();
        String displayName = object.get("displayName").getAsString();
        Location location = LocationUtils.deserializeLocation(object.get("location").getAsString());

        NPC npc = new NPCEntity(name, displayName, location);

        if (object.has("skin")){
            npc.setSkin(new SkinTexture(object.get("skin").getAsJsonObject()));
        }

        if (object.has("hologram")) {
            npc.setHologram(HologramTypeAdapter.fromJson(object.get("hologram")));
        }

        if (object.has("helmet")) {
            npc.setHelmet(ItemStackAdapter.deserialize(object.get("helmet").getAsJsonObject()));
        }

        if (object.has("chestplate")) {
            npc.setChestplate(ItemStackAdapter.deserialize(object.get("chestplate").getAsJsonObject()));
        }

        if (object.has("leggings")) {
            npc.setLeggings(ItemStackAdapter.deserialize(object.get("leggings").getAsJsonObject()));
        }

        if (object.has("boots")) {
            npc.setBoots(ItemStackAdapter.deserialize(object.get("boots").getAsJsonObject()));
        }

        if (object.has("itemInHand")) {
            npc.setItemInHand(ItemStackAdapter.deserialize(object.get("itemInHand").getAsJsonObject()));
        }

        npc.updateVisibility(object.get("hidden").getAsBoolean());

        return npc;
    }
}
