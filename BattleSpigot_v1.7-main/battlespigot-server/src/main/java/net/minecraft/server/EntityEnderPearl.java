package net.minecraft.server;

import com.google.common.collect.Sets;
import net.minecraft.server.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.craftbukkit.event.CraftEventFactory;
import org.bukkit.craftbukkit.util.CraftMagicNumbers;
import org.bukkit.event.player.PlayerPearlRefundEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.material.Gate;
import org.bukkit.util.BlockIterator;
import org.bukkit.util.Vector;

import java.util.List;
import java.util.Set;

public class EntityEnderPearl extends EntityProjectile {

    private Location lastValidTeleport;

    private Float angle;
    private EntityLiving c;

    public EntityEnderPearl(World world) {
        super(world);
        this.loadChunks = world.paperSpigotConfig.loadUnloadedEnderPearls; // PaperSpigot
    }

    public EntityEnderPearl(World world, EntityLiving entityliving) {
        super(world, entityliving);
        this.c = entityliving;
        this.angle = entityliving.pitch;
        this.loadChunks = world.paperSpigotConfig.loadUnloadedEnderPearls; // PaperSpigot
        //this.setSize(0.175F,0.175F);
    }

    protected void a(MovingObjectPosition position) {
        if (this.world.getType(position.b, position.c, position.d) != null) {
            Block blockAt = this.world.getType(position.b, position.c, position.d);


            if (blockAt == Blocks.FENCE_GATE) {
                BlockIterator iterator = null;

                try {
                    Vector l = new Vector(this.locX, this.locY, this.locZ);
                    Vector l2 = new Vector(this.locX + this.motX, this.locY + this.motY, this.locZ + this.motZ);
                    Vector dir = new Vector(l2.getX() - l.getX(), l2.getY() - l.getY(), l2.getZ() - l.getZ()).normalize();
                    iterator = new BlockIterator(this.world.getWorld(), l, dir, 0, 1);
                } catch (IllegalStateException ex) {
                    // ignore
                }

                if (iterator != null) {
                    boolean open = true;
                    boolean hasSolidBlock = false;

                    while (iterator.hasNext()) {

                        org.bukkit.block.Block bukkitBlock = iterator.next();
                        Block block = CraftMagicNumbers.getBlock(iterator.next());

                        if (!(bukkitBlock.getState().getData() instanceof Gate) && block.getMaterial().isSolid()) {
                            hasSolidBlock = true;
                        }

                        if (bukkitBlock.getState().getData() instanceof Gate && !((Gate)bukkitBlock.getState().getData()).isOpen()) {
                            open = false;
                            break;
                        }

                    }

                    if (open && !hasSolidBlock) {
                        return;
                    }
                }
            }
        }

        final EntityLiving entityLiving = this.getShooter();

        if (position.entity != null) {

            if (position.entity == this.c) {
                return;
            }

            org.bukkit.block.Block bukkitBlock = this.world.getWorld().getBlockAt(position.b, position.c, position.d);

            if (bukkitBlock.getState().getData() instanceof Gate && ((Gate)bukkitBlock.getState().getData()).isOpen()) {
                this.lastValidTeleport = position.entity.getBukkitEntity().getLocation();
            } else {

                if (this.yaw < (DOWN_GATE_ANGLE + 1)) {
                    position.entity.damageEntity(DamageSource.projectile(this,entityLiving),0.0F);

                    BlockIterator iterator = null;

                    try {
                        Vector l = new Vector(this.locX, this.locY, this.locZ);
                        Vector l2 = new Vector(this.locX + this.motX, this.locY + this.motY, this.locZ + this.motZ);
                        Vector dir = new Vector(l2.getX() - l.getX(), l2.getY() - l.getY(), l2.getZ() - l.getZ()).normalize();
                        iterator = new BlockIterator(this.world.getWorld(), l, dir, 0, 1);
                    } catch (IllegalStateException ex) {
                        // ignore
                    }

                    if (iterator != null) {

                        boolean hasSolidBlock = false;

                        while (iterator.hasNext()) {

                            Block block = CraftMagicNumbers.getBlock(iterator.next());

                            if (block.getMaterial().isSolid() && !PASS_THROUGH_BLOCKS.contains(block)) {
                                hasSolidBlock = true;
                                break;
                            }

                        }

                        if (!hasSolidBlock) {
                            this.lastValidTeleport = position.entity.getBukkitEntity().getLocation();
                        }

                    }
                }

            }

            position.entity.damageEntity(DamageSource.projectile(this,entityLiving),0.0F);
        }

        // PaperSpigot start - Remove entities in unloaded chunks
        if (this.inUnloadedChunk && this.world.paperSpigotConfig.removeUnloadedEnderPearls) {
            die();
        }
        // PaperSpigot end

        for (int i = 0; i < 32; ++i) {
            this.world.addParticle("portal", this.locX, this.locY + this.random.nextDouble() * 2.0D, this.locZ, this.random.nextGaussian(), 0.0D, this.random.nextGaussian());
        }

        final EntityLiving entityliving = this.getShooter();

        if (!this.world.isStatic) {

            if (entityliving instanceof EntityPlayer) {

                final Location location = this.lastValidTeleport;
                final EntityPlayer entityplayer = (EntityPlayer) entityliving;

                if (entityplayer.playerConnection.b().isConnected() && entityplayer.world == this.world && !entityplayer.isSleeping()) {
                    // CraftBukkit start - Fire PlayerTeleportEvent

                    if (this.lastValidTeleport != null) {
                        final CraftPlayer player = entityplayer.getBukkitEntity();

                        location.setPitch(player.getLocation().getPitch());
                        location.setYaw(player.getLocation().getYaw());

                        final PlayerTeleportEvent event = new PlayerTeleportEvent(
                                player,
                                player.getLocation(),
                                location,
                                PlayerTeleportEvent.TeleportCause.ENDER_PEARL
                        );

                        Bukkit.getPluginManager().callEvent(event);

                        if (!event.isCancelled() && !entityplayer.playerConnection.isDisconnected()) {

                            /*
                            if (this.random.nextFloat() < 0.05F && this.world.getGameRules().getBoolean("doMobSpawning")) {
                                EntityEndermite entityendermite = new EntityEndermite(this.world);

                                entityendermite.a(true);
                                entityendermite.setPositionRotation(entityliving.locX, entityliving.locY, entityliving.locZ, entityliving.yaw, entityliving.pitch);
                                this.world.addEntity(entityendermite);
                            }*/

                            if (entityliving.am()) {
                                entityliving.mount(null);
                            }

                            entityplayer.yaw = event.getTo().getYaw();
                            entityplayer.pitch = event.getTo().getPitch();
                            entityplayer.enderTeleportTo(event.getTo().getX(),event.getTo().getY(),event.getTo().getZ());
                            //entityplayer.playerConnection.teleport(event.getTo());
                            entityliving.fallDistance = 0.0F;

                            CraftEventFactory.entityDamage = this;
                            entityliving.damageEntity(DamageSource.FALL, 5.0F);
                            CraftEventFactory.entityDamage = null;
                        }
                        // CraftBukkit end
                    } else {
                        Bukkit.getPluginManager().callEvent(new PlayerPearlRefundEvent(entityplayer.getBukkitEntity()));
                    }

                }
            } else if (entityliving != null) {
                entityliving.enderTeleportTo(this.locX, this.locY, this.locZ);
                entityliving.fallDistance = 0.0F;
            }

            this.die();
        }
    }

    public void h() {
        EntityLiving entityliving = this.getShooter();

        if (this.ticksLived > 320) {
            this.die();
            return;
        }

        if (entityliving instanceof EntityHuman && !entityliving.isAlive()) {
            this.die();
        } else {

            org.bukkit.block.Block bukkitBlock = this.world.getWorld().getBlockAt(
                    MathHelper.floor(this.locX),
                    MathHelper.floor(this.locY),
                    MathHelper.floor(this.locZ));
            
            Block block = CraftMagicNumbers.getBlock(bukkitBlock);

            if (this.angle >= DOWN_GATE_ANGLE && bukkitBlock.getState().getData() instanceof Gate && ((Gate)bukkitBlock.getState().getData()).isOpen()) {

                final Location location = new Location(this.world.getWorld(),this.locX,this.locY,this.locZ);

                if (this.shooter != null) {
                    location.setYaw(this.shooter.yaw);
                    location.setPitch(this.shooter.pitch);
                }

                this.lastValidTeleport = location;
            } if (PASS_THROUGH_BLOCKS.contains(block)) {
                this.lastValidTeleport = this.getBukkitEntity().getLocation();
            } else {

                double boundingY = BOUNDING_BOX_Y;
                double boundingXZ = BOUNDING_BOX_X_Z;

                final AxisAlignedBB box = AxisAlignedBB.a(
                        this.locX - boundingXZ,
                        this.locY - 0.05D,
                        this.locZ - boundingXZ,
                        this.locX + boundingXZ,
                        this.locY + boundingY,
                        this.locZ + boundingXZ
                );

                final List<AxisAlignedBB> cubes = this.world.getCubes(this, box, PASS_THROUGH_BLOCKS);

                final boolean valid = cubes.isEmpty();
                final boolean prohibited = this.world.boundingBoxContainsMaterials(this.getBoundingBox().grow(0.25D, 0D, 0.25D),PROHIBITED_PEARL_BLOCKS);

                if (!prohibited && valid) {
                    this.lastValidTeleport = this.getBukkitEntity().getLocation();
                }
            }

        }

        super.h();
    }

    private static final float DOWN_GATE_ANGLE = 79;

    private static final Set<Block> PASS_THROUGH_BLOCKS = Sets.newHashSet(
            Blocks.WEB,
            Blocks.TRIPWIRE,
            //Blocks.TRIPWIRE_HOOK,

            Blocks.CHEST,
            Blocks.TRAPPED_CHEST,

           /* Blocks.STONE_SLAB,
            Blocks.STONE_SLAB2,
            Blocks.WOODEN_SLAB,*/

            Blocks.TORCH,

            Blocks.LEVER,
            //Blocks.TRAPDOOR,
            Blocks.PISTON_EXTENSION,

            Blocks.WOOD_STAIRS,
            Blocks.BRICK_STAIRS,
            Blocks.STONE_STAIRS,
            Blocks.QUARTZ_STAIRS,
            Blocks.ACACIA_STAIRS,
            Blocks.DARK_OAK_STAIRS,
            Blocks.SANDSTONE_STAIRS,
            Blocks.NETHER_BRICK_STAIRS,

            Blocks.BED,
            Blocks.ENCHANTMENT_TABLE,
            Blocks.SNOW,
            Blocks.FENCE,
            Blocks.NETHER_BRICK_STAIRS,
            //Blocks.IRON_FENCE,
            Blocks.TRAP_DOOR,

            Blocks.WALL_SIGN,
            Blocks.SIGN_POST,
            Blocks.COBBLE_WALL,

            Blocks.BREWING_STAND,
            Blocks.ENDER_PORTAL_FRAME
    );

    private static final Set<Block> PROHIBITED_PEARL_BLOCKS = Sets.newHashSet(
            Blocks.FENCE,
            Blocks.NETHER_FENCE
    );

    //public static double BOUNDING_BOX_X_Z = 0.125D;
    public static double BOUNDING_BOX_Y = 0.5D;
    public static double BOUNDING_BOX_X_Z = 0.3D;
}
