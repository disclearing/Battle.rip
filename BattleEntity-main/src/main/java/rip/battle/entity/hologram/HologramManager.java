package rip.battle.entity.hologram;

import cc.stormworth.core.util.serialize.ItemStackAdapter;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import rip.battle.entity.EntityPlugin;
import rip.battle.entity.hologram.api.Hologram;
import rip.battle.entity.hologram.impl.UpdatableHologram;
import rip.battle.entity.hologram.serialization.HologramTypeAdapter;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Map;

public class HologramManager {

    @Getter private final Map<String, Hologram> holograms = Maps.newHashMap();

    private final EntityPlugin plugin;

    public HologramManager(EntityPlugin plugin){
        this.plugin = plugin;

        loadHolograms();
    }

    public void loadHolograms(){
        File file = new File(plugin.getDataFolder(), "holograms");

        if (!file.exists()){
            return;
        }

        Arrays.stream(file.listFiles()).forEach(file1 -> {

            FileReader fileReader;

            try {
                fileReader = new FileReader(file1);

                JsonElement jsonElement = new Gson().fromJson(fileReader, JsonElement.class);

                System.out.println(jsonElement.toString());
                Hologram hologram = HologramTypeAdapter.fromJson(jsonElement);

                holograms.put(file1.getName().replace(".json", ""), hologram);

                System.out.println("Loaded hologram " + file1.getName());
                System.out.println("hologram " + ((CraftHologram)hologram).getLocation());

            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    public void saveHolograms(){

        Gson gson = new GsonBuilder()
                .registerTypeAdapter(ItemStack.class, new ItemStackAdapter())
                .registerTypeAdapter(Hologram.class, new HologramTypeAdapter())
                .enableComplexMapKeySerialization()
                .setPrettyPrinting()
                .create();

        File file = new File(plugin.getDataFolder(), "holograms");

        if (!file.exists())
            file.mkdirs();

        holograms.forEach((name, hologram) -> {
            try {
                FileWriter writer = new FileWriter(new File(plugin.getDataFolder(),
                        File.separator + "holograms" + File.separator + name + ".json"));

                writer.write(gson.toJson(HologramTypeAdapter.toJson(hologram)));
                System.out.println("Saved hologram " + name);

                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    public void addHologram(String name, Hologram hologram) {
        this.holograms.put(name, hologram);
    }

    public void removeHologram(String name) {
        Hologram hologram = this.holograms.remove(name);

        if (hologram != null) {
            hologram.destroy();
        }

        File file = new File(plugin.getDataFolder(), "holograms" + File.separator + name + ".json");

        if (file.exists())
            file.delete();
    }

    public Hologram getHologram(String name) {
        return this.holograms.get(name);
    }

    public void createHologram(String name, Location location) {
        Hologram hologram = new UpdatableHologram(name, location);

        hologram.spawn();
        addHologram(name, hologram);
    }
}
