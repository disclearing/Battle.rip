package net.minecraft.server;

import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class PathfinderGoalBreed extends PathfinderGoal {

    private EntityAnimal d;
    World a;
    private EntityAnimal e;
    int b;
    double c;

    public PathfinderGoalBreed(EntityAnimal entityanimal, double d0) {
        this.d = entityanimal;
        this.a = entityanimal.world;
        this.c = d0;
        this.a(3);
    }

    public boolean a() {
        if (!this.d.ce()) {
            return false;
        } else {
            this.e = this.f();
            return this.e != null;
        }
    }

    public boolean b() {
        return this.e.isAlive() && this.e.ce() && this.b < 60;
    }

    public void d() {
        this.e = null;
        this.b = 0;
    }

    public void e() {
        this.d.getControllerLook().a(this.e, 10.0F, (float) this.d.x());
        this.d.getNavigation().a((Entity) this.e, this.c);
        ++this.b;
        if (this.b >= 60 && this.d.f(this.e) < 9.0D) {
            this.g();
        }
    }

    private EntityAnimal f() {
        float f = 8.0F;
        List list = this.a.a(this.d.getClass(), this.d.boundingBox.grow((double) f, (double) f, (double) f));
        double d0 = Double.MAX_VALUE;
        EntityAnimal entityanimal = null;
        Iterator iterator = list.iterator();

        while (iterator.hasNext()) {
            EntityAnimal entityanimal1 = (EntityAnimal) iterator.next();

            if (this.d.mate(entityanimal1) && this.d.f(entityanimal1) < d0) {
                entityanimal = entityanimal1;
                d0 = this.d.f(entityanimal1);
            }
        }

        return entityanimal;
    }

    private void g() {
        EntityAgeable entityageable = this.d.createChild(this.e);

        if (entityageable != null) {
            // CraftBukkit start - set persistence for tame animals
            if (entityageable instanceof EntityTameableAnimal && ((EntityTameableAnimal) entityageable).isTamed()) {
                entityageable.persistent = true;
            }
            // CraftBukkit end
            EntityHuman entityhuman = this.d.cd();

            if (entityhuman == null && this.e.cd() != null) {
                entityhuman = this.e.cd();
            }

            if (entityhuman != null) {
                entityhuman.a(StatisticList.x);
                if (this.d instanceof EntityCow) {
                    entityhuman.a((Statistic) AchievementList.H);
                }
            }

            this.d.setAge(6000);
            this.e.setAge(6000);
            this.d.cf();
            this.e.cf();
            entityageable.setAge(-24000);
            entityageable.setPositionRotation(this.d.locX, this.d.locY, this.d.locZ, 0.0F, 0.0F);
            this.a.addEntity(entityageable, org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason.BREEDING); // CraftBukkit - added SpawnReason
            Random random = this.d.aI();

            for (int i = 0; i < 7; ++i) {
                double d0 = random.nextGaussian() * 0.02D;
                double d1 = random.nextGaussian() * 0.02D;
                double d2 = random.nextGaussian() * 0.02D;
                this.a.addParticle("heart", this.d.locX + (double) (random.nextFloat() * this.d.width * 2.0F) - (double) this.d.width, this.d.locY + 0.5D + (double) (random.nextFloat() * this.d.length), this.d.locZ + (double) (random.nextFloat() * this.d.width * 2.0F) - (double) this.d.width, d0, d1, d2);
            }

            if (this.a.getGameRules().getBoolean("doMobLoot")) {
                this.a.addEntity(new EntityExperienceOrb(this.a, this.d.locX, this.d.locY, this.d.locZ, random.nextInt(7) + 1));
            }
        }
    }
}
