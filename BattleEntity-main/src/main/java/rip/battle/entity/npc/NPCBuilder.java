package rip.battle.entity.npc;

import cc.stormworth.core.util.UUIDUtils;
import cc.stormworth.core.util.skin.SkinTexture;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.NPC;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import rip.battle.entity.hologram.api.Hologram;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class NPCBuilder {

    public NPCEntity npc;

    public NPCBuilder(String name, Location location) {
        npc = new NPCEntity(name, location);
    }

    public NPCBuilder setSkin(SkinTexture skin) {
        npc.setSkin(skin);
        return this;
    }

    public NPCBuilder setSkin(Player player) {
        npc.setSkin(SkinTexture.getPlayerTexture(player));
        return this;
    }

    public NPCBuilder setSkin(String value, String signature) {
        npc.setSkin(new SkinTexture(value, signature));
        return this;
    }

    public NPCBuilder setSkin(String name) {
        UUID uuid = Bukkit.getOfflinePlayer(name).getUniqueId();

        if (uuid == null){
            CompletableFuture<UUID> future = UUIDUtils.getUUIDFromMojang(name);

            future.thenAccept(uuidFetch -> {
                if (uuidFetch == null){
                    return;
                }

                CompletableFuture<SkinTexture> skinFuture = UUIDUtils.getSkinFromMojang(uuidFetch);

                skinFuture.thenAccept(skinFetch -> {
                    if (skinFetch == null){
                        return;
                    }

                    npc.setSkin(skinFetch);
                });
            });
        }else {
            CompletableFuture<SkinTexture> skinFuture = UUIDUtils.getSkinFromMojang(uuid);

            skinFuture.thenAccept(skinFetch -> {
                if (skinFetch == null){
                    return;
                }

                npc.setSkin(skinFetch);
            });
        }
        return this;
    }

    public NPCBuilder setDisplayName(String displayName) {
        npc.setDisplayName(displayName);
        return this;
    }

    public NPCBuilder setArmor(Player player) {

        if (player.getInventory().getHelmet() != null) {
            npc.setHelmet(player.getInventory().getHelmet());
        }

        if (player.getInventory().getChestplate() != null) {
            npc.setChestplate(player.getInventory().getChestplate());
        }

        if (player.getInventory().getLeggings() != null) {
            npc.setLeggings(player.getInventory().getLeggings());
        }

        if (player.getInventory().getBoots() != null) {
            npc.setBoots(player.getInventory().getBoots());
        }

        return this;
    }

    public NPCBuilder setArmor(ItemStack[] armor){
        npc.setArmor(armor);
        return this;
    }

    public NPCBuilder setHelmet(ItemStack helmet) {
        npc.setHelmet(helmet);
        return this;
    }

    public NPCBuilder setChestplate(ItemStack chestplate) {
        npc.setChestplate(chestplate);
        return this;
    }

    public NPCBuilder setLeggings(ItemStack leggings) {
        npc.setLeggings(leggings);
        return this;
    }

    public NPCBuilder setBoots(ItemStack boots) {
        npc.setBoots(boots);
        return this;
    }

    public NPCBuilder setItemInHand(ItemStack itemInHand) {
        npc.setItemInHand(itemInHand);
        return this;
    }

    public NPCBuilder onInteract(Consumer<Player> consumer) {
        npc.setOnLeftClick(consumer);
        return this;
    }

    public NPCBuilder onAttack(Consumer<Player> consumer) {
        npc.setOnRightClick(consumer);
        return this;
    }

    public NPCBuilder setHologram(Hologram hologram) {
        npc.setHologram(hologram);
        return this;
    }

    public NPCBuilder setCommands(Set<String> commands) {
        npc.setCommands(commands);
        return this;
    }

    public NPCBuilder addCommand(String command) {
        npc.getCommands().add(command);
        return this;
    }

    public NPCBuilder removeCommand(String command) {
        npc.getCommands().remove(command);
        return this;
    }

    public NPCBuilder setMessages(List<String> messages) {
        npc.setMessages(messages);
        return this;
    }

    public NPCBuilder addMessage(String message) {
        npc.getMessages().add(message);
        return this;
    }

    public NPCBuilder removeMessage(String message) {
        npc.getMessages().remove(message);
        return this;
    }

    public NPCBuilder setMessage(int index, String message) {
        npc.getMessages().set(index, message);
        return this;
    }

    public NPCBuilder setOnRightClick(Consumer<Player> onRightClick) {
        npc.setOnRightClick(onRightClick);
        return this;
    }

    public NPCBuilder setOnLeftClick(Consumer<Player> onLeftClick) {
        npc.setOnLeftClick(onLeftClick);
        return this;
    }

    public NPCEntity build() {
        return npc;
    }

}
