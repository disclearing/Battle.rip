package rip.battle.entity.hologram.serialization;

import cc.stormworth.core.util.location.LocationUtils;
import cc.stormworth.core.util.serialize.ItemStackAdapter;
import com.google.gson.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import rip.battle.entity.hologram.CraftHologram;
import rip.battle.entity.hologram.api.Hologram;
import rip.battle.entity.hologram.impl.UpdatableHologram;
import rip.battle.entity.hologram.lines.HologramLine;
import rip.battle.entity.hologram.lines.impl.ItemLine;
import rip.battle.entity.hologram.lines.impl.TextLine;

import java.lang.reflect.Type;

public class HologramTypeAdapter implements JsonDeserializer<Hologram>, JsonSerializer<Hologram> {

    @Override
    public Hologram deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        return fromJson(jsonElement);
    }

    @Override
    public JsonElement serialize(Hologram hologram, Type type, JsonSerializationContext jsonSerializationContext) {
        return toJson(hologram);
    }

    public static JsonObject toJson(Hologram hologram) {
        if (hologram == null) {
            return null;
        }

        JsonObject object = new JsonObject();

        CraftHologram craftHologram = (CraftHologram) hologram;

        object.addProperty("location", LocationUtils.serializeLocation(craftHologram.getLocation()));

        object.addProperty("name", craftHologram.getName());

        JsonArray lines = new JsonArray();

        for (HologramLine<?> line : craftHologram.getLines()) {
            JsonObject lineObject = new JsonObject();

            if (line instanceof TextLine) {
                lineObject.addProperty("text", ((TextLine) line).getText().apply(Bukkit.getConsoleSender()));
            }else {
                ItemLine itemLine = (ItemLine) line;
                lineObject.add("item", ItemStackAdapter.serialize(itemLine.getItemStack()));
            }

            lines.add(lineObject);
        }

        object.add("lines", lines);

        object.add("hidden", new JsonPrimitive(craftHologram.isHidden()));

        return object;
    }

    public static Hologram fromJson(JsonElement jsonElement) {
        if (jsonElement == null || !jsonElement.isJsonObject()) {
            return null;
        }

        JsonObject jsonObject = jsonElement.getAsJsonObject();

        Location location = LocationUtils.deserializeLocation(jsonObject.get("location").getAsString());

        JsonArray lines = jsonObject.getAsJsonArray("lines");

        String name = jsonObject.get("name").getAsString();
        CraftHologram hologram = new UpdatableHologram(name, location);

        for (JsonElement line : lines) {
            JsonObject lineObject = line.getAsJsonObject();

            if (lineObject.has("text")) {
                hologram.addLine(lineObject.get("text").getAsString());
            }else {
                hologram.addLine(ItemStackAdapter.deserialize(lineObject.get("item")));
            }
        }

        hologram.updateVisibility(jsonObject.get("hidden").getAsBoolean());

        return hologram;
    }
}
