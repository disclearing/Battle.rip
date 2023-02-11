package cc.stormworth.meetup.scenarios.impl;

import cc.stormworth.core.util.holograms.Hologram;
import cc.stormworth.core.util.holograms.Holograms;
import cc.stormworth.meetup.Meetup;
import cc.stormworth.meetup.scenarios.Scenario;
import cc.stormworth.meetup.util.*;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.beans.ConstructorProperties;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicReference;

public class TimebombScenario extends Scenario implements Listener {
    private static Map<String, Timebomb> TimebombS;

    private static int TASK;

    public TimebombScenario() {
        super("Timebomb", new ItemStack(Material.TNT), false, "Upon death, a chest will spawn", "containing all the deceased player's loot.", "This chest explodes after 30 seconds.");
    }

    public static void start() {
        TimebombS = new HashMap<>();
        TASK = (new BukkitRunnable() {
            public void run() {
                if (TimebombScenario.TimebombS.isEmpty())
                    return;
                Iterator<Map.Entry<String, Timebomb>> iterator = TimebombScenario.TimebombS.entrySet().iterator();
                while (iterator.hasNext()) {
                    Map.Entry<String, Timebomb> next = iterator.next();
                    String name = next.getKey().split(";")[0];
                    Timebomb Timebomb = next.getValue();
                    long minusTime = System.currentTimeMillis() - Timebomb.time;
                    Hologram hologram = Timebomb.hologram;
                    if (Timebomb.sf && minusTime > 10000L && !hologram.getLines().get(hologram.getLines().size() - 1).isEmpty()) {
                        hologram.setLine(hologram.getLines().size() - 1, "");
                        Timebomb.sf = false;
                    }
                    if (minusTime > 30000L) {
                        hologram.destroy();
                        Chest chest = Timebomb.chest;
                        TaskUtil.run(() -> {
                            Location where = null;
                            if (chest != null && chest.getBlockInventory() != null) {
                                where = chest.getLocation();
                                chest.getBlockInventory().clear();
                                chest.setType(Material.AIR);
                            }
                            Chest secondChest = Timebomb.secondChest;
                            if (secondChest != null && secondChest.getBlockInventory() != null) {
                                if (where == null)
                                    where = secondChest.getLocation();
                                secondChest.getBlockInventory().clear();
                                secondChest.setType(Material.AIR);
                            }
                            if (where != null)
                                where.getWorld().createExplosion(where, 6.0F);
                        });
                        Msg.sendMessage(Colors.GRAY + "[" + Colors.PRIMARY + "Timebomb" + Colors.GRAY + "] " + Colors.SECONDARY + name + "'s corpse has exploded!");
                        iterator.remove();
                        continue;
                    }
                    hologram.setLine(1, Colors.SECONDARY + "Exploding in: " + Colors.PRIMARY + TimeUtil.formatDuration(30000L - minusTime).replace(" seconds", "").replace(" second", "") + "s");
                }
            }
        }).runTaskTimerAsynchronously(Meetup.getInstance(), 0L, 20L).getTaskId();
    }

    public static void cleanup() {
        TimebombS.clear();
        TimebombS = null;
        Bukkit.getScheduler().cancelTask(TASK);
    }

    public static void handleTimebomb(LivingEntity entity, List<ItemStack> drops, List<ItemStack> items, boolean tb, boolean sf) {
        final Location where = entity.getLocation();

        // Spawn the first chest
        where.getBlock().setType(Material.AIR);
        where.getBlock().setType(Material.CHEST);

        Chest chest = (Chest) where.getBlock().getState();
        Chest secondChest;

        // Spawn the second chest
        chest.getLocation().add(1.0D, 0.0D, 0.0D).getBlock().setType(Material.AIR);
        chest.getLocation().add(1.0D, 0.0D, 0.0D).getBlock().setType(Material.CHEST);

        try {
            secondChest = (Chest) chest.getLocation().add(1.0D, 0.0D, 0.0D).getBlock().getState();
        } catch (Exception e) {
            secondChest = chest;
        }

        // Clear the blocks above the double chest
        chest.getLocation().add(0.0D, 1.0D, 0.0D).getBlock().setType(Material.AIR);
        chest.getLocation().add(1.0D, 1.0D, 0.0D).getBlock().setType(Material.AIR);

        String entityName = ChatColor.stripColor((entity instanceof Player) ? ((Player) entity).getName() : entity.getCustomName());
        if (sf) {
            Player killer = entity.getKiller();
            if (killer != null) {
                AtomicReference<Hologram> hologram = new AtomicReference<>();
                if (!tb) {
                    hologram.set(Holograms.newHologram().addLines(Colors.PRIMARY + entityName + Colors.SECONDARY + "'s corpse", Colors.SECONDARY + "Locked to " + Colors.PRIMARY + Colors.PRIMARY + killer.getDisguisedName()).at(chest.getLocation().clone().add(1.0D, 1.25D, 0.5D)).build());
                    hologram.get().send();
                }
                TaskUtil.runLater(() -> killer.sendMessage(Colors.SECONDARY + entityName + "'s chest is now protected for " + Colors.PRIMARY + "20 seconds" + Colors.SECONDARY + "."), 1L);
                chest.setMetadata("SafeLoot", new FixedMetadataValue(Meetup.getInstance(), killer.getName()));
                secondChest.setMetadata("SafeLoot", new FixedMetadataValue(Meetup.getInstance(), killer.getName()));
                Chest finalSecondChest = secondChest;
                TaskUtil.runLater(() -> {
                    chest.removeMetadata("SafeLoot", Meetup.getInstance());
                    finalSecondChest.removeMetadata("SafeLoot", Meetup.getInstance());
                    if (hologram.get() != null)
                        hologram.get().destroy();
                    if (killer.isOnline())
                        killer.sendMessage(Colors.SECONDARY + entityName + "'s chest is no longer protected.");
                }, 400L);
            }
        }
        items.forEach(stack -> chest.getInventory().addItem(new ItemStack[]{stack}));
        if (Scenario.getByName("Hypixel Heads").isActive()) {
            chest.getInventory().addItem(HypixelHeadsScenario.getSkullItem(entityName));
        } else {
            chest.getInventory().addItem(ItemUtil.getGoldenHead());
        }
        if (Scenario.getByName("Golden Retriever").isActive())
            chest.getInventory().addItem(ItemUtil.getGoldenHead());

        chest.getInventory().addItem(new ItemStack(Material.EXP_BOTTLE, 32));
        if (tb) {
            Hologram hologram = Holograms.newHologram().addLines(Colors.PRIMARY + entityName + Colors.SECONDARY + "'s corpse", Colors.SECONDARY + "Exploding in: " + Colors.PRIMARY + "30s").at(chest.getLocation().clone().add(1.0D, 1.25D, 0.5D)).build();
            hologram.send();
            if (sf && entity instanceof Player && ((Player) entity).getPlayer().getKiller() != null)
                hologram.addLines(Colors.SECONDARY + "Locked to " + Colors.PRIMARY + ((Player) entity).getPlayer().getKiller().getDisguisedName());
            TimebombS.put(entityName + ';' + ThreadLocalRandom.current().nextInt(300), new Timebomb(sf, System.currentTimeMillis(), hologram, chest, secondChest));
        }
    }

    @EventHandler
    public void onEntityExplode(EntityExplodeEvent event) {
        event.blockList().removeIf(block -> (block.getType() == Material.BEDROCK));
    }

    /*public static void handleTimebomb(LivingEntity entity, UserData data, boolean tb, boolean sf) {
        final Location where = entity.getLocation();

        // Spawn the first chest
        where.getBlock().setType(Material.AIR);
        where.getBlock().setType(Material.CHEST);

        Chest chest = (Chest) where.getBlock().getState();
        Chest secondChest;

        // Spawn the second chest
        chest.getLocation().add(1.0D, 0.0D, 0.0D).getBlock().setType(Material.AIR);
        chest.getLocation().add(1.0D, 0.0D, 0.0D).getBlock().setType(Material.CHEST);

        try {
            secondChest = (Chest) chest.getLocation().add(1.0D, 0.0D, 0.0D).getBlock().getState();
        } catch (Exception e) {
            secondChest = chest;
        }

        // Clear the blocks above the double chest
        chest.getLocation().add(0.0D, 1.0D, 0.0D).getBlock().setType(Material.AIR);
        chest.getLocation().add(1.0D, 1.0D, 0.0D).getBlock().setType(Material.AIR);

        String entityName = ChatColor.stripColor((entity instanceof Player) ? ((Player) entity).getName() : entity.getCustomName());
        if (sf) {
            Player killer = entity.getKiller();
            if (killer != null) {
                AtomicReference<Hologram> hologram = new AtomicReference<>();
                if (!tb) {
                    hologram.set(new Hologram(Colors.SECONDARY + "Locked to " + Colors.PRIMARY + Colors.PRIMARY + killer.getName(), chest.getLocation().clone().add(1.0D, 1.25D, 0.5D)));
                    ((Hologram) hologram.get()).spawn(false);
                    ((Hologram) hologram.get()).addLineAbove(Colors.PRIMARY + entityName + Colors.SECONDARY + "'s corpse");
                }
                TaskUtil.runLater(() -> killer.sendMessage(Colors.SECONDARY + entityName + "'s chest is now protected for " + Colors.PRIMARY + "20 seconds" + Colors.SECONDARY + "."), 1L);
                chest.setMetadata("SafeLoot", (MetadataValue) new FixedMetadataValue((Plugin) Main.getInstance(), killer.getName()));
                secondChest.setMetadata("SafeLoot", (MetadataValue) new FixedMetadataValue((Plugin) Main.getInstance(), killer.getName()));
                Chest finalSecondChest = secondChest;
                TaskUtil.runLater(() -> {
                    chest.removeMetadata("SafeLoot", (Plugin) Main.getInstance());
                    finalSecondChest.removeMetadata("SafeLoot", (Plugin) Main.getInstance());
                    if (hologram.get() != null)
                        ((Hologram) hologram.get()).delete(true);
                    if (killer.isOnline())
                        killer.sendMessage(Colors.SECONDARY + entityName + "'s chest is no longer protected.");
                }, 400L);
            }
        }

        chest.getInventory().setContents(data.getItems());
        chest.getInventory().addItem(data.getArmor());

        if (Scenario.getByName("Hypixel Heads").isActive()) {
            chest.getInventory().addItem(HypixelHeadsScenario.getSkullItem(entityName));
        } else {
            chest.getInventory().addItem(ItemUtil.getGoldenHead());
        }
        if (Scenario.getByName("Golden Retriever").isActive())
            chest.getInventory().addItem(ItemUtil.getGoldenHead());
        if (Scenario.getByName("Diamondless").isActive())
            chest.getInventory().addItem(new ItemStack(Material.DIAMOND, 1));
        if (Scenario.getByName("Goldless").isActive()) {
            chest.getInventory().addItem(new ItemStack(Material.GOLD_INGOT, 8));
            chest.getInventory().addItem(ItemUtil.getGoldenHead());
        }
        if (Scenario.getByName("BareBones").isActive()) {
            chest.getInventory().addItem(new ItemStack(Material.GOLDEN_APPLE));
            chest.getInventory().addItem(new ItemStack(Material.DIAMOND));
            chest.getInventory().addItem(new ItemStack(Material.ARROW, 32));
            chest.getInventory().addItem(new ItemStack(Material.STRING, 2));
        }
        if (tb) {
            Hologram hologram = new Hologram(Colors.SECONDARY + "Exploding in: " + Colors.PRIMARY + "30s", chest.getLocation().clone().add(1.0D, 1.25D, 0.5D));
            hologram.spawn(false);
            hologram.addLineAbove(Colors.PRIMARY + entityName + Colors.SECONDARY + "'s corpse");
            if (sf && entity instanceof Player && ((Player) entity).getPlayer().getKiller() != null)
                hologram.addLineBelow(Colors.SECONDARY + "Locked to " + Colors.PRIMARY + ((Player) entity).getPlayer().getKiller().getName());
            TimebombS.put(entityName + ';' + ThreadLocalRandom.current().nextInt(300), new Timebomb(sf, System.currentTimeMillis(), hologram, chest, secondChest));
        }
    }*/

    public static class Timebomb {
        public boolean sf;

        public long time;

        public Hologram hologram;

        public Chest chest;

        public Chest secondChest;

        @ConstructorProperties({"sf", "time", "hologram", "chest", "secondChest"})
        public Timebomb(boolean sf, long time, Hologram hologram, Chest chest, Chest secondChest) {
            this.sf = sf;
            this.time = time;
            this.hologram = hologram;
            this.chest = chest;
            this.secondChest = secondChest;
        }
    }
}
