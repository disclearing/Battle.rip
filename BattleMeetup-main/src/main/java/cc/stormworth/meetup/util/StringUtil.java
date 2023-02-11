package cc.stormworth.meetup.util;

import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.*;

public class StringUtil {

    public static String niceBuilder(Collection<String> collection) {
        return niceBuilder(collection, ", ", " and ", ".");
    }

    public static String niceBuilder(Collection<String> collection, String color) {
        return niceBuilder(collection, color + ", ", color + " and ", color + '.');
    }

    public static String niceBuilder(Collection<String> collection, String delimiter, String and, String dot) {
        if (collection != null && !collection.isEmpty()) {
            List<String> contents = new ArrayList<>(collection);

            // removing last object from list
            String last = null;
            if (contents.size() > 1) {
                last = contents.remove(contents.size() - 1);
            }

            StringBuilder builder = new StringBuilder();
            Iterator<String> iterator = contents.iterator();

            while (iterator.hasNext()) {
                String name = iterator.next();

                if (builder.length() > 0) {
                    builder.append(delimiter);
                }

                builder.append(name);
            }

            if (last != null) {
                builder.append(and).append(last);
            }

            return builder.append(dot != null ? dot : "").toString();
        }

        return "";
    }

    public static String listToString(List<String> list) {
        String s = list.toString().replace("[", "").replace("]", "");
        return s;
    }

    public static String inventoryToString(Inventory inventory) {

        try {
            ByteArrayOutputStream str = new ByteArrayOutputStream();
            BukkitObjectOutputStream data = new BukkitObjectOutputStream(str);
            data.writeInt(inventory.getSize());
            data.writeObject(inventory.getName());

            for (int i = 0; i < inventory.getSize(); i++) {
                data.writeObject(inventory.getItem(i));
            }

            data.close();
            return Base64.getEncoder().encodeToString(str.toByteArray());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "";
    }

    public static Inventory stringToInventory(String inventoryData) {

        if (inventoryData.isEmpty()) return null;

        try {
            ByteArrayInputStream stream = new ByteArrayInputStream(Base64.getDecoder().decode(inventoryData));
            BukkitObjectInputStream data = new BukkitObjectInputStream(stream);
            Inventory inventory = Bukkit.createInventory(null, data.readInt(), data.readObject().toString());

            for (int i = 0; i < inventory.getSize(); i++) {
                inventory.setItem(i, (ItemStack) data.readObject());
            }

            data.close();
            return inventory;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
