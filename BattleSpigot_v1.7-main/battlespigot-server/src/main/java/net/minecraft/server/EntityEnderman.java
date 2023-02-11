package net.minecraft.server;

import org.apache.commons.math3.util.FastMath;
import org.spigotmc.ProtocolData;

import java.util.UUID;

public class EntityEnderman extends EntityMonster {

    private static final UUID bp = UUID.fromString("020E0DFB-87AE-4653-9556-831010E291A0");
    private static final AttributeModifier bq = (new AttributeModifier(bp, "Attacking speed boost", 6.199999809265137D, 0)).a(false);
    //private static boolean[] br = new boolean[256];
    private int bs;
    private int bt;
    private Entity bu;
    private boolean bv;

    public EntityEnderman(World world) {
        super(world);
        this.a(0.6F, 2.9F);
        this.W = 1.0F;
    }

    @Override
    public void h() {
        super.h();
        if (!this.world.isStatic && !this.world.spigotConfig.mobsEnabled) {
            this.die();
        }
    }

    protected void aD() {
        super.aD();
        this.getAttributeInstance(GenericAttributes.maxHealth).setValue(40.0D);
        this.getAttributeInstance(GenericAttributes.d).setValue(0.30000001192092896D);
        this.getAttributeInstance(GenericAttributes.e).setValue(7.0D);
    }

    protected void c() {
        super.c();
        this.datawatcher.a( 16, new ProtocolData.ByteShort( (short) 0 ) ); // Spigot - protocol patch, handle metadata change
        this.datawatcher.a(17, new Byte((byte) 0));
        this.datawatcher.a(18, new Byte((byte) 0));
    }

    /*public void b(NBTTagCompound nbttagcompound) {
        super.b(nbttagcompound);
        nbttagcompound.setShort("carried", (short) Block.getId(this.getCarried()));
        nbttagcompound.setShort("carriedData", (short) this.getCarriedData());
    }

    public void a(NBTTagCompound nbttagcompound) {
        super.a(nbttagcompound);
        this.setCarried(Block.getById(nbttagcompound.getShort("carried")));
        this.setCarriedData(nbttagcompound.getShort("carriedData"));
    }*/

    protected Entity findTarget() {
        EntityHuman entityhuman = this.world.findNearbyVulnerablePlayer(this, 16.0D);

        if (entityhuman != null) {
            if (this.f(entityhuman)) {
                this.bv = true;
                if (this.bt == 0) {
                    this.world.makeSound(entityhuman.locX, entityhuman.locY, entityhuman.locZ, "mob.endermen.stare", 1.0F, 1.0F);
                }

                if (this.bt++ == 5) {
                    this.bt = 0;
                    this.a(true);
                    return entityhuman;
                }
            } else {
                this.bt = 0;
            }
        }

        return null;
    }

    private boolean f(EntityHuman entityhuman) {
        ItemStack itemstack = entityhuman.inventory.armor[3];

        if (itemstack != null && itemstack.getItem() == Item.getItemOf(Blocks.PUMPKIN)) {
            return false;
        } else {
            Vec3D vec3d = entityhuman.j(1.0F).a();
            Vec3D vec3d1 = Vec3D.a(this.locX - entityhuman.locX, this.boundingBox.b + (double) (this.length / 2.0F) - (entityhuman.locY + (double) entityhuman.getHeadHeight()), this.locZ - entityhuman.locZ);
            double d0 = vec3d1.b();

            vec3d1 = vec3d1.a();
            double d1 = vec3d.b(vec3d1);

            return d1 > 1.0D - 0.025D / d0 && entityhuman.hasLineOfSight(this);
        }
    }

    public void e() {
        if (this.L()) {
            this.damageEntity(DamageSource.DROWN, 1.0F);
        }

        if (this.bu != this.target) {
            AttributeInstance attributeinstance = this.getAttributeInstance(GenericAttributes.d);

            attributeinstance.b(bq);
            if (this.target != null) {
                attributeinstance.a(bq);
            }
        }

        this.bu = this.target;
        int i;

        if (this.L() || this.isBurning()) {
            this.target = null;
            this.a(false);
            this.bv = false;
            this.bZ();
        }

        if (this.cd() && !this.bv && this.random.nextInt(100) == 0) {
            this.a(false);
        }

        this.bc = false;
        if (this.target != null) {
            this.a(this.target, 100.0F, 100.0F);
        }

        if (!this.world.isStatic && this.isAlive()) {
            if (this.target != null) {
                if (this.target instanceof EntityHuman && this.f((EntityHuman) this.target)) {
                    if (this.target.f((Entity) this) < 16.0D) {
                        this.bZ();
                    }

                    this.bs = 0;
                } else if (this.target.f((Entity) this) > 256.0D && this.bs++ >= 30 && this.c(this.target)) {
                    this.bs = 0;
                }
            } else {
                this.a(false);
                this.bs = 0;
            }
        }

        super.e();
    }

    protected boolean bZ() {
        double d0 = this.locX + (this.random.nextDouble() - 0.5D) * 64.0D;
        double d1 = this.locY + (double) (this.random.nextInt(64) - 32);
        double d2 = this.locZ + (this.random.nextDouble() - 0.5D) * 64.0D;

        return this.k(d0, d1, d2);
    }

    protected boolean c(Entity entity) {
        Vec3D vec3d = Vec3D.a(this.locX - entity.locX, this.boundingBox.b + (double) (this.length / 2.0F) - entity.locY + (double) entity.getHeadHeight(), this.locZ - entity.locZ);

        vec3d = vec3d.a();
        double d0 = 16.0D;
        double d1 = this.locX + (this.random.nextDouble() - 0.5D) * 8.0D - vec3d.a * d0;
        double d2 = this.locY + (double) (this.random.nextInt(16) - 8) - vec3d.b * d0;
        double d3 = this.locZ + (this.random.nextDouble() - 0.5D) * 8.0D - vec3d.c * d0;

        return this.k(d1, d2, d3);
    }

    protected boolean k(double d0, double d1, double d2) {
        double d3 = this.locX;
        double d4 = this.locY;
        double d5 = this.locZ;

        this.locX = d0;
        this.locY = d1;
        this.locZ = d2;
        boolean flag = false;
        int i = (int) FastMath.floor(this.locX);
        int j = (int) FastMath.floor(this.locY);
        int k = (int) FastMath.floor(this.locZ);

        if (this.world.isLoaded(i, j, k)) {
            boolean flag1 = false;

            while (!flag1 && j > 0) {
                Block block = this.world.getType(i, j - 1, k);

                if (block.getMaterial().isSolid()) {
                    flag1 = true;
                } else {
                    --this.locY;
                    --j;
                }
            }
        }

        return true;
    }

    protected String t() {
        return this.cd() ? "mob.endermen.scream" : "mob.endermen.idle";
    }

    protected String aT() {
        return "mob.endermen.hit";
    }

    protected String aU() {
        return "mob.endermen.death";
    }

    protected Item getLoot() {
        return Items.ENDER_PEARL;
    }

    protected void dropDeathLoot(boolean flag, int i) {
        Item item = this.getLoot();

        if (item != null) {
            int j = this.random.nextInt(2 + i);

            for (int k = 0; k < j; ++k) {
                this.a(item, 1);
            }
        }
    }

    public void setCarried(Block block) {
        this.datawatcher.watch( 16, new ProtocolData.ByteShort( (short) Block.getId( block ) ) ); // Spigot - protocol patch, handle metadata change
    }

    public Block getCarried() {
        return Block.getById(this.datawatcher.getShort( 16 )); // Spigot - protocol patch, handle metadata change
    }

    public void setCarriedData(int i) {
        this.datawatcher.watch(17, Byte.valueOf((byte) (i & 255)));
    }

    public int getCarriedData() {
        return this.datawatcher.getByte(17);
    }

    public boolean damageEntity(DamageSource damagesource, float f) {
        if (this.isInvulnerable()) {
            return false;
        } else {
            this.a(true);
            if (damagesource instanceof EntityDamageSource && damagesource.getEntity() instanceof EntityHuman) {
                this.bv = true;
            }

            if (damagesource instanceof EntityDamageSourceIndirect) {
                this.bv = false;

                for (int i = 0; i < 64; ++i) {
                    if (this.bZ()) {
                        return true;
                    }
                }

                return false;
            } else {
                return super.damageEntity(damagesource, f);
            }
        }
    }

    public boolean cd() {
        return this.datawatcher.getByte(18) > 0;
    }

    public void a(boolean flag) {
        this.datawatcher.watch(18, Byte.valueOf((byte) (flag ? 1 : 0)));
    }

    /*static {
        br[Block.getId(Blocks.GRASS)] = true;
        br[Block.getId(Blocks.DIRT)] = true;
        br[Block.getId(Blocks.SAND)] = true;
        br[Block.getId(Blocks.GRAVEL)] = true;
        br[Block.getId(Blocks.YELLOW_FLOWER)] = true;
        br[Block.getId(Blocks.RED_ROSE)] = true;
        br[Block.getId(Blocks.BROWN_MUSHROOM)] = true;
        br[Block.getId(Blocks.RED_MUSHROOM)] = true;
        br[Block.getId(Blocks.TNT)] = true;
        br[Block.getId(Blocks.CACTUS)] = true;
        br[Block.getId(Blocks.CLAY)] = true;
        br[Block.getId(Blocks.PUMPKIN)] = true;
        br[Block.getId(Blocks.MELON)] = true;
        br[Block.getId(Blocks.MYCEL)] = true;
    }*/
}