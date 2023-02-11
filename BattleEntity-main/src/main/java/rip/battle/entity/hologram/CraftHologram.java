package rip.battle.entity.hologram;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import rip.battle.entity.Entity;
import rip.battle.entity.hologram.api.Hologram;
import rip.battle.entity.hologram.lines.HologramLine;
import rip.battle.entity.hologram.lines.impl.ItemLine;
import rip.battle.entity.hologram.lines.impl.MultipleTextLine;
import rip.battle.entity.hologram.lines.impl.SupplierMultipleTextLine;
import rip.battle.entity.hologram.lines.impl.TextLine;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Getter
public class CraftHologram implements Hologram {

    private final List<HologramLine<?>> lines = Lists.newArrayList();
    private Location location;

    private final Set<UUID> currentWatchers = Sets.newHashSet();
    private final Set<UUID> hiddenPlayers = Sets.newHashSet();

    private boolean hidden;

    private Entity parent;

    private String name;

    public CraftHologram(String name, String line, Location location) {
        this.name = name;
        this.location = location;
        addLine(line);
    }

    public CraftHologram(String name, ItemStack line, Location location) {
        this.name = name;
        this.location = location;
        addLine(line);
    }

    public CraftHologram(String name, Location location) {
        this.location = location;
        this.name = name;
    }

    public boolean isVisible() {
        return !hidden;
    }

    public boolean isVisibleTo(Player player) {
        return !hidden && location.getWorld() == player.getWorld() && location.distance(player.getLocation()) <= 32.0 && !hiddenPlayers.contains(player.getUniqueId());
    }

    @Override
    public void destroy() {
        onDelete();
    }

    @Override
    public void spawn() {
        Bukkit.getOnlinePlayers().forEach(player -> {
            if (isVisibleTo(player)) {
                lines.forEach(line -> line.spawn(player));
                currentWatchers.add(player.getUniqueId());
            }
        });
    }

    public void spawnFor(Player player) {
        lines.forEach(line -> line.spawn(player));

        currentWatchers.add(player.getUniqueId());
    }

    public void updateVisibility(boolean hidden){
        boolean hasChanged = this.hidden != hidden;

        this.hidden = hidden;

        if(hasChanged){
            lines.forEach(line -> line.updateVisibility(hidden));
        }
    }

    @Override
    public void updateVisibilityFor(Player player, boolean hidden) {
        boolean hasChanged = currentWatchers.contains(player.getUniqueId()) != hidden;

        if(hasChanged){

            if(hidden){
                hiddenPlayers.add(player.getUniqueId());
                currentWatchers.remove(player.getUniqueId());
            }else{
                currentWatchers.add(player.getUniqueId());
            }

            lines.forEach(line -> {
                if (line.isVisibleTo(player)) {
                    line.sendSpawnPackets(player);
                }else{
                    line.sendDestroyPackets(player);
                }
            });
        }
    }

    public List<Player> getCurrentWatchersPlayers() {
        return currentWatchers.stream().map(Bukkit::getPlayer).filter(Objects::nonNull).collect(Collectors.toList());
    }

    public boolean isWatcher(Player player){
        return currentWatchers.contains(player.getUniqueId());
    }

    protected void updateForCurrentWatchers() {
        lines.forEach(Entity::updateForCurrentWatchers);
    }


    public void onDelete() {
        lines.forEach(HologramLine::onDelete);
        lines.clear();

        currentWatchers.clear();
    }

    public void updateLocation(Location location){
        this.location = location;

        synchronizeLocations();
    }

    public void addLine(HologramLine<?> line) {
        getCurrentWatchersPlayers().forEach(line::spawn);
        lines.add(line);

        synchronizeLocations();
    }

    @Override
    public void addLine(String text){
        addLine(player -> text);
    }

    @Override
    public void addLine(Function<CommandSender, String> line) {
        addLine(line, null);
    }

    @Override
    public void addLines(List<Function<CommandSender, String>> lines) {
        lines.forEach(this::addLine);
    }


    @Override
    public void addLines(Function<CommandSender, List<String>> lines) {
        addLine(new MultipleTextLine(this, this.location.clone(), lines));
    }

    @Override
    public void addSupplierLines(Function<CommandSender, Supplier<List<String>>> lines) {
        addLine(new SupplierMultipleTextLine(this, this.location.clone(), lines));
    }

    @Override
    public void addLine(Function<CommandSender, String> line, Consumer<Player> onClick) {
        addLine(line, onClick, player -> true);
    }

    @Override
    public void addLine(Function<CommandSender, String> line, Consumer<Player> onClick, Predicate<Player> shouldSend) {
        addLine(new TextLine(this, this.location.clone().subtract(0, 0.25, 0), line, onClick, shouldSend));
    }

    @Override
    public void addLine(ItemStack itemStack){
        addLine(itemStack, null);
    }

    @Override
    public void addLine(ItemStack line, Consumer<Player> onClick) {
        addLine(line, onClick, player -> true);
    }

    @Override
    public void addLine(ItemStack line, Consumer<Player> onClick, Predicate<Player> shouldSend) {
        addLine(new ItemLine(this, this.location.clone().subtract(0, 0.25, 0), line, onClick, shouldSend));
    }

    @Override
    public void setLine(int line, String text) {
        setLine(line, player -> text);
    }

    @Override
    public void setLine(int line, Function<CommandSender, String> text) {
        setLine(line, text, null);
    }

    @Override
    public void setLine(int line, Function<CommandSender, String> text, Consumer<Player> onClick) {
        setLine(line, text, onClick, player -> true);
    }

    @Override
    public void setLine(int line, Function<CommandSender, String> text, Consumer<Player> onClick, Predicate<Player> shouldSend) {
        HologramLine<?> hologramLine = lines.get(line);

        if(hologramLine instanceof TextLine){
            TextLine textLine = (TextLine) hologramLine;
            textLine.setText(text);
            textLine.setOnLeftClick(onClick);
            textLine.setShouldSend(shouldSend);

            textLine.updateForCurrentWatchers();
        }
    }

    @Override
    public void setLine(int line, ItemStack itemStack) {
        setLine(line, itemStack, null);
    }

    @Override
    public void setLine(int line, ItemStack itemStack, Consumer<Player> onClick) {
        setLine(line, itemStack, onClick, player -> true);
    }

    @Override
    public void setLine(int line, ItemStack itemStack, Consumer<Player> onClick, Predicate<Player> shouldSend) {
        HologramLine<?> hologramLine = lines.get(line);

        if(hologramLine instanceof ItemLine){
            ItemLine itemLine = (ItemLine) hologramLine;
            itemLine.setItemStack(itemStack);
            itemLine.setOnLeftClick(onClick);
            itemLine.setShouldSend(shouldSend);

            itemLine.updateForCurrentWatchers();
        }
    }

    @Override
    public void removeLine(HologramLine<?> line) {
        lines.remove(line);

        getCurrentWatchersPlayers().forEach(line::destroy);
    }

    @Override
    public void removeLine(int index) {
        removeLine(lines.remove(index));
    }

    @Override
    public void teleport(Location location) {
        updateLocation(location);
    }

    public void clearLines() {
        getCurrentWatchersPlayers().forEach(player -> {
            for (HologramLine<?> line : lines) {
                line.sendDestroyPackets(player);
            }
        });
        lines.clear();
    }

    protected void synchronizeLocations(){
        Location startLocation = location.clone().add(0, lines.size() * 0.25, 0);

        for (HologramLine<?> line : lines) {
            if (line instanceof ItemLine){
                startLocation.subtract(0, 0.25, 0);
                line.updateLocation(startLocation.clone().add(0.0, 0.5, 0.0));
                startLocation.subtract(0, 0.5, 0);
            }if (line instanceof TextLine) {
                line.updateLocation(startLocation.clone());
                startLocation.subtract(0, 0.25, 0);
            }else if (line instanceof MultipleTextLine){
                MultipleTextLine multipleTextLine = (MultipleTextLine) line;
                multipleTextLine.updateLocation(startLocation.clone().add(0, 0.25, 0));
                startLocation.subtract(0, 0.25 * multipleTextLine.getLines().apply(Bukkit.getConsoleSender()).size(), 0);
            }
        }
    }

    public void setParent(Entity parent) {
        this.parent = parent;
    }
}
