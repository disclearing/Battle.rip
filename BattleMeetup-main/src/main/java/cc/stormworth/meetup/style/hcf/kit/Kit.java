package cc.stormworth.meetup.style.hcf.kit;

import cc.stormworth.core.CorePlugin;
import cc.stormworth.meetup.database.MeetupMongo;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.bson.Document;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

@AllArgsConstructor
@Getter
@Setter
@RequiredArgsConstructor
public class Kit {

    @Getter
    private final static Map<Integer, Kit> kits = Maps.newHashMap();
    private static final MongoCollection<Document> collection = MeetupMongo.getInstance().getDatabase().getCollection("kits");

    private final int id;
    private ItemStack[] armor;
    private ItemStack[] contents;


    public static Kit findAny() {
        return Lists.newArrayList(kits.values()).get(ThreadLocalRandom.current().nextInt(kits.size()));
    }

    public static void loadKits() {
        for (Document document : collection.find()) {
            Kit kit = new Kit(document.getInteger("id"),
                    CorePlugin.PLAIN_GSON.fromJson(document.getString("armor"), ItemStack[].class),
                    CorePlugin.PLAIN_GSON.fromJson(document.getString("contents"), ItemStack[].class));

            kits.put(kit.getId(), kit);
        }
    }

    public static Kit getById(int id) {
        return kits.get(id);
    }

    public void saveKit() {
        Document document = new Document();

        document.put("id", id);
        document.put("armor", CorePlugin.PLAIN_GSON.toJson(armor));
        document.put("contents", CorePlugin.PLAIN_GSON.toJson(contents));

        collection.replaceOne(Filters.eq("id", id), document, new ReplaceOptions().upsert(true));
    }

    public void apply(Player player) {

        player.getInventory().setArmorContents(armor);
        player.getInventory().setContents(contents);

        // Add speed II to all the kits
        player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 9999 * 20, 1));

        player.updateInventory();
    }

    public void remove() {
        collection.deleteOne(Filters.eq("id", id));

        kits.remove(id);
    }
}
