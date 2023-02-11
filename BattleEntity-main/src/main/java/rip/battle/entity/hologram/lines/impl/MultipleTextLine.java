package rip.battle.entity.hologram.lines.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import rip.battle.entity.hologram.CraftHologram;
import rip.battle.entity.hologram.lines.HologramLine;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;

@Getter
public class MultipleTextLine extends HologramLine<List<String>> {

    private Function<CommandSender, List<String>> lines;

    private final Map<UUID, List<TextLine>> textLines = Maps.newHashMap();

    public MultipleTextLine(CraftHologram parent, Location location, Function<CommandSender, List<String>> lines) {
        super(parent, location);
        this.lines = lines;
    }

    @Override
    public List<String> getLine(Player player) {
        return lines.apply(player);
    }

    @Override
    public void setLine(List<String> line) {
        this.lines = player -> line;
    }

    @Override
    public void sendSpawnPackets(Player player) {
        List<String> lines = getLine(player);

        List<TextLine> textLines = this.textLines.get(player.getUniqueId());

        Location location = getLocation().clone();

        if (textLines == null) {
            textLines = Lists.newArrayList();
        }

        for (String line : lines) {
            TextLine textLine = new TextLine(getParent(), location.subtract(0, 0.25, 0), player1 -> line);

            textLine.setMultiLine(true);
            textLine.spawn(player);

            textLines.add(textLine);
        }

        this.textLines.put(player.getUniqueId(), textLines);
    }

    @Override
    public void updateLocation(Location location) {
        setLocation(location);

       for (List<TextLine> textLines : this.textLines.values()) {

           Location locationCloned = getLocation().clone();

            for (TextLine textLine : textLines) {
                textLine.updateLocation(locationCloned.subtract(0, 0.25, 0));
            }
        }
    }

    @Override
    public void sendDestroyPackets(Player player) {

        List<TextLine> textLines = this.textLines.get(player.getUniqueId());

        if (textLines == null) {
            return;
        }

        for (TextLine textLine : textLines) {
            textLine.sendDestroyPackets(player);
        }

        this.textLines.remove(player.getUniqueId());
    }

    @Override
    public void sendUpdatePackets(Player player) {
        List<TextLine> textLines = this.textLines.get(player.getUniqueId());

        if (textLines == null) {
            return;
        }

        for (TextLine textLine : textLines) {
            textLine.sendUpdatePackets(player);
        }
    }
}
