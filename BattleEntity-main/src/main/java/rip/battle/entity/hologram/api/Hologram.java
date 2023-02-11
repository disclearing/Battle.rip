package rip.battle.entity.hologram.api;

import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import rip.battle.entity.hologram.lines.HologramLine;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

public interface Hologram {

    String getName();

    List<HologramLine<?>> getLines();

    void addLine(String line);

    void addLine(Function<CommandSender, String> line);

    void addLines(List<Function<CommandSender, String>> lines);

    void addLines(Function<CommandSender, List<String>> lines);
    void addSupplierLines(Function<CommandSender, Supplier<List<String>>> lines);

    void addLine(Function<CommandSender, String> line, Consumer<Player> onClick);

    void addLine(Function<CommandSender, String> line, Consumer<Player> onClick, Predicate<Player> shouldSend);

    void addLine(ItemStack line);

    void addLine(ItemStack line, Consumer<Player> onClick);

    void addLine(ItemStack line, Consumer<Player> onClick, Predicate<Player> shouldSend);

    void setLine(int line, String text);

    void setLine(int line, Function<CommandSender, String> text);

    void setLine(int line, Function<CommandSender, String> text, Consumer<Player> onClick);

    void setLine(int line, Function<CommandSender, String> text, Consumer<Player> onClick, Predicate<Player> shouldSend);

    void setLine(int line, ItemStack itemStack);

    void setLine(int line, ItemStack itemStack, Consumer<Player> onClick);

    void setLine(int line, ItemStack itemStack, Consumer<Player> onClick, Predicate<Player> shouldSend);

    void removeLine(HologramLine<?> line);

    void removeLine(int index);

    void teleport(Location location);

    Location getLocation();

    boolean isVisible();

    void updateVisibility(boolean hidden);

    void updateVisibilityFor(Player player, boolean hidden);

    boolean isVisibleTo(Player player);

    void destroy();

    void spawn();

}
