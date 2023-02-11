package rip.battle.entity.taks;

import com.google.common.collect.Lists;
import lombok.Getter;
import org.bukkit.ChatColor;

import java.util.List;

public class RainbowTask implements Runnable {

    private int index = 0;

    private static final List<ChatColor> options = Lists.newArrayList(
            ChatColor.DARK_RED,
            ChatColor.RED,
            ChatColor.GOLD,
            ChatColor.YELLOW,
            ChatColor.GREEN,
            ChatColor.DARK_GREEN,
            ChatColor.AQUA,
            ChatColor.DARK_AQUA,
            ChatColor.BLUE,
            ChatColor.DARK_BLUE,
            ChatColor.DARK_PURPLE,
            ChatColor.LIGHT_PURPLE
    );

    @Getter public static ChatColor currentColor = options.get(0);

    @Override
    public void run() {
        index++;
        if (index >= options.size()) {
            index = 0;
        }

        currentColor = options.get(index);
    }
}
