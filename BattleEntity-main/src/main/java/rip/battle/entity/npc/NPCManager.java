package rip.battle.entity.npc;

import cc.stormworth.core.util.serialize.ItemStackAdapter;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;
import rip.battle.entity.EntityPlugin;
import rip.battle.entity.hologram.api.Hologram;
import rip.battle.entity.hologram.serialization.HologramTypeAdapter;
import rip.battle.entity.npc.api.NPC;
import rip.battle.entity.npc.serialization.NPCTypeAdapter;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Map;

@Getter
public class NPCManager {

    private final Map<String, NPC> npcs = Maps.newHashMap();

    private final EntityPlugin plugin;

    public NPCManager(EntityPlugin plugin) {
        this.plugin = plugin;
        loadNPCs();
    }

    public void loadNPCs() {
        File file = new File(plugin.getDataFolder(), "npcs");

        if (!file.exists()){
            return;
        }

        Arrays.stream(file.listFiles()).forEach(file1 -> {

            FileReader fileReader;

            try {
                fileReader = new FileReader(file1);

                JsonElement jsonElement = new Gson().fromJson(fileReader, JsonElement.class);
                NPC npc = NPCTypeAdapter.fromJson(jsonElement);

                npcs.put(file1.getName().replace(".json", ""), npc);

                System.out.println("Loaded NPC: " + npc.getName());

                fileReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    public void saveNPCs(){
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(ItemStack.class, new ItemStackAdapter())
                .registerTypeAdapter(Hologram.class, new HologramTypeAdapter())
                .enableComplexMapKeySerialization()
                .setPrettyPrinting()
                .create();

        File file = new File(plugin.getDataFolder(), "npcs");

        if (!file.exists())
            file.mkdirs();

        npcs.forEach((name, hologram) -> {
            try {
                FileWriter writer = new FileWriter(new File(plugin.getDataFolder(),
                        File.separator + "npcs" + File.separator + name + ".json"));

                writer.write(gson.toJson(NPCTypeAdapter.toJson(hologram)));
                System.out.println("Saved NPC: " + name);

                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    public void addNPC(NPC npc) {
        npcs.put(npc.getName(), npc);
    }

    public void removeNPC(NPC npc) {
        npc.destroy();
        npcs.remove(npc.getName());

        File file = new File(plugin.getDataFolder(), "npcs" + File.separator + npc.getName() + ".json");

        if (file.exists())
            file.delete();
    }

    public NPC getNPC(String name) {
        return npcs.get(name);
    }

    public void createNPC(String name, Location location) {
        NPC npc = new NPCEntity(name, location);

        npc.spawn();
        addNPC(npc);
    }
}
