package rip.battle.crates.utils;

import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftFirework;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.inventory.meta.FireworkMeta;

import java.util.Random;

public class FireworkUtil {

    public static void launchFirework(Location loc) {
        Firework fw = (Firework) loc.getWorld().spawnEntity(loc, EntityType.FIREWORK);
        FireworkMeta fwm = fw.getFireworkMeta();

        Random random = RandomUtils.getRandom();

        int randomInt = random.nextInt(4) + 1;
        FireworkEffect.Type type;

        if (randomInt == 1) {
            type = FireworkEffect.Type.BALL;
        } else if (randomInt == 2) {
            type = FireworkEffect.Type.BALL_LARGE;
        } else if (randomInt == 3) {
            type = FireworkEffect.Type.BURST;
        } else {
            type = FireworkEffect.Type.STAR;
        }


        int r1i = random.nextInt(17) + 1;
        int r2i = random.nextInt(17) + 1;

        Color c1 = getColor(r1i);
        Color c2 = getColor(r2i);

        FireworkEffect effect = FireworkEffect.builder()
                .flicker(random.nextBoolean())
                .withColor(c1)
                .withFade(c2).with(type).trail(random.nextBoolean()).build();

        fwm.addEffect(effect);

        fwm.setPower(3);

        ((CraftFirework) fw).getHandle().expectedLifespan = 5;

        fw.setFireworkMeta(fwm);
    }

    public static Color getColor(int i) {
        Color color = null;
        switch (i) {
            case 1:
                color = Color.AQUA;
                break;
            case 2:
                color = Color.BLACK;
                break;
            case 3:
                color = Color.BLUE;
                break;
            case 4:
                color = Color.FUCHSIA;
                break;
            case 5:
                color = Color.GRAY;
                break;
            case 6:
                color = Color.GREEN;
                break;
            case 7:
                color = Color.LIME;
                break;
            case 8:
                color = Color.MAROON;
                break;
            case 9:
                color = Color.NAVY;
                break;
            case 10:
                color = Color.OLIVE;
                break;
            case 11:
                color = Color.ORANGE;
                break;
            case 12:
                color = Color.PURPLE;
                break;
            case 13:
                color = Color.RED;
                break;
            case 14:
                color = Color.SILVER;
                break;
            case 15:
                color = Color.TEAL;
                break;
            case 16:
                color = Color.WHITE;
                break;
            case 17:
                color = Color.YELLOW;
                break;
            default:
                break;
        }
        return color;
    }
}