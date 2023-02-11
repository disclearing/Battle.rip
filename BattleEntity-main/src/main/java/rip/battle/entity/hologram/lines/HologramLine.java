package rip.battle.entity.hologram.lines;

import lombok.Getter;
import lombok.Setter;
import net.minecraft.server.v1_8_R3.PacketPlayOutAttachEntity;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import rip.battle.entity.Entity;
import rip.battle.entity.hologram.CraftHologram;
import rip.battle.entity.utils.PacketUtils;

import java.util.function.Consumer;
import java.util.function.Predicate;

@Getter
@Setter
public abstract class HologramLine<T> extends Entity {

    private final CraftHologram parent;
    private Predicate<Player> shouldSend;

    private final int supportEntityID = nextId();

    public HologramLine(CraftHologram parent, Location location) {
        this(parent, location, player -> true, null);
    }

    public HologramLine(CraftHologram parent, Location location, Predicate<Player> shouldSend) {
        this(parent, location, shouldSend, null);
    }

    public HologramLine(CraftHologram parent, Location location, Consumer<Player> onClick) {
        this(parent, location, player -> true, onClick);
    }

    public HologramLine(CraftHologram parent, Location location, Predicate<Player> shouldSend, Consumer<Player> onClick) {
        super(location);
        this.parent = parent;
        this.shouldSend = shouldSend;
        this.setOnLeftClick(onClick);
    }

    public boolean shouldSend(Player player) {
        return shouldSend.test(player);
    }

    /*@Override
    public void onLeftClick(Player player) {
        player.sendMessage("You clicked a hologram line!");
    }*/

    @Override
    public void spawn() {
        super.spawn();

        parent.getCurrentWatchers().addAll(getCurrentWatchers());
    }

    @Override
    public void spawn(Player player) {
        super.spawn(player);

        parent.getCurrentWatchers().add(player.getUniqueId());
    }

    @Override
    public void destroy(Player player) {
        super.destroy(player);

        parent.getCurrentWatchers().remove(player.getUniqueId());
    }

    public abstract T getLine(Player player);

    public abstract void setLine(T line);

    public void sendAttachPacket(Player player){
        CraftHologram hologram = getParent();

        if (hologram == null || hologram.getParent() == null)
            return;

        PacketUtils.sendPacket(player, new PacketPlayOutAttachEntity(0, supportEntityID, hologram.getParent().getId()));
    }

    @Override
    public String getTypeName() {
        return "Hologram_Line";
    }

    @Override
    public boolean isRoot() {
        return false;
    }

    @Override
    public boolean isMultiPartEntity() {
        return true;
    }
}
