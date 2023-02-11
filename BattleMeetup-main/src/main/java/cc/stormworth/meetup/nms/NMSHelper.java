package cc.stormworth.meetup.nms;

import net.minecraft.server.v1_7_R4.EntityPlayer;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;

import java.lang.reflect.Field;
import java.util.stream.Stream;

public class NMSHelper {

    public static void setValueStatic(Object object, String name, Object value) {
        try {
            Field field = object.getClass().getDeclaredField(name);
            field.setAccessible(true);
            field.set(object, value);
        } catch (Exception var4) {
            var4.printStackTrace();
        }

    }

    public static Object getValueStatic(Object object, String name) {
        try {
            Field field = object.getClass().getDeclaredField(name);
            field.setAccessible(true);
            return field.get(object);
        } catch (Exception var3) {
            var3.printStackTrace();
            return null;
        }
    }

    protected void setValue(Object object, String name, Object value) {
        try {
            Field field = object.getClass().getDeclaredField(name);
            field.setAccessible(true);
            field.set(object, value);
        } catch (Exception var5) {
            var5.printStackTrace();
        }

    }

    protected Object getValue(Object object, String name) {
        try {
            Field field = object.getClass().getDeclaredField(name);
            field.setAccessible(true);
            return field.get(object);
        } catch (Exception var4) {
            var4.printStackTrace();
            return null;
        }
    }

    protected Stream<EntityPlayer> getEntityPlayers(Location location) {
        return location.getWorld().getPlayers().stream().map((player) -> ((CraftPlayer) player).getHandle());
    }
}
