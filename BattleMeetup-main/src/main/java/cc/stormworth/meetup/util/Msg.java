package cc.stormworth.meetup.util;

import cc.stormworth.core.util.chat.CC;
import org.bukkit.Bukkit;
import org.bukkit.Sound;

public class Msg {

    public static void sendMessage(String message) {
        Bukkit.getOnlinePlayers().forEach(player -> player.sendMessage(message));
    }

    public static void sendMessage(String message, Sound sound) {
        Bukkit.getOnlinePlayers().forEach(player -> {
            player.sendMessage(message);
            player.playSound(player.getLocation(), sound, 1.0F, 1.0F);
        });
    }

    public static void sendMessage(Clickable clickable) {
        Bukkit.getOnlinePlayers().forEach(clickable::sendToPlayer);
    }

    public static void logConsole(String message) {
        Bukkit.getConsoleSender().sendMessage(CC.translate(message));
    }
}
