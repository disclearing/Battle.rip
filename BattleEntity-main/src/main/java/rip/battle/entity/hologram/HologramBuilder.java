package rip.battle.entity.hologram;

import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import rip.battle.entity.hologram.api.Hologram;
import rip.battle.entity.hologram.impl.UpdatableHologram;
import rip.battle.entity.hologram.lines.HologramLine;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

public class HologramBuilder {

    private final Hologram hologram;

    public HologramBuilder(String name, Location location, boolean updatable) {
        if (updatable) {
            hologram = new UpdatableHologram(name, location) {
                @Override
                public int getUpdateInterval() {
                    return 1;
                }
            };
        } else {
            hologram = new CraftHologram(name, location);
        }
    }

    public HologramBuilder addLine(String line) {
        hologram.addLine(line);
        return this;
    }

    public HologramBuilder addLine(ItemStack line) {
        hologram.addLine(line);
        return this;
    }

    public HologramBuilder addLine(ItemStack line, Consumer<Player> onClick) {
        hologram.addLine(line, onClick);
        return this;
    }

    public HologramBuilder addLine(ItemStack line, Consumer<Player> onClick, Predicate<Player> shouldSend) {
        hologram.addLine(line, onClick, shouldSend);
        return this;
    }

    public HologramBuilder addLine(String line, Consumer<Player> onClick) {
        hologram.addLine(player -> line, onClick);
        return this;
    }

    public HologramBuilder addLine(String line, Consumer<Player> onClick, Predicate<Player> shouldSend) {
        hologram.addLine(player -> line, onClick, shouldSend);
        return this;
    }


    public HologramBuilder addLine(Function<CommandSender, String> line, Consumer<Player> onClick, Predicate<Player> shouldSend) {
        hologram.addLine(line, onClick, shouldSend);
        return this;
    }

    public HologramBuilder addLine(Function<CommandSender, String> line, Consumer<Player> onClick) {
        hologram.addLine(line, onClick);
        return this;
    }

    public HologramBuilder addLine(Function<CommandSender, String> line) {
        hologram.addLine(line);
        return this;
    }

    public HologramBuilder removeLine(int index) {
        hologram.removeLine(index);
        return this;
    }

    public HologramBuilder removeLine(HologramLine<?> line) {
        hologram.removeLine(line);
        return this;
    }

    public HologramBuilder addLines(String... lines) {
        for (String line : lines) {
            hologram.addLine(line);
        }
        return this;
    }

    public HologramBuilder addLines(ItemStack... lines) {
        for (ItemStack line : lines) {
            hologram.addLine(line);
        }
        return this;
    }

    @SafeVarargs
    public final HologramBuilder addLines(Function<CommandSender, String>... lines) {
        for (Function<CommandSender, String> line : lines) {
            hologram.addLine(line);
        }
        return this;
    }

    public HologramBuilder hide() {
        hologram.updateVisibility(true);
        return this;
    }

    public HologramBuilder show() {
        hologram.updateVisibility(false);
        return this;
    }

    public HologramBuilder hideFor(Player player) {
        hologram.updateVisibilityFor(player, true);
        return this;
    }

    public HologramBuilder showFor(Player player) {
        hologram.updateVisibilityFor(player, false);
        return this;
    }

    public Hologram build() {
        return hologram;
    }
}
