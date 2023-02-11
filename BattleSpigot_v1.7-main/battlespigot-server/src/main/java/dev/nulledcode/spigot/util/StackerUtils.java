package dev.nulledcode.spigot.util;

import dev.nulledcode.spigot.BattleSpigot;
import org.bukkit.ChatColor;
import org.bukkit.entity.Ageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;

import java.util.Arrays;
import java.util.List;

public class StackerUtils {

    static List<String> mobstackerlist = Arrays.asList("ZOMBIE", "SKELETON", "SPIDER", "CAVE_SPIDER", "SHEEP", "PIG", "CHICKEN", "COW", "IRON_GOLEM", "CREEPER");

    public static void unstackEntity(LivingEntity entity) {
        int stackSize = getAmount(entity);
        if (stackSize <= 1) return;

        String newName = ChatColor.GREEN + "x" + (stackSize - 1);
        LivingEntity toSpawn = (LivingEntity) entity.getWorld().spawnEntity(entity.getLocation(), entity.getType());

        toSpawn.setCustomName(newName);
        toSpawn.setCustomNameVisible(true);

        if (toSpawn instanceof Ageable) {
            ((Ageable) toSpawn).setAdult();
        }
    }

    public static void stackEntity(Entity stackOn, Entity toStack) {
        if (!(stackOn instanceof LivingEntity) || !(toStack instanceof LivingEntity)) return;
        if (toStack.getType() != stackOn.getType() || stackOn.isDead() || toStack.isDead()) return;

        if (!mobstackerlist.contains(stackOn.getType().name()) || !mobstackerlist.contains(toStack.getType().name())) return;
        if (stackOn.hasMetadata("CombatLogger") || toStack.hasMetadata("CombatLogger") || stackOn.hasMetadata("loggerBait") || toStack.hasMetadata("loggerBait")) return;

        LivingEntity livingStackOn = (LivingEntity) stackOn;
        LivingEntity livingToStack = (LivingEntity) toStack;

        int oldAmount = getAmount(livingStackOn);
        int newAmount = 1;

        if (isStacked(livingToStack)) newAmount = getAmount(livingToStack);

        int finalAmount = oldAmount == 0 ? newAmount + 1 : oldAmount + newAmount;
        //if (finalAmount > 150) return;

        toStack.remove();
        String name = ChatColor.GREEN + "x" + finalAmount;

        livingStackOn.setCustomName(name);
        livingStackOn.setCustomNameVisible(true);
    }

    public static int getAmount(LivingEntity entity) {
        if (entity.getCustomName() == null) return 0;

        String name = ChatColor.stripColor(entity.getCustomName().replace("x", ""));
        if (!BattleSpigot.isInteger(name)) return 0;

        return Integer.parseInt(name);
    }

    public static boolean isStacked(LivingEntity entity) {
        return getAmount(entity) != 0;
    }
}