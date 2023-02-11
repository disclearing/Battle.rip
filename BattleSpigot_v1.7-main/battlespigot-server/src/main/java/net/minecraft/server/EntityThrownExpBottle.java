package net.minecraft.server;

import org.apache.commons.math3.util.FastMath;

public class EntityThrownExpBottle extends EntityProjectile {

    public EntityThrownExpBottle(World world) {
        super(world);
    }

    public EntityThrownExpBottle(World world, EntityLiving entityliving) {
        super(world, entityliving);
    }

    public EntityThrownExpBottle(World world, double d0, double d1, double d2) {
        super(world, d0, d1, d2);
    }

    protected float i() {
        return 0.07F;
    }

    protected float e() {
        return 0.7F;
    }

    protected float f() {
        return -20.0F;
    }

    protected void a(MovingObjectPosition movingobjectposition) {
        if (!this.world.isStatic) {
            // CraftBukkit - moved to after event
            // this.world.triggerEffect(2002, (int) FastMath.round(this.locX), (int) FastMath.round(this.locY), (int) FastMath.round(this.locZ), 0);
            int i = 3 + this.world.random.nextInt(5) + this.world.random.nextInt(5);

            // CraftBukkit start
            org.bukkit.event.entity.ExpBottleEvent event = org.bukkit.craftbukkit.event.CraftEventFactory.callExpBottleEvent(this, i);
            i = event.getExperience();
            if (event.getShowEffect()) {
                this.world.triggerEffect(2002, (int) FastMath.round(this.locX), (int) FastMath.round(this.locY), (int) FastMath.round(this.locZ), 0);
            }
            // CraftBukkit end

            while (i > 0) {
                int j = EntityExperienceOrb.getOrbValue(i);

                i -= j;
                this.world.addEntity(new EntityExperienceOrb(this.world, this.locX, this.locY, this.locZ, j));
            }

            this.die();
        }
    }
}
