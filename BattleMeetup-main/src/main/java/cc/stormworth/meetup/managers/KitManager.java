package cc.stormworth.meetup.managers;

import cc.stormworth.meetup.kits.KitInventorySortation;
import cc.stormworth.meetup.kits.KitItemContainer;
import cc.stormworth.meetup.scenarios.Scenario;
import cc.stormworth.meetup.user.UserData;
import cc.stormworth.meetup.util.ItemBuilder;
import cc.stormworth.meetup.util.ItemUtil;
import cc.stormworth.meetup.util.KitUtil;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class KitManager {

    private static KitManager instance;
    private final List<KitItemContainer> randomKits;
    private final List<KitItemContainer> randomOPKits;
    public KitManager() {
        this.randomKits = new ArrayList<>();

        this.randomKits.add(new KitItemContainer(KitUtil.PROT_1_IRON_HELMET,        KitUtil.PROT_2_DIAMOND_CHESTPLATE,      KitUtil.PROT_1_IRON_LEGGINGS, KitUtil.PROT_2_DIAMOND_BOOTS, KitUtil.SHARP_3_DIAMOND_SWORD, KitUtil.NO_BOW, 8, 2, Arrays.asList(KitUtil.FNS)));
        this.randomKits.add(new KitItemContainer(KitUtil.PROT_2_DIAMOND_HELMET,     KitUtil.PROT_2_DIAMOND_CHESTPLATE,      KitUtil.PROT_1_IRON_LEGGINGS, KitUtil.PROT_1_IRON_BOOTS, KitUtil.SHARP_3_IRON_SWORD, KitUtil.POWER_2_BOW, 9, 3, Arrays.asList(KitUtil.FIRERESISTANCE_POTION)));
        this.randomKits.add(new KitItemContainer(KitUtil.PROJ_1_IRON_HELMET,        KitUtil.PROT_2_DIAMOND_CHESTPLATE,      KitUtil.PROT_1_DIAMOND_LEGGINGS, KitUtil.PROT_1_IRON_BOOTS, KitUtil.SHARP_3_DIAMOND_SWORD, KitUtil.POWER_1_BOW, 7, 2, Collections.singletonList(KitUtil.FNS)));

        this.randomKits.add(new KitItemContainer(KitUtil.PROJ_1_IRON_HELMET,        KitUtil.PROT_2_DIAMOND_CHESTPLATE,      KitUtil.PROJ_1_IRON_LEGGINGS, KitUtil.PROT_1_DIAMOND_BOOTS, KitUtil.SHARP_3_DIAMOND_SWORD, KitUtil.POWER_1_BOW, 10, 2, new ArrayList<>()));
        this.randomKits.add(new KitItemContainer(KitUtil.PROT_1_DIAMOND_HELMET,     KitUtil.PROJ_1_DIAMOND_CHESTPLATE,      KitUtil.IRON_LEGGINGS, KitUtil.IRON_BOOTS, KitUtil.SHARP_3_DIAMOND_SWORD, KitUtil.POWER_2_BOW, 10, 3, Arrays.asList(KitUtil.FNS, KitUtil.COBWEB)));
        this.randomKits.add(new KitItemContainer(KitUtil.PROJ_1_IRON_HELMET,        KitUtil.PROT_1_DIAMOND_CHESTPLATE,      KitUtil.PROJ_1_DIAMOND_LEGGINGS, KitUtil.PROT_1_IRON_BOOTS, KitUtil.SHARP_2_DIAMOND_SWORD, KitUtil.POWER_1_PUNCH_1_BOW, 10, 1, Collections.singletonList(KitUtil.SPEED_1_POTION)));

        this.randomKits.add(new KitItemContainer(KitUtil.PROT_1_DIAMOND_HELMET,     KitUtil.IRON_CHESTPLATE,                KitUtil.PROT_1_DIAMOND_LEGGINGS, KitUtil.PROJ_1_DIAMOND_BOOTS, KitUtil.SHARP_3_IRON_SWORD, KitUtil.POWER_1_BOW, 11, 2, Collections.singletonList(KitUtil.COBWEB)));
        this.randomKits.add(new KitItemContainer(KitUtil.PROT_2_DIAMOND_HELMET,     KitUtil.PROT_1_IRON_CHESTPLATE,         KitUtil.PROT_2_DIAMOND_LEGGINGS, KitUtil.PROT_2_DIAMOND_BOOTS, KitUtil.SHARP_2_DIAMOND_SWORD, KitUtil.PUNCH_2_BOW, 8, 0, new ArrayList<>()));
        this.randomKits.add(new KitItemContainer(KitUtil.PROT_2_DIAMOND_HELMET,     KitUtil.PROT_1_IRON_CHESTPLATE,         KitUtil.PROJ_1_DIAMOND_LEGGINGS, KitUtil.PROT_2_DIAMOND_BOOTS, KitUtil.SHARP_2_FIRE_1_IRON_SWORD, KitUtil.POWER_2_BOW, 10, 2, Arrays.asList(KitUtil.FIRERESISTANCE_POTION)));
        this.randomKits.add(new KitItemContainer(KitUtil.PROJ_1_DIAMOND_HELMET,     KitUtil.PROJ_1_IRON_CHESTPLATE,         KitUtil.PROT_1_DIAMOND_LEGGINGS, KitUtil.PROJ_1_DIAMOND_BOOTS, KitUtil.SHARP_3_IRON_SWORD, KitUtil.NO_BOW, 8, 1, Arrays.asList(KitUtil.SPEED_1_POTION, KitUtil.FNS)));

        this.randomKits.add(new KitItemContainer(KitUtil.PROT_1_DIAMOND_HELMET,     KitUtil.PROT_2_DIAMOND_CHESTPLATE,      KitUtil.IRON_LEGGINGS, KitUtil.PROJ_1_DIAMOND_BOOTS, KitUtil.SHARP_2_DIAMOND_SWORD, KitUtil.NO_BOW, 7, 1, new ArrayList<>()));
        this.randomKits.add(new KitItemContainer(KitUtil.PROT_2_DIAMOND_HELMET,     KitUtil.PROT_1_DIAMOND_CHESTPLATE,      KitUtil.PROT_1_IRON_LEGGINGS, KitUtil.PROT_1_DIAMOND_BOOTS, KitUtil.SHARP_2_FIRE_1_IRON_SWORD, KitUtil.POWER_1_BOW, 6, 0, new ArrayList<>()));
        this.randomKits.add(new KitItemContainer(KitUtil.PROJ_1_DIAMOND_HELMET,     KitUtil.PROJ_1_DIAMOND_CHESTPLATE,      KitUtil.IRON_LEGGINGS, KitUtil.PROJ_1_DIAMOND_BOOTS, KitUtil.SHARP_3_DIAMOND_SWORD, KitUtil.NO_BOW, 8, 0, Collections.singletonList(KitUtil.FNS)));
        this.randomKits.add(new KitItemContainer(KitUtil.PROT_2_DIAMOND_HELMET,     KitUtil.PROT_2_DIAMOND_CHESTPLATE,      KitUtil.PROT_1_IRON_LEGGINGS, KitUtil.PROT_2_DIAMOND_BOOTS, KitUtil.SHARP_2_DIAMOND_SWORD, KitUtil.NO_BOW, 5, 0, new ArrayList<>()));

        this.randomKits.add(new KitItemContainer(KitUtil.IRON_HELMET,               KitUtil.PROJ_1_DIAMOND_CHESTPLATE,      KitUtil.PROT_1_DIAMOND_LEGGINGS, KitUtil.PROJ_1_DIAMOND_BOOTS, KitUtil.SHARP_2_DIAMOND_SWORD, KitUtil.POWER_1_BOW, 6, 1, new ArrayList<>()));
        this.randomKits.add(new KitItemContainer(KitUtil.IRON_HELMET,               KitUtil.PROT_2_DIAMOND_CHESTPLATE,      KitUtil.PROT_1_DIAMOND_LEGGINGS, KitUtil.PROT_1_DIAMOND_BOOTS, KitUtil.SHARP_3_IRON_SWORD, KitUtil.NO_BOW, 5, 0, Collections.singletonList(KitUtil.COBWEB)));
        this.randomKits.add(new KitItemContainer(KitUtil.PROJ_1_IRON_HELMET,        KitUtil.PROJ_1_DIAMOND_CHESTPLATE,      KitUtil.PROT_2_DIAMOND_LEGGINGS, KitUtil.PROJ_1_DIAMOND_BOOTS, KitUtil.SHARP_2_DIAMOND_SWORD, KitUtil.NO_BOW, 4, 2, Collections.singletonList(KitUtil.FNS)));
        this.randomKits.add(new KitItemContainer(KitUtil.PROT_1_IRON_HELMET,        KitUtil.PROT_1_DIAMOND_CHESTPLATE,      KitUtil.PROJ_1_DIAMOND_LEGGINGS, KitUtil.PROT_2_DIAMOND_BOOTS, KitUtil.SHARP_2_FIRE_1_IRON_SWORD, KitUtil.POWER_1_BOW, 6, 0, Arrays.asList(KitUtil.FIRERESISTANCE_POTION)));

        this.randomKits.add(new KitItemContainer(KitUtil.PROJ_1_DIAMOND_HELMET,     KitUtil.PROJ_1_DIAMOND_CHESTPLATE,      KitUtil.PROT_2_DIAMOND_LEGGINGS, KitUtil.IRON_BOOTS, KitUtil.SHARP_3_IRON_SWORD, KitUtil.NO_BOW, 8, 2, new ArrayList<>()));
        this.randomKits.add(new KitItemContainer(KitUtil.PROJ_1_DIAMOND_HELMET,     KitUtil.PROT_1_DIAMOND_CHESTPLATE,      KitUtil.PROT_1_DIAMOND_LEGGINGS, KitUtil.IRON_BOOTS, KitUtil.SHARP_2_DIAMOND_SWORD, KitUtil.POWER_1_BOW, 9, 0, new ArrayList<>()));
        this.randomKits.add(new KitItemContainer(KitUtil.PROT_1_DIAMOND_HELMET,     KitUtil.PROJ_1_DIAMOND_CHESTPLATE,      KitUtil.PROT_2_DIAMOND_LEGGINGS, KitUtil.PROJ_1_IRON_BOOTS, KitUtil.SHARP_2_DIAMOND_SWORD, KitUtil.POWER_1_BOW, 6, 0, Arrays.asList(KitUtil.FNS)));
        this.randomKits.add(new KitItemContainer(KitUtil.PROT_2_DIAMOND_HELMET,     KitUtil.PROT_1_DIAMOND_CHESTPLATE,      KitUtil.PROJ_1_DIAMOND_LEGGINGS, KitUtil.PROT_1_IRON_BOOTS, KitUtil.SHARP_2_DIAMOND_SWORD, KitUtil.POWER_1_PUNCH_1_BOW, 5, 1, new ArrayList<>()));

        this.randomKits.add(new KitItemContainer(KitUtil.IRON_HELMET,               KitUtil.PROT_1_DIAMOND_CHESTPLATE,      KitUtil.PROJ_1_DIAMOND_LEGGINGS, KitUtil.IRON_BOOTS, KitUtil.SHARP_2_DIAMOND_SWORD, KitUtil.POWER_1_FLAME_1_BOW, 9, 0, Collections.singletonList(KitUtil.COBWEB)));
        this.randomKits.add(new KitItemContainer(KitUtil.PROJ_1_DIAMOND_HELMET,     KitUtil.PROT_1_DIAMOND_CHESTPLATE,      KitUtil.IRON_LEGGINGS, KitUtil.IRON_BOOTS, KitUtil.SHARP_2_FIRE_1_IRON_SWORD, KitUtil.NO_BOW, 6, 0, Collections.singletonList(KitUtil.SPEED_1_POTION)));
        this.randomKits.add(new KitItemContainer(KitUtil.PROT_1_IRON_HELMET,        KitUtil.PROJ_1_DIAMOND_CHESTPLATE,      KitUtil.PROJ_1_IRON_LEGGINGS, KitUtil.PROT_2_DIAMOND_BOOTS, KitUtil.SHARP_3_IRON_SWORD, KitUtil.NO_BOW, 7, 1, Arrays.asList(KitUtil.SPEED_1_POTION, KitUtil.COBWEB)));
        this.randomKits.add(new KitItemContainer(KitUtil.PROJ_1_DIAMOND_HELMET,     KitUtil.IRON_CHESTPLATE,                KitUtil.PROT_1_IRON_LEGGINGS, KitUtil.PROT_1_DIAMOND_BOOTS, KitUtil.SHARP_3_DIAMOND_SWORD, KitUtil.POWER_2_BOW, 6, 2, Collections.singletonList(KitUtil.HORSE)));
        this.randomKits.add(new KitItemContainer(KitUtil.IRON_HELMET,               KitUtil.PROT_2_DIAMOND_CHESTPLATE,      KitUtil.PROT_1_DIAMOND_LEGGINGS, KitUtil.IRON_BOOTS, KitUtil.SHARP_3_DIAMOND_SWORD, KitUtil.POWER_1_FLAME_1_BOW, 6, 0, Arrays.asList(KitUtil.HORSE, KitUtil.FNS)));

        // OP Kits:

        this.randomOPKits = new ArrayList<>();

        this.randomOPKits.add(new KitItemContainer(KitUtil.PROT_3_DIAMOND_HELMET,   KitUtil.PROT_2_DIAMOND_CHESTPLATE,      KitUtil.PROT_1_DIAMOND_LEGGINGS, KitUtil.PROJ_1_DIAMOND_BOOTS, KitUtil.SHARP_4_DIAMOND_SWORD, KitUtil.POWER_2_BOW, 12, 1, Arrays.asList(KitUtil.FIRERESISTANCE_POTION)));
        this.randomOPKits.add(new KitItemContainer(KitUtil.PROT_1_DIAMOND_HELMET,   KitUtil.PROT_3_DIAMOND_CHESTPLATE,      KitUtil.PROJ_1_DIAMOND_LEGGINGS, KitUtil.PROT_2_DIAMOND_BOOTS, KitUtil.SHARP_4_DIAMOND_SWORD, KitUtil.POWER_2_BOW, 12, 1, new ArrayList<>()));
        this.randomOPKits.add(new KitItemContainer(KitUtil.PROJ_1_DIAMOND_HELMET,   KitUtil.PROT_2_DIAMOND_CHESTPLATE,      KitUtil.PROT_3_DIAMOND_LEGGINGS, KitUtil.PROT_1_DIAMOND_BOOTS, KitUtil.SHARP_4_DIAMOND_SWORD, KitUtil.PUNCH_2_BOW, 12, 1, new ArrayList<>()));
        this.randomOPKits.add(new KitItemContainer(KitUtil.PROT_2_DIAMOND_HELMET,   KitUtil.PROT_1_DIAMOND_CHESTPLATE,      KitUtil.PROJ_1_DIAMOND_LEGGINGS, KitUtil.PROT_3_DIAMOND_BOOTS, KitUtil.SHARP_4_DIAMOND_SWORD, KitUtil.POWER_2_BOW, 12, 1, Arrays.asList(KitUtil.FIRERESISTANCE_POTION)));

        this.randomOPKits.add(new KitItemContainer(KitUtil.PROT_2_DIAMOND_HELMET,   KitUtil.PROT_3_DIAMOND_CHESTPLATE,      KitUtil.PROJ_1_DIAMOND_LEGGINGS, KitUtil.PROT_2_DIAMOND_BOOTS, KitUtil.SHARP_3_DIAMOND_SWORD, KitUtil.POWER_3_BOW, 14, 2, Arrays.asList(KitUtil.FNS)));
        this.randomOPKits.add(new KitItemContainer(KitUtil.PROT_1_DIAMOND_HELMET,   KitUtil.PROT_3_DIAMOND_CHESTPLATE,      KitUtil.PROT_3_DIAMOND_LEGGINGS, KitUtil.PROJ_1_DIAMOND_BOOTS, KitUtil.SHARP_3_DIAMOND_SWORD, KitUtil.POWER_3_BOW, 12, 2, Arrays.asList(KitUtil.FIRERESISTANCE_POTION)));

        this.randomOPKits.add(new KitItemContainer(KitUtil.PROJ_1_DIAMOND_HELMET,   KitUtil.PROT_3_DIAMOND_CHESTPLATE,      KitUtil.PROT_2_DIAMOND_LEGGINGS, KitUtil.PROT_3_DIAMOND_BOOTS, KitUtil.SHARP_3_FIRE_1_IRON_SWORD, KitUtil.POWER_2_BOW, 13, 3, Arrays.asList(KitUtil.FNS)));
        this.randomOPKits.add(new KitItemContainer(KitUtil.PROT_3_DIAMOND_HELMET,   KitUtil.PROT_3_DIAMOND_CHESTPLATE,      KitUtil.PROJ_1_DIAMOND_LEGGINGS, KitUtil.PROT_2_DIAMOND_BOOTS, KitUtil.SHARP_3_IRON_SWORD, KitUtil.POWER_2_FLAME_1_BOW, 15, 2, Arrays.asList(KitUtil.COBWEB)));

        this.randomOPKits.add(new KitItemContainer(KitUtil.PROT_1_DIAMOND_HELMET,   KitUtil.PROJ_1_DIAMOND_CHESTPLATE,      KitUtil.PROT_2_DIAMOND_LEGGINGS, KitUtil.PROT_2_DIAMOND_BOOTS, KitUtil.SHARP_4_DIAMOND_SWORD, KitUtil.POWER_3_BOW, 13, 1, Arrays.asList(KitUtil.SPEED_1_POTION)));
        this.randomOPKits.add(new KitItemContainer(KitUtil.PROJ_1_DIAMOND_HELMET,   KitUtil.PROT_2_DIAMOND_CHESTPLATE,      KitUtil.PROT_2_DIAMOND_LEGGINGS, KitUtil.PROT_1_DIAMOND_BOOTS, KitUtil.SHARP_4_IRON_SWORD, KitUtil.POWER_1_PUNCH_1_BOW, 15, 1, Arrays.asList(KitUtil.SPEED_1_POTION, KitUtil.COBWEB)));
        this.randomOPKits.add(new KitItemContainer(KitUtil.PROT_1_DIAMOND_HELMET,   KitUtil.PROT_2_DIAMOND_CHESTPLATE,      KitUtil.PROT_3_DIAMOND_LEGGINGS, KitUtil.PROJ_1_DIAMOND_BOOTS, KitUtil.SHARP_3_FIRE_1_IRON_SWORD, KitUtil.POWER_3_BOW, 16, 1, Arrays.asList(KitUtil.FNS, KitUtil.COBWEB)));
    }

    public static KitManager getInstance() {

        if (instance == null) {
            instance = new KitManager();
        }

        return instance;
    }

    public void giveKitItemContainerToPlayer(final KitItemContainer container, final Player player) {
        final UserData meetupPlayer = UserManager.getInstance().getUser(player.getUniqueId());
        final KitInventorySortation sortation = meetupPlayer.getSortation();
        player.getInventory().clear();
        player.getInventory().setArmorContents(null);
        player.getActivePotionEffects().forEach(potionEffect -> player.removePotionEffect(potionEffect.getType()));
        player.getInventory().setHelmet(container.getHelmet().getItem());
        player.getInventory().setChestplate(container.getChestplate().getItem());
        player.getInventory().setLeggings(container.getLeggings().getItem());
        player.getInventory().setBoots(container.getBoots().getItem());
        final List<ItemStack> notAddedItems = new ArrayList<ItemStack>();
        if (sortation.getSwordSlot() < 0) {
            notAddedItems.add(container.getSword().getItem());
        } else {
            player.getInventory().setItem(sortation.getSwordSlot(), container.getSword().getItem());
        }
        if (sortation.getRodSlot() < 0) {
            notAddedItems.add(new ItemStack(Material.FISHING_ROD));
        } else {
            player.getInventory().setItem(sortation.getRodSlot(), new ItemStack(Material.FISHING_ROD));
        }
        if (sortation.getBowSlot() < 0) {
            if (!Scenario.getByName("Bowless").isActive())
                notAddedItems.add(container.getBow().getItem());
        } else {
            if (!Scenario.getByName("Bowless").isActive())
                player.getInventory().setItem(sortation.getBowSlot(), container.getBow().getItem());
        }
        if (sortation.getGoldenAppleSlot() < 0) {
            notAddedItems.add(new ItemStack(Material.GOLDEN_APPLE, container.getGoldenApples()));
        } else {
            player.getInventory().setItem(sortation.getGoldenAppleSlot(), new ItemStack(Material.GOLDEN_APPLE, container.getGoldenApples()));
        }
        if (sortation.getGoldenHeadSlot() < 0) {
            if (container.getGoldenHeads() != 0)
                notAddedItems.add(ItemUtil.getGoldenHeads(container.getGoldenHeads()));
        } else {
            if (container.getGoldenHeads() != 0)
                player.getInventory().setItem(sortation.getGoldenHeadSlot(), ItemUtil.getGoldenHeads(container.getGoldenHeads()));
        }
        if (sortation.getCobbleStoneSlot() < 0) {
            notAddedItems.add(new ItemStack(Material.COBBLESTONE, 64));
        } else {
            player.getInventory().setItem(sortation.getCobbleStoneSlot(), new ItemStack(Material.COBBLESTONE, 64));
        }
        if (sortation.getWaterSlotOne() < 0) {
            notAddedItems.add(new ItemStack(Material.WATER_BUCKET));
        } else {
            player.getInventory().setItem(sortation.getWaterSlotOne(), new ItemStack(Material.WATER_BUCKET));
        }
        if (sortation.getLavaSlotOne() < 0) {
            notAddedItems.add(new ItemStack(Material.LAVA_BUCKET));
        } else {
            player.getInventory().setItem(sortation.getLavaSlotOne(), new ItemStack(Material.LAVA_BUCKET));
        }
        if (sortation.getArrowSlot() < 0) {
            if (!Scenario.getByName("Bowless").isActive())
                if (container.getBow().getItem().getType() != Material.AIR)
                    notAddedItems.add(new ItemStack(Material.ARROW, 20));
        } else {
            if (!Scenario.getByName("Bowless").isActive())
                if (container.getBow().getItem().getType() != Material.AIR)
                    player.getInventory().setItem(sortation.getArrowSlot(), new ItemStack(Material.ARROW, 20));
        }
        if (sortation.getPickAxeSlot() < 0) {
            notAddedItems.add(new ItemBuilder(Material.DIAMOND_PICKAXE).addEnchant(Enchantment.DIG_SPEED, 1).build());
        } else {
            player.getInventory().setItem(sortation.getPickAxeSlot(), new ItemBuilder(Material.DIAMOND_PICKAXE).addEnchant(Enchantment.DIG_SPEED, 1).build());
        }
        if (sortation.getWaterSlotTwo() < 0) {
            notAddedItems.add(new ItemStack(Material.WATER_BUCKET));
        } else {
            player.getInventory().setItem(sortation.getWaterSlotTwo(), new ItemStack(Material.WATER_BUCKET));
        }
        if (sortation.getLavaSlotTwo() < 0) {
            notAddedItems.add(new ItemStack(Material.LAVA_BUCKET));
        } else {
            player.getInventory().setItem(sortation.getLavaSlotTwo(), new ItemStack(Material.LAVA_BUCKET));
        }
        if (sortation.getAnvilSlot() < 0) {
            notAddedItems.add(new ItemStack(Material.ANVIL));
        } else {
            player.getInventory().setItem(sortation.getAnvilSlot(), new ItemStack(Material.ANVIL));
        }
        if (sortation.getWorkbenchSlot() < 0) {
            notAddedItems.add(new ItemStack(Material.WORKBENCH));
        } else {
            player.getInventory().setItem(sortation.getWorkbenchSlot(), new ItemStack(Material.WORKBENCH));
        }
        if (sortation.getFoodSlot() < 0) {
            notAddedItems.add(new ItemStack(Material.COOKED_BEEF, 10));
        } else {
            player.getInventory().setItem(sortation.getFoodSlot(), new ItemStack(Material.COOKED_BEEF, 10));
        }

        container.getExtraItems().forEach(item -> {
            if (item.getItem().getType() == Material.WEB) {
                if (sortation.getCobWebSlot() < 0)
                    notAddedItems.add(item.getItem());
                else
                    player.getInventory().setItem(sortation.getCobWebSlot(), item.getItem());
            } else if (item.getItem().getType() == Material.POTION && item.getItem().getData().getData() == 2) {
                if (sortation.getSpeedSlot() < 0)
                    notAddedItems.add(item.getItem());
                else
                    player.getInventory().setItem(sortation.getSpeedSlot(), item.getItem());
            } else if (item.getItem().getType() == Material.MONSTER_EGG && item.getItem().getData().getData() == 127) {
                if (sortation.getHorseSpawnSlot() < 0)
                    notAddedItems.add(item.getItem());
                else
                    player.getInventory().setItem(sortation.getHorseSpawnSlot(), item.getItem());
            } else if (item.getItem().getType() == Material.FLINT_AND_STEEL) {
                notAddedItems.add(item.getItem());
            }
        });

        for (ItemStack notAddedItem : notAddedItems) {
            player.getInventory().addItem(notAddedItem);
            //player.getInventory().addItem(new ItemStack[] { notAddedItem });
        }

        if (Scenario.getByName("Soup").isActive()) {
            player.getInventory().addItem(new ItemBuilder(Material.BOWL).setAmount(48).build());
            player.getInventory().addItem(new ItemBuilder(Material.RED_MUSHROOM).setAmount(48).build());
            player.getInventory().addItem(new ItemBuilder(Material.BROWN_MUSHROOM).setAmount(48).build());
        }
    }

    public Map<Integer, ItemStack> getKitSortation(final KitItemContainer container, final Player player) {
        final Map<Integer, ItemStack> stackMap = new HashMap<Integer, ItemStack>();
        final UserData meetupPlayer = UserManager.getInstance().getUser(player.getUniqueId());
        final KitInventorySortation sortation = meetupPlayer.getSortation();
        if (sortation.getSwordSlot() < 0) {
            stackMap.put(new KitInventorySortation().getSwordSlot(), new ItemStack(Material.DIAMOND_SWORD));
        } else {
            stackMap.put(sortation.getSwordSlot(), new ItemStack(Material.DIAMOND_SWORD));
        }
        if (sortation.getRodSlot() < 0) {
            stackMap.put(new KitInventorySortation().getRodSlot(), new ItemStack(Material.FISHING_ROD));
        } else {
            stackMap.put(sortation.getRodSlot(), new ItemStack(Material.FISHING_ROD));
        }
        if (sortation.getBowSlot() < 0) {
            stackMap.put(new KitInventorySortation().getBowSlot(), new ItemStack(Material.BOW));
        } else {
            stackMap.put(sortation.getBowSlot(), new ItemStack(Material.BOW));
        }
        if (sortation.getGoldenAppleSlot() < 0) {
            stackMap.put(new KitInventorySortation().getGoldenAppleSlot(), new ItemStack(Material.GOLDEN_APPLE, container.getGoldenApples()));
        } else {
            stackMap.put(sortation.getGoldenAppleSlot(), new ItemStack(Material.GOLDEN_APPLE, container.getGoldenApples()));
        }
        if (sortation.getGoldenHeadSlot() < 0) {
            stackMap.put(new KitInventorySortation().getGoldenHeadSlot(), ItemUtil.getGoldenHead());
        } else {
            stackMap.put(sortation.getGoldenHeadSlot(), ItemUtil.getGoldenHead());
        }
        if (sortation.getCobbleStoneSlot() < 0) {
            stackMap.put(new KitInventorySortation().getCobbleStoneSlot(), new ItemStack(Material.COBBLESTONE, 64));
        } else {
            stackMap.put(sortation.getCobbleStoneSlot(), new ItemStack(Material.COBBLESTONE, 64));
        }
        if (sortation.getWaterSlotOne() < 0) {
            stackMap.put(new KitInventorySortation().getWaterSlotOne(), new ItemStack(Material.WATER_BUCKET));
        } else {
            stackMap.put(sortation.getWaterSlotOne(), new ItemStack(Material.WATER_BUCKET));
        }
        if (sortation.getLavaSlotOne() < 0) {
            stackMap.put(new KitInventorySortation().getLavaSlotOne(), new ItemStack(Material.LAVA_BUCKET));
        } else {
            stackMap.put(sortation.getLavaSlotOne(), new ItemStack(Material.LAVA_BUCKET));
        }
        if (sortation.getArrowSlot() < 0) {
            stackMap.put(new KitInventorySortation().getArrowSlot(), new ItemStack(Material.ARROW, 20));
        } else {
            stackMap.put(sortation.getArrowSlot(), new ItemStack(Material.ARROW, 20));
        }
        if (sortation.getPickAxeSlot() < 0) {
            stackMap.put(new KitInventorySortation().getPickAxeSlot(), new ItemBuilder(Material.DIAMOND_PICKAXE).addEnchant(Enchantment.DIG_SPEED, 1).build());
        } else {
            stackMap.put(sortation.getPickAxeSlot(), new ItemBuilder(Material.DIAMOND_PICKAXE).addEnchant(Enchantment.DIG_SPEED, 1).build());
        }
        if (sortation.getWaterSlotTwo() < 0) {
            stackMap.put(new KitInventorySortation().getWaterSlotTwo(), new ItemStack(Material.WATER_BUCKET));
        } else {
            stackMap.put(sortation.getWaterSlotTwo(), new ItemStack(Material.WATER_BUCKET));
        }
        if (sortation.getLavaSlotTwo() < 0) {
            stackMap.put(new KitInventorySortation().getLavaSlotTwo(), new ItemStack(Material.LAVA_BUCKET));
        } else {
            stackMap.put(sortation.getLavaSlotTwo(), new ItemStack(Material.LAVA_BUCKET));
        }
        if (sortation.getAnvilSlot() < 0) {
            stackMap.put(new KitInventorySortation().getAnvilSlot(), new ItemStack(Material.ANVIL));
        } else {
            stackMap.put(sortation.getAnvilSlot(), new ItemStack(Material.ANVIL));
        }
        if (sortation.getWorkbenchSlot() < 0) {
            stackMap.put(new KitInventorySortation().getWorkbenchSlot(), new ItemStack(Material.WORKBENCH));
        } else {
            stackMap.put(sortation.getWorkbenchSlot(), new ItemStack(Material.WORKBENCH));
        }
        if (sortation.getFoodSlot() < 0) {
            stackMap.put(new KitInventorySortation().getFoodSlot(), new ItemStack(Material.COOKED_BEEF, 10));
        } else {
            stackMap.put(sortation.getFoodSlot(), new ItemStack(Material.COOKED_BEEF, 10));
        }

        /*if(sortation.getCobWebSlot() < 0)
            stackMap.put(new KitInventorySortation().getCobWebSlot(), new ItemStack(Material.WEB));
        else
            stackMap.put(sortation.getCobWebSlot(), new ItemStack(Material.WEB));

        if(sortation.getSpeedSlot() < 0)
            stackMap.put(new KitInventorySortation().getSpeedSlot(), new ItemBuilder(Material.POTION).setDurability((short) 8194).build());
        else
            stackMap.put(sortation.getSpeedSlot(), new ItemBuilder(Material.POTION).setDurability((short) 8194).build());

        if(sortation.getHorseSpawnSlot() < 0)
            stackMap.put(new KitInventorySortation().getHorseSpawnSlot(), new ItemBuilder(Material.MONSTER_EGG).setDurability((short) 100).build());
        else
            stackMap.put(sortation.getHorseSpawnSlot(), new ItemBuilder(Material.MONSTER_EGG).setDurability((short) 100).build());*/

        return stackMap;
    }

    public KitItemContainer getRandomOPKit() {
        ThreadLocalRandom random = ThreadLocalRandom.current();
        return this.randomOPKits.get(random.nextInt(0, this.randomOPKits.size() - 1));
    }

    public KitItemContainer getRandomKit() {
        ThreadLocalRandom random = ThreadLocalRandom.current();
        return this.randomKits.get(random.nextInt(0, this.randomKits.size() - 1));
    }
}
