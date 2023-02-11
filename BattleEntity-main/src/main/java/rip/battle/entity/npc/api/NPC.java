package rip.battle.entity.npc.api;

import cc.stormworth.core.util.skin.SkinTexture;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import rip.battle.entity.hologram.api.Hologram;

import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

public interface NPC {

    String getName();

    void setSkin(SkinTexture skin);

    SkinTexture getSkin();

    void destroy();

    void destroy(Player player);

    void spawn();

    void spawn(Player player);

    void updateVisibility(boolean hidden);

    void updateVisibilityFor(Player player, boolean hidden);

    boolean isVisible();

    boolean isVisibleTo(Player player);

    void teleport(double x, double y, double z);

    void teleport(double x, double y, double z, float yaw, float pitch);

    void teleport(Location location);

    void teleport(Location location, float yaw, float pitch);

    Location getLocation();

    ItemStack getHelmet();

    ItemStack getChestplate();

    ItemStack getLeggings();

    ItemStack getBoots();

    ItemStack getItemInHand();

    void setHelmet(ItemStack itemStack);

    void setChestplate(ItemStack itemStack);

    void setLeggings(ItemStack itemStack);

    void setBoots(ItemStack itemStack);

    void setItemInHand(ItemStack itemStack);

    void setArmor(Player player);

    void setArmor(ItemStack[] armor);

    void setArmor(ItemStack helmet, ItemStack chestplate, ItemStack leggings, ItemStack boots);

    void setDisplayName(String displayName);

    String getDisplayName();

    Hologram getHologram();

    void setHologram(Hologram hologram);

    void setMessages(List<String> messages);

    List<String> getMessages();

    Set<String> getCommands();

    void setOnRightClick(Consumer<Player> onRightClick);

    void setOnLeftClick(Consumer<Player> onLeftClick);
}
