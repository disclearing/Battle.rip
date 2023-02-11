package cc.stormworth.meetup.style.hcf.kit;

import lombok.Getter;
import lombok.Setter;
import org.bson.Document;
import org.bukkit.inventory.ItemStack;

@Getter
@Setter
public class HCFKitSortation {

    private int abilitySlot;
    private int swordSlot;
    private int pearlSlot;
    private int rodSlot;
    private int bowSlot;
    private int arrowSlot;
    private int webSlot;
    private int foodSlot;
    private int firstPotionSlot;
    private int secondPotionSlot;
    private int thirdPotionSlot;
    private int fourthPotionSlot;

    public int getSlot(ItemStack item) {
        switch (item.getType()) {
            default:
                return -1;
            case ENDER_CHEST:
            case SNOW_BALL:
            case GOLD_HOE:
            case MONSTER_EGG:
            case BLAZE_POWDER:
            case TNT:
            case GHAST_TEAR:
            case INK_SACK:
            case REDSTONE:
            case BLAZE_ROD:
            case RED_MUSHROOM:
                return this.convertToPlayerInventory(abilitySlot);
            case DIAMOND_SWORD:
                return this.convertToPlayerInventory(swordSlot);
            case ENDER_PEARL:
                return this.convertToPlayerInventory(pearlSlot);
            case FISHING_ROD:
                return this.convertToPlayerInventory(rodSlot);
            case BOW:
                return this.convertToPlayerInventory(bowSlot);
            case ARROW:
                return this.convertToPlayerInventory(arrowSlot);
            case WEB:
                return this.convertToPlayerInventory(webSlot);
            case COOKED_BEEF:
                return this.convertToPlayerInventory(foodSlot);
            case POTION:
                // I feel bad for this but I don't like switch in switch
                short data = item.getDurability();
                if (data == 16388) return this.convertToPlayerInventory(this.firstPotionSlot);
                if (data == 16426) return this.convertToPlayerInventory(this.secondPotionSlot);
                if (data == 16424) return this.convertToPlayerInventory(this.thirdPotionSlot);
                if (data == 16428) return this.convertToPlayerInventory(this.fourthPotionSlot);
                return -1;
        }
    }

    public void loadDefaults() {
        this.abilitySlot = 6;
        this.swordSlot = 0;
        this.pearlSlot = 1;
        this.rodSlot = 2;
        this.bowSlot = 7;
        this.foodSlot = 8;
        this.webSlot = 9;
        this.arrowSlot = 27;

        this.firstPotionSlot = 17;
        this.secondPotionSlot = 26;
        this.thirdPotionSlot = 35;
        this.fourthPotionSlot = 16;
    }

    public HCFKitSortation unserialize(Document document) {
        if (document == null) {
            this.loadDefaults();

            return this;
        }

        this.abilitySlot = document.getInteger("abilitySlot", 1);
        this.swordSlot = document.getInteger("swordSlot", 0);
        this.pearlSlot = document.getInteger("pearlSlot", 2);
        this.rodSlot = document.getInteger("rodSlot", 3);
        this.bowSlot = document.getInteger("bowSlot", 4);
        this.arrowSlot = document.getInteger("arrowSlot", 5);
        this.webSlot = document.getInteger("webSlot", 6);
        this.foodSlot = document.getInteger("foodSlot", 7);

        this.firstPotionSlot = document.getInteger("firstPotionSlot", 8);
        this.secondPotionSlot = document.getInteger("secondPotionSlot", 9);
        this.thirdPotionSlot = document.getInteger("thirdPotionSlot", 10);
        this.fourthPotionSlot = document.getInteger("fourthPotionSlot", 11);

        return this;
    }

    public Document serialize() {
        return new Document("abilitySlot", this.abilitySlot)
                .append("swordSlot", this.swordSlot)
                .append("pearlSlot", this.pearlSlot)
                .append("rodSlot", this.rodSlot)
                .append("bowSlot", this.bowSlot)
                .append("arrowSlot", this.arrowSlot)
                .append("webSlot", this.webSlot)
                .append("foodSlot", this.foodSlot)
                .append("firstPotionSlot", this.firstPotionSlot)
                .append("secondPotionSlot", this.secondPotionSlot)
                .append("thirdPotionSlot", this.thirdPotionSlot)
                .append("fourthPotionSlot", this.fourthPotionSlot);
    }

    private int convertToPlayerInventory(int slot) {
        return (slot < 9 ? slot : slot < 18 ? slot + 18 : slot < 27 ? slot - 9 : slot - 18);
    }
}