package cc.stormworth.meetup.kits;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

@Getter
@Setter
public class KitInventorySortation {

    private int swordSlot;
    private int rodSlot;
    private int bowSlot;
    private int goldenAppleSlot;
    private int goldenHeadSlot;
    private int cobbleStoneSlot;
    private int waterSlotOne;
    private int lavaSlotOne;
    private int pickAxeSlot;
    private int arrowSlot;
    private int anvilSlot;
    private int workbenchSlot;
    private int foodSlot;
    private int lavaSlotTwo;
    private int waterSlotTwo;
    private int speedSlot;
    private int fireResSlot;
    private int cobWebSlot;
    private int horseSpawnSlot;

    public KitInventorySortation() {
        this.swordSlot = 0;
        this.rodSlot = 1;
        this.bowSlot = 2;
        this.goldenAppleSlot = 3;
        this.goldenHeadSlot = 4;
        this.cobbleStoneSlot = 5;
        this.waterSlotOne = 6;
        this.lavaSlotOne = 7;
        this.pickAxeSlot = 8;
        this.arrowSlot = 9;
        this.anvilSlot = 10;
        this.workbenchSlot = 11;
        this.foodSlot = 12;
        this.lavaSlotTwo = 13;
        this.waterSlotTwo = 14;
        this.speedSlot = 15;
        this.fireResSlot = 16;
        this.cobWebSlot = 17;
        this.horseSpawnSlot = 18;
    }

    public static KitInventorySortation of(final ItemStack[] matrix) {
        final KitInventorySortation sortation = negative();
        int index = -1;
        for (final ItemStack itemStack : matrix) {
            ++index;

            if (itemStack == null || itemStack.getType() == Material.AIR) {
                continue;
            }

            if (sortation.getSwordSlot() < 0 && (itemStack.getType() == Material.IRON_SWORD || itemStack.getType() == Material.DIAMOND_SWORD)) {
                sortation.setSwordSlot(index);
            } else if (sortation.getRodSlot() < 0 && itemStack.getType() == Material.FISHING_ROD) {
                sortation.setRodSlot(index);
            } else if (sortation.getBowSlot() < 0 && itemStack.getType() == Material.BOW) {
                sortation.setBowSlot(index);
            } else if (sortation.getGoldenAppleSlot() < 0 && itemStack.getType() == Material.GOLDEN_APPLE && !itemStack.getItemMeta().hasDisplayName()) {
                sortation.setGoldenAppleSlot(index);
            } else if (sortation.getGoldenHeadSlot() < 0 && itemStack.getType() == Material.GOLDEN_APPLE && itemStack.getItemMeta().hasDisplayName()) {
                sortation.setGoldenHeadSlot(index);
            } else if (sortation.getCobbleStoneSlot() < 0 && itemStack.getType() == Material.COBBLESTONE) {
                sortation.setCobbleStoneSlot(index);
            } else if (sortation.getWaterSlotOne() < 0 && itemStack.getType() == Material.WATER_BUCKET) {
                sortation.setWaterSlotOne(index);
            } else if (sortation.getLavaSlotOne() < 0 && itemStack.getType() == Material.LAVA_BUCKET) {
                sortation.setLavaSlotOne(index);
            } else if (sortation.getArrowSlot() < 0 && itemStack.getType() == Material.ARROW) {
                sortation.setArrowSlot(index);
            } else if (sortation.getPickAxeSlot() < 0 && itemStack.getType() == Material.DIAMOND_PICKAXE) {
                sortation.setPickAxeSlot(index);
            } else if (sortation.getWaterSlotTwo() < 0 && itemStack.getType() == Material.WATER_BUCKET) {
                sortation.setWaterSlotTwo(index);
            } else if (sortation.getLavaSlotTwo() < 0 && itemStack.getType() == Material.LAVA_BUCKET) {
                sortation.setLavaSlotTwo(index);
            } else if (sortation.getAnvilSlot() < 0 && itemStack.getType() == Material.ANVIL) {
                sortation.setAnvilSlot(index);
            } else if (sortation.getWorkbenchSlot() < 0 && itemStack.getType() == Material.WORKBENCH) {
                sortation.setWorkbenchSlot(index);
            } else if (sortation.getFoodSlot() < 0 && itemStack.getType() == Material.COOKED_BEEF) {
                sortation.setFoodSlot(index);
            } else if (sortation.getCobWebSlot() < 0 && itemStack.getType() == Material.WEB) {
                sortation.setCobWebSlot(index);
            } else if (sortation.getSpeedSlot() < 0 && itemStack.getType() == Material.POTION && itemStack.getData().getData() == 2) {
                sortation.setSpeedSlot(index);
            } else {
                if (sortation.getFireResSlot() < 0 && itemStack.getType() == Material.POTION && itemStack.getData().getData() == 3) {
                    sortation.setFireResSlot(index);
                }
                if (sortation.getHorseSpawnSlot() < 0 && itemStack.getType() == Material.MONSTER_EGG && itemStack.getDurability() == 100) {
                    sortation.setHorseSpawnSlot(index);
                }
            }
        }

        return sortation;
    }

    private static KitInventorySortation negative() {
        final KitInventorySortation sortation = new KitInventorySortation();
        sortation.setSwordSlot(-5);
        sortation.setRodSlot(-5);
        sortation.setBowSlot(-5);
        sortation.setGoldenAppleSlot(-5);
        sortation.setGoldenHeadSlot(-5);
        sortation.setCobbleStoneSlot(-5);
        sortation.setWaterSlotOne(-5);
        sortation.setLavaSlotOne(-5);
        sortation.setPickAxeSlot(-5);
        sortation.setArrowSlot(-5);
        sortation.setAnvilSlot(-5);
        sortation.setWorkbenchSlot(-5);
        sortation.setFoodSlot(-5);
        sortation.setLavaSlotTwo(-5);
        sortation.setWaterSlotTwo(-5);
        sortation.setSpeedSlot(-5);
        sortation.setFireResSlot(-5);
        sortation.setCobWebSlot(-5);
        sortation.setHorseSpawnSlot(-5);
        return sortation;
    }
}