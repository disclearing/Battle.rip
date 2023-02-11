package cc.stormworth.meetup.ability.impl;

import cc.stormworth.core.kt.util.ItemBuilder;
import cc.stormworth.core.util.chat.CC;
import cc.stormworth.core.util.time.TimeUtil;
import cc.stormworth.meetup.Meetup;
import cc.stormworth.meetup.ability.InteractAbility;
import cc.stormworth.meetup.managers.TeamManager;
import cc.stormworth.meetup.team.Team;
import com.google.common.collect.Lists;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.PigZombie;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;
import java.util.UUID;

public class MiniBarder extends InteractAbility {

    public MiniBarder() {
        super("MiniBarder",
                "&7Mini Barder",
                Lists.newArrayList(
                        "",
                        "&7Are you alone? or don't you have a good",
                        "&7 barder in your faction? Don't wait any longer,",
                        "&7summon a minibarder that will do everything a common",
                        "&7barder does, with an important advantage, if he dies, you won't lose DTR lol.",
                        ""
                ),
                new ItemBuilder(Material.MOB_SPAWNER)/*.spawnerType(EntityType.PIG_ZOMBIE)*/.build(), //FIX
                TimeUtil.parseTimeLong("2m"));

    }

    @Override
    public void onInteract(PlayerInteractEvent event) {

        Player player = event.getPlayer();
        PigZombie pigZombie = player.getWorld().spawn(player.getLocation(), PigZombie.class);

        pigZombie.removeMetadata("enableai", Meetup.getInstance());
        pigZombie.setMetadata("owner", new FixedMetadataValue(Meetup.getInstance(), player.getUniqueId()));
        pigZombie.setCustomName(CC.translate("&6&lMiniBarder"));
        pigZombie.setCanPickupItems(false);
        pigZombie.setBaby(true);

        pigZombie.getEquipment().setHelmet(new ItemBuilder(Material.GOLD_HELMET).enchant(
                Enchantment.DURABILITY, 1).build());
        pigZombie.getEquipment().setChestplate(new ItemBuilder(Material.GOLD_CHESTPLATE).enchant(
                Enchantment.DURABILITY, 1).build());
        pigZombie.getEquipment().setLeggings(new ItemBuilder(Material.GOLD_LEGGINGS).enchant(
                Enchantment.DURABILITY, 1).build());
        pigZombie.getEquipment().setBoots(new ItemBuilder(Material.GOLD_BOOTS).enchant(
                Enchantment.DURABILITY, 1).build());

        pigZombie.setCustomNameVisible(true);
        pigZombie.setAngry(false);

        new BukkitRunnable() {

            final Material[] bardItem = {
                    Material.BLAZE_POWDER, Material.SUGAR, Material.FEATHER,
                    Material.IRON_INGOT, Material.MAGMA_CREAM, Material.GHAST_TEAR
            };

            int count = 10;

            @Override
            public void run() {
                if (count <= 0 || pigZombie.isDead()) {
                    cancel();

                    if (!pigZombie.isDead()) pigZombie.remove();
                    return;
                }

                Material randomMaterial = bardItem[(int) (Math.random() * bardItem.length)];

                pigZombie.getEquipment().setItemInHand(new ItemStack(randomMaterial));

                Team team = TeamManager.getInstance().getTeam(player);

                if (team != null) {
                    pigZombie.getNearbyEntities(10, 50, 10)
                            .stream()
                            .filter(entity -> entity instanceof Player)
                            .map(entity -> (Player) entity)
                            .filter(target -> team.isMember(target.getUniqueId()))
                            .forEach(target -> getPotionEffects().forEach(effect -> {

                                target.addPotionEffect(effect, true);

                            }));
                } else {
                    getPotionEffects().forEach(effect -> player.addPotionEffect(effect, true));
                }

                count--;
            }
        }.runTaskTimer(Meetup.getInstance(), 0, 20L);

        super.onInteract(event);
    }


    @EventHandler
    public void onEntityDamage(EntityDamageByEntityEvent event) {
        if (!event.getEntity().hasMetadata("owner") && !(event.getEntity() instanceof PigZombie)) {
            return;
        }

        if (!(event.getDamager() instanceof Player)) {
            return;
        }

        Player owner = Meetup.getInstance().getServer().getPlayer(UUID.fromString(event.getEntity().getMetadata("owner").get(0).asString()));
        Player player = (Player) event.getDamager();

        if (owner.equals(player)) {
            event.setCancelled(true);
            return;
        }

        Team team = TeamManager.getInstance().getTeam(owner);

        if (team == null) {
            return;
        }

        if (team.isMember(player.getUniqueId())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        if (!event.getEntity().hasMetadata("owner") && !(event.getEntity() instanceof PigZombie)) {
            return;
        }

        event.getDrops().clear();
    }

    @Override
    public List<PotionEffect> getPotionEffects() {
        return Lists.newArrayList(
                new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 20 * 3, 1),
                new PotionEffect(PotionEffectType.SPEED, 20 * 3, 1),
                new PotionEffect(PotionEffectType.JUMP, 20 * 3, 1),
                new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 20 * 3, 1),
                new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 20 * 3, 1),
                new PotionEffect(PotionEffectType.REGENERATION, 20 * 3, 1)
        );
    }
}