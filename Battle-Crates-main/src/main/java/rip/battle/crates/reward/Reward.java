package rip.battle.crates.reward;

import cc.stormworth.core.CorePlugin;
import cc.stormworth.core.kt.util.ItemBuilder;
import cc.stormworth.core.util.chat.CC;
import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.Setter;
import org.bson.Document;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import rip.battle.crates.utils.ItemUtils;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Getter
@Setter
public class Reward {

    private ItemStack item;
    private double chance;
    private int slot;
    private RewardType type;
    private List<String> commands;
    private boolean obligatory;
    private LinkedList<String> broadcast = Lists.newLinkedList();

    public Reward(ItemStack item, double chance, int slot, RewardType type, List<String> commands, boolean obligatory) {
        this.item = item;

        if (this.item == null) {
            this.item = new ItemStack(Material.AIR);
        }

        this.chance = chance;
        this.slot = slot;
        this.type = type;
        this.commands = commands;
        this.obligatory = obligatory;

        if (this.chance > 1.0) {
            this.chance = 1.0;
        }

        ItemUtils.removeRewardsLore(this.item);
    }

    public Reward(Document document) {
        this.item = CorePlugin.GSON.fromJson(document.getString("item"), ItemStack.class);
        this.slot = document.getInteger("slot");

        if (document.get("chance") instanceof Double) {
            this.chance = document.getDouble("chance");
        } else if (document.get("chance") instanceof Integer) {
            this.chance = Double.parseDouble(document.getInteger("chance").toString());
        }

        if (this.chance > 1.0) {
            this.chance = 1.0;
        }

        if (document.containsKey("type")) {
            this.type = RewardType.getByName(document.getString("type"));
        } else {
            this.type = RewardType.ITEMS;
        }

        if (document.containsKey("commands")) {
            this.commands = document.getList("commands", String.class);
        }

        if (document.containsKey("broadcast")) {
            setBroadcast(new LinkedList<>(document.getList("broadcast", String.class)));
        }
    }

    public ItemStack getItem() {

        if (this.item == null) {
            return new ItemStack(Material.AIR);
        }

        ItemStack item = this.item.clone();

        ItemUtils.removeRewardsLore(item);
        return item;
    }

    public ItemStack getPreviewItem() {
        List<String> lore = Lists.newArrayList();

        lore.add("&0");
        lore.add("&7Type: &e" + this.type.name());
        lore.add("&0");
        lore.add("&7Chance: &e" + this.chance + "%");
        if (this.commands != null && !this.commands.isEmpty()) {
            lore.add("&7Commands: &e");
            for (String command : this.commands) {
                lore.add(" &7- &f" + command);
            }
        }
        lore.add("&0");
        lore.add("&fRight Click &7to edit");

        return new ItemBuilder(this.item.clone())
                .addLoreList(CC.translate(lore)).build();
    }

    public ItemStack getPreviewObligatoryItem() {
        List<String> lore = Lists.newArrayList();

        lore.add("&0");
        lore.add("&7Type: &e" + this.type.name());
        lore.add("&0");
        lore.add("&7Chance: &e" + this.chance + "%");
        lore.add("&cObligatory");
        if (this.commands != null && !this.commands.isEmpty()) {
            lore.add("&7Commands: &e");
            for (String command : this.commands) {
                lore.add(" &7- &f" + command);
            }
        }
        lore.add("&0");
        lore.add("&fRight Click &7to edit");

        return new ItemBuilder(this.item.clone())
                .addLoreList(CC.translate(lore)).build();
    }

    public ItemStack getRealItem() {
        return item;
    }

    public Document serialize() {
        Document document = new Document();
        document.append("item", CorePlugin.GSON.toJson(item));
        document.append("slot", slot);
        document.append("chance", chance);
        document.append("type", type.name());

        if (commands != null) {
            document.append("commands", commands);
        }

        document.append("broadcast", new ArrayList<>(broadcast));

        return document;
    }

}