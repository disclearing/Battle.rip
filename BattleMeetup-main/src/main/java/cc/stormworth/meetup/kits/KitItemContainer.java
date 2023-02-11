package cc.stormworth.meetup.kits;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class KitItemContainer {

    private final KitItem helmet;
    private final KitItem chestplate;
    private final KitItem leggings;
    private final KitItem boots;
    private final KitItem sword;
    private final KitItem bow;
    private final int goldenApples;
    private final int goldenHeads;
    private final List<KitItem> extraItems;

    public KitItemContainer(final KitItem helmet, final KitItem chestplate, final KitItem leggings, final KitItem boots, final KitItem sword, final KitItem bow, final int gaps, final int gheads, final List<KitItem> extraItems) {
        this.helmet = helmet;
        this.chestplate = chestplate;
        this.leggings = leggings;
        this.boots = boots;
        this.sword = sword;
        this.bow = bow;
        this.goldenApples = gaps;
        this.goldenHeads = gheads;
        this.extraItems = new ArrayList<>(extraItems);
    }

}
