package rip.battle.entity.living;

import cc.stormworth.core.util.TaskUtil;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.server.v1_8_R3.MathHelper;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityTeleport;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityVelocity;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;
import rip.battle.entity.Entity;
import rip.battle.entity.EntityPlugin;
import rip.battle.entity.living.events.LivingEntityDeathEvent;
import rip.battle.entity.tick.TickableEntity;
import rip.battle.entity.utils.AngleUtils;
import rip.battle.entity.utils.PacketUtils;
import rip.battle.spigot.Battle;
import rip.battle.spigot.knockback.KnockbackProfile;

@Getter @Setter
public abstract class LivingEntity extends Entity implements TickableEntity {

    private double health = 20;
    private double maxHealth = 20;

    private boolean dead;

    private boolean invulnerable;
    private long lastDamage;

    private boolean gravity = true;

    private String customName;

    private Vector velocity;

    private boolean onGround;

    public LivingEntity(Location location) {
        super(location);
        velocity = new Vector();
    }

    @Override
    public void tick() {
        if (gravity){
            if (!getLocation().getBlock().getType().isSolid()) {

                System.out.println("Falling");
                setVelocity(getVelocity().clone().add(new Vector(0, -0.1, 0)));

                onGround = false;
            }else{
                System.out.println("On ground");
                onGround = true;
            }
        }
    }

    public void setVelocity(Vector vector) {
        this.velocity = vector;
        move(vector);

        getCurrentWatchersPlayers().forEach(currentWatchersPlayer -> sendVelocityPacket(currentWatchersPlayer, vector));
    }

    public void sendVelocityPacket(Player player, Vector vector) {
        PacketPlayOutEntityVelocity packet = new PacketPlayOutEntityVelocity(getId(), vector.getX(), vector.getY(), vector.getZ());
        PacketUtils.sendPacket(player, packet);
    }

    public void move(Vector vector) {
        Location to = getLocation().clone().add(vector);

        if (to.getBlock().getRelative(0, 1, 0).getType().isSolid()) {
            to.setY(to.getBlockY() + 1);
        }

        setLocation(to);

        getCurrentWatchersPlayers().forEach(this::sendMovePacket);
    }

    public void sendMovePacket(Player player) {
        PacketPlayOutEntityTeleport teleportPacket = new PacketPlayOutEntityTeleport(
                getId(),
                MathHelper.floor(getLocation().getX() * 32.0D),
                MathHelper.floor(getLocation().getY() * 32.0D),
                MathHelper.floor(getLocation().getZ() * 32.0D),
                AngleUtils.yawToBytes(getLocation().getYaw()),
                AngleUtils.yawToBytes(getLocation().getPitch()),
                true);

        PacketUtils.sendPacket(player, teleportPacket);

        velocity = new Vector();
    }

    @Override
    public void onLeftClick(Player player) {

        ItemStack hand = player.getItemInHand();

        double damage = 1;

        switch (hand.getType()){
            case WOOD_SWORD:
            case STONE_SWORD:
                damage = 3.0;
                break;
            case IRON_SWORD:
                damage = 4.0;
                break;
            case GOLD_SWORD:
                damage = 5.0;
                break;
            case DIAMOND_SWORD:
                damage = 6.0;
                break;
        }

        //Change this xd
        double finalDamage = damage;
        TaskUtil.run(JavaPlugin.getPlugin(EntityPlugin.class), () -> {
            damage(player, finalDamage);
        });
    }

    public boolean damage(org.bukkit.entity.Entity entity, double damage){

        if (invulnerable){
            return false;
        }

        if (lastDamage + 500L > System.currentTimeMillis()){
            return false;
        }

        if (dead){
            return false;
        }

        health -= damage;

        if (health <= 0){
            onDeath(entity);
            sendStatusPacket((byte) 3);

            TaskUtil.runLater(JavaPlugin.getPlugin(EntityPlugin.class), () -> {
                if (dead){
                    destroyForCurrentWatchers();
                }
            }, 40);


            Bukkit.getPluginManager().callEvent(new LivingEntityDeathEvent(this, entity));
            return true;
        }

        lastDamage = System.currentTimeMillis();

        sendAnimationPacket(1);

        KnockbackProfile profile = Battle.INSTANCE.getConfig().getCurrentKb();

        setVelocity(new Vector(
                (-MathHelper.sin(entity.getLocation().getYaw() * 3.1415927F / 180.0F) * profile.getHorizontal()),
                profile.getVertical(),
                (MathHelper.cos(entity.getLocation().getYaw() * 3.1415927F / 180.0F) * profile.getHorizontal())
        ));
        return true;
    }

    public void onDeath(org.bukkit.entity.Entity entity){
        dead = true;
    }

    public void setCustomName(String customName) {
        this.customName = customName;

        updateForCurrentWatchers();
    }

    public boolean isCollideWithBlock(Location to){
        return to.getBlock().getType().isSolid();
    }
}
