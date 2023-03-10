package net.minecraft.server;

import org.apache.commons.math3.util.FastMath;
import org.bukkit.event.entity.ExplosionPrimeEvent;

public class EntityWitherSkull extends EntityFireball {

    public EntityWitherSkull(World world) {
        super(world);
        this.a(0.3125F, 0.3125F);
    }

    public EntityWitherSkull(World world, EntityLiving entityliving, double d0, double d1, double d2) {
        super(world, entityliving, d0, d1, d2);
        this.a(0.3125F, 0.3125F);
    }

    protected float e() {
        return this.isCharged() ? 0.73F : super.e();
    }

    public boolean isBurning() {
        return false;
    }

    public float a(Explosion explosion, World world, int i, int j, int k, Block block) {
        float f = super.a(explosion, world, i, j, k, block);

        if (this.isCharged() && block != Blocks.BEDROCK && block != Blocks.ENDER_PORTAL && block != Blocks.ENDER_PORTAL_FRAME && block != Blocks.COMMAND) {
            f = FastMath.min(0.8F, f);
        }

        return f;
    }

    protected void a(MovingObjectPosition movingobjectposition) {
        if (!this.world.isStatic) {
            if (movingobjectposition.entity != null) {
                // Spigot start
                boolean didDamage = false;         	
                if (this.shooter != null) {
                    didDamage = movingobjectposition.entity.damageEntity(DamageSource.mobAttack(this.shooter), 8.0F);
                    if (didDamage && !movingobjectposition.entity.isAlive()) {
                        this.shooter.heal(5.0F, org.bukkit.event.entity.EntityRegainHealthEvent.RegainReason.WITHER); // CraftBukkit
                    }
                } else {
                    didDamage = movingobjectposition.entity.damageEntity(DamageSource.MAGIC, 5.0F);
                }

                if (didDamage && movingobjectposition.entity instanceof EntityLiving) {
                // Spigot end
                    byte b0 = 0;

                    if (this.world.difficulty == EnumDifficulty.NORMAL) {
                        b0 = 10;
                    } else if (this.world.difficulty == EnumDifficulty.HARD) {
                        b0 = 40;
                    }

                    if (b0 > 0) {
                        ((EntityLiving) movingobjectposition.entity).addEffect(new MobEffect(MobEffectList.WITHER.id, 20 * b0, 1));
                    }
                }
            }

            // CraftBukkit start
            ExplosionPrimeEvent event = new ExplosionPrimeEvent(this.getBukkitEntity(), 1.0F, false);
            this.world.getServer().getPluginManager().callEvent(event);

            if (!event.isCancelled()) {
                this.world.createExplosion(this, this.locX, this.locY, this.locZ, event.getRadius(), event.getFire(), this.world.getGameRules().getBoolean("mobGriefing"));
            }
            // CraftBukkit end

            this.die();
        }
    }

    public boolean R() {
        return false;
    }

    public boolean damageEntity(DamageSource damagesource, float f) {
        return false;
    }

    protected void c() {
        this.datawatcher.a(10, Byte.valueOf((byte) 0));
    }

    public boolean isCharged() {
        return this.datawatcher.getByte(10) == 1;
    }

    public void setCharged(boolean flag) {
        this.datawatcher.watch(10, Byte.valueOf((byte) (flag ? 1 : 0)));
    }
}
