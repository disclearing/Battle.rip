package rip.battle.crates.crate;

import cc.stormworth.core.CorePlugin;
import cc.stormworth.core.kt.util.ItemBuilder;
import cc.stormworth.core.util.chat.CC;
import cc.stormworth.core.util.general.LocationUtil;
import cc.stormworth.core.util.holograms.Hologram;
import cc.stormworth.core.util.holograms.Holograms;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;
import lombok.Getter;
import lombok.Setter;
import org.bson.Document;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import rip.battle.crates.Crates;
import rip.battle.crates.airdrop.Airdrop;
import rip.battle.crates.misterybox.MysteryBox;
import rip.battle.crates.reward.Reward;
import rip.battle.crates.reward.RewardType;
import rip.battle.crates.supplydrop.SupplyDrop;
import rip.battle.crates.utils.ChatUtils;
import rip.battle.crates.utils.RandomUtils;

import java.util.*;
import java.util.stream.Collectors;

@Setter
@Getter
public class Crate {

    private static final MongoCollection<Document> collection = Crates.getInstance().getMongoDatabase().getCollection("crates");

    @Getter
    private static final Map<String, Crate> crates = Maps.newHashMap();

    private final String name;
    private boolean enable;

    private List<Reward> rewards = Lists.newArrayList();
    private List<CratePlaceholder> placeholders = Lists.newArrayList();

    private String displayName;

    private ItemStack key;

    private Sound openSound;

    private int minimumReward = 1;
    private int maximumReward = 2;

    private Location chestLocation;

    private final Map<Location, Hologram> holograms = Maps.newHashMap();

    private final Inventory inventory;

    public Crate(String name) {
        this.name = name;
        this.enable = true;

        this.displayName = ChatColor.GOLD + name;

        this.key = new ItemBuilder(Material.TRIPWIRE_HOOK).name(CC.translate(displayName + " Key")).build();
        this.inventory = Bukkit.createInventory(null, 54, CC.translate("&eEdit Rewards of " + name));

        if (!name.equalsIgnoreCase("Airdrop")) {
            crates.put(name, this);
        }
    }

    public void destroyHolograms() {
        holograms.values().forEach(Hologram::destroy);
        holograms.clear();
    }

    public List<String> getHologramsLines() {
        return Arrays.asList(
                displayName + " Crate",
                "&7",
                "&fLeft click&7 &7for preview rewards",
                "&fRight click&7 to open",
                "&7",
                "&7&ostore.battle.rip");
    }

    public void sendHolograms() {
        if (chestLocation == null) return;
        double y = 0.50;

        if (chestLocation.getBlock().getType() == Material.BEACON || chestLocation.getBlock().getType() == Material.ENDER_CHEST
                || chestLocation.getBlock().getType() == Material.ENDER_PORTAL || chestLocation.getBlock().getType() == Material.DROPPER) {
            y = 0.90;
        }

        Location finalLocation = chestLocation.clone().add(0.50, y, 0.50);
        Hologram hologram = Holograms.newHologram().at(finalLocation).addLines(getHologramsLines()).build();
        hologram.send();
        this.holograms.put(finalLocation, hologram);
    }

    public void createHologram(Location location) {
        Location finalLocation = location.clone().add(0.50, 0.50, 0.50);
        Hologram hologram = Holograms.newHologram().at(finalLocation).addLines(getHologramsLines()).build();
        hologram.send();
        this.holograms.put(finalLocation, hologram);
    }

    public static void load() {
        Crate crate;
        for (Document document : collection.find()) {

            if (document.containsKey("type")) {
                if (document.getString("type").equalsIgnoreCase("supplydrop")) {
                    crate = new SupplyDrop();
                } else if (document.getString("type").equalsIgnoreCase("mysterybox")) {
                    crate = new MysteryBox(document.getString("name"));
                } else if (document.getString("type").equalsIgnoreCase("airdrop")) {
                    crate = new Airdrop();
                } else {
                    crate = new Crate(document.getString("name"));
                }
            } else {
                crate = new Crate(document.getString("name"));
            }

            crate.setEnable(document.getBoolean("enable"));
            crate.setDisplayName(document.getString("displayName"));
            crate.setKey(CorePlugin.GSON.fromJson(document.getString("key"), ItemStack.class));
            if (document.containsKey("openSound")) {
                crate.setOpenSound(Sound.valueOf(document.getString("openSound")));
            }
            crate.setMinimumReward(document.getInteger("minimumReward"));
            crate.setMaximumReward(document.getInteger("maximumReward"));

            for (Document reward : document.getList("rewards", Document.class)) {
                crate.getRewards().add(new Reward(reward));
            }

            for (Document placeholder : document.getList("placeholders", Document.class)) {
                crate.getPlaceholders().add(new CratePlaceholder(placeholder));
            }

            if (crate instanceof MysteryBox) {
                for (Document reward : document.getList("obligatoryrewards", Document.class)) {
                    ((MysteryBox) crate).getObligatoryRewards().add(new Reward(reward));
                }
            }

            if (document.containsKey("chestLocations")) {
                for (String location : document.getList("chestLocations", String.class)) {
                    crate.setChestLocation(LocationUtil.convertLocation(location));
                }
            }

            if (document.containsKey("chestLocation")) {
                crate.setChestLocation(LocationUtil.convertLocation(document.getString("chestLocation")));
            }

            for (CratePlaceholder placeholder : crate.getPlaceholders()) {
                crate.getInventory().setItem(placeholder.getSlot(), placeholder.getItem());
            }

            for (Reward reward : crate.getRewards()) {
                crate.getInventory().setItem(reward.getSlot(), reward.getItem());
            }

            if (crate instanceof MysteryBox) {
                for (Reward reward : ((MysteryBox) crate).getObligatoryRewards()) {
                    crate.getInventory().setItem(reward.getSlot(), reward.getItem());
                }
            }

            crate.sendHolograms();
        }

        if (!crates.containsKey("Airdrop")) {
            crates.put("Airdrop", new Airdrop());
        }

        if (!crates.containsKey("SupplyDrop")) {
            crates.put("SupplyDrop", new SupplyDrop());
        }
    }

    public void save() {
        Document document = new Document();

        document.append("name", name);
        document.append("enable", enable);
        document.append("displayName", displayName);
        document.append("key", CorePlugin.GSON.toJson(key));

        if (openSound != null) document.append("openSound", openSound.name());

        document.append("minimumReward", minimumReward);
        document.append("maximumReward", maximumReward);
        document.append("rewards", rewards.stream().map(Reward::serialize).collect(Collectors.toList()));
        document.append("placeholders", placeholders.stream().map(CratePlaceholder::serialize).collect(Collectors.toList()));
        //document.append("chestLocations", chestLocations.stream().map(LocationUtil::parseLocation).collect(Collectors.toList()));

        if (chestLocation != null) {
            document.append("chestLocation", LocationUtil.parseLocation(chestLocation));
        }

        if (this instanceof MysteryBox) {
            MysteryBox mysteryBox = (MysteryBox) this;
            document.append("type", "mysteryBox");
            document.append("obligatoryrewards", mysteryBox.getObligatoryRewards().stream().map(Reward::serialize).collect(Collectors.toList()));
        } else if (this instanceof Airdrop) {
            document.append("type", "airdrop");
        } else if (this instanceof SupplyDrop) {
            document.append("type", "supplydrop");
        } else {
            document.append("type", "crate");
        }

        collection.replaceOne(Filters.eq("name", name), document, new ReplaceOptions().upsert(true));
    }

    public static Crate getByLocation(Location location) {
        for (Crate crate : crates.values()) {
            if (crate.getChestLocation() != null && crate.getChestLocation().equals(location)) {
                return crate;
            }
        }

        return null;
    }


    public static Crate getByName(String name) {
        return crates.get(name);
    }

    public ItemStack generateKey() {

        if (getName().equalsIgnoreCase("Giftbox")) {
            return new ItemBuilder(key.clone())
                    .name(CC.translate(displayName + " &7(Package) &a&l* CLICK *"))
                    .setLore(Lists.newArrayList(
                            "&7",
                            "&7You can preview this " + getDisplayName() + " Crate &7 rewards",
                            "&7By going to the overworld &aSpawn",
                            "&7",
                            ChatUtils.getFirstColor(getDisplayName()) + "Right Click &7to open the key",
                            "&7",
                            "&7Purchase additional keys at &f&nstore.battle.rip"
                    ))
                    .build();
        }

        return new ItemBuilder(key.clone())
                .name(CC.translate(displayName + " Key"))
                .setLore(Lists.newArrayList(
                        "&7",
                        "&7You can preview this " + getDisplayName() + " Crate &7 rewards",
                        "&7By going to the overworld &aSpawn",
                        "&7",
                        ChatUtils.getFirstColor(getDisplayName()) + "Right Click &7to open the key",
                        ChatUtils.getFirstColor(getDisplayName()) + "Left Click &7to preview rewards",
                        "&7",
                        "&7Purchase additional keys at &f&nstore.battle.rip"
                ))
                .build();
    }

    public static Crate getCrateByKey(ItemStack key) {

        if (key == null) return null;

        if (key.getItemMeta() == null) return null;

        ItemMeta meta = key.getItemMeta();
        if (meta.hasDisplayName() && meta.hasLore()) {
            String name = meta.getDisplayName();

            if (name.contains(CC.translate("&7(Package)"))) {
                return Crate.getByName("Giftbox");
            }

            if (name.contains("Key")) {
                name = name.replace(" Key", "");

                String finalName = name;

                return crates.values()
                        .stream()
                        .filter(crate -> crate.getDisplayName().equalsIgnoreCase(finalName))
                        .findFirst().orElse(null);
            }
        }

        return null;
    }

    public void openCrate(Player player) {

        if (!enable) {
            player.sendMessage(CC.translate("&cThis crate is currently disabled"));
            return;
        }

        if (getMaximumReward() == 0 || getRewards().isEmpty()) {
            player.sendMessage(CC.translate("&cCrate " + getName() + " is empty, please contact an admin."));
            return;
        }

        if (player.getInventory().firstEmpty() < 0) {
            player.sendMessage(CC.translate("&cInventory Full."));
            return;
        }

        int random = new Random().nextInt(getMaximumReward() - getMinimumReward() + 1) + getMinimumReward();

        List<Reward> randomRewards = new ArrayList<>();

        List<Reward> rewardList = new ArrayList<>(getRewards());
        Collections.shuffle(rewardList);

        for (int i = 0; i < random; i++) {
            randomRewards.add(RandomUtils.getRandomReward(rewardList));
        }

        for (Reward reward : randomRewards) {
            if (reward.getType() == RewardType.ITEMS) {
                if (player.getInventory().firstEmpty() < 0) {
                    player.sendMessage(CC.translate("&cInventory Full."));
                    player.getWorld().dropItem(player.getLocation(), reward.getItem());
                } else {
                    player.getInventory().addItem(reward.getItem());
                }
            } else {
                List<String> commands = reward.getCommands();

                for (String command : commands) {

                    if (command.contains("op")) {
                        continue;
                    }

                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command.replace("%player%", player.getName()));
                }
            }

            if (!reward.getBroadcast().isEmpty()) {
                reward.getBroadcast().forEach(line -> Bukkit.broadcastMessage(line.replace("{player}", player.getName())));
            }
        }

        if (randomRewards.size() == 0) { // if not found any reward
            Reward reward = getRewards().get(new Random().nextInt(getRewards().size()));

            if (reward.getType() == RewardType.ITEMS) {
                if (player.getInventory().firstEmpty() < 0) {
                    player.sendMessage(CC.translate("&cInventory Full."));
                    player.getWorld().dropItem(player.getLocation(), reward.getItem());
                } else {
                    player.getInventory().addItem(reward.getItem());
                }
            } else {
                List<String> commands = reward.getCommands();

                for (String command : commands) {
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command.replace("%player%", player.getName()));
                }
            }

            if (!reward.getBroadcast().isEmpty()) {
                reward.getBroadcast().forEach(line -> Bukkit.broadcastMessage(line.replace("{player}", player.getName())));
            }
        }

        if (openSound != null) {
            player.playSound(player.getLocation(), openSound, 1, 1);
        }

        player.sendMessage(CC.translate("&aYou have received " + randomRewards.size() + " reward(s)"));
        consumeKey(player);
        player.updateInventory();
    }

    public void openEditRewardsInventory(Player player) {
        player.openInventory(inventory);
    }

    public void consumeKey(Player player) {
        if (player.getItemInHand().getAmount() > 1) {
            player.getItemInHand().setAmount(player.getItemInHand().getAmount() - 1);
        } else {
            player.setItemInHand(new ItemStack(Material.AIR));
        }
    }

    public Reward getReward(int slot) {
        return rewards.stream().filter(reward -> reward.getSlot() == slot).findFirst().orElse(null);
    }

    public CratePlaceholder getPlaceholder(int slot) {
        return placeholders.stream().filter(reward -> reward.getSlot() == slot).findFirst().orElse(null);
    }

    public void delete() {
        crates.remove(name);
        destroyHolograms();

        collection.deleteOne(Filters.eq("name", name));
    }

    public void removeHologram() {
        holograms.forEach((loc, hologram) -> hologram.destroy());
    }
}