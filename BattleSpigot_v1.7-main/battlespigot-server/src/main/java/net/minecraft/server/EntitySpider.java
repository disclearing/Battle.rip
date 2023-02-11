package net.minecraft.server;

public class EntitySpider extends EntityMonster {

    public EntitySpider(World world) {
        super(world);
        this.a(1.4F, 0.9F);
    }

    protected void c() {
        super.c();
        this.datawatcher.a(16, new Byte((byte) 0));
    }

    public void h() {
        super.h();
        if (!this.world.isStatic) {
            // MineHQ - Add mobsEnabled check.
            if (!this.world.spigotConfig.mobsEnabled) {
                this.die();
            }

            this.a(this.positionChanged);
        }
    }

    protected void aD() {
        super.aD();
        this.getAttributeInstance(GenericAttributes.maxHealth).setValue(16.0D);
        this.getAttributeInstance(GenericAttributes.d).setValue(0.800000011920929D);
    }

    protected String t() {
        return "mob.spider.say";
    }

    protected String aT() {
        return "mob.spider.say";
    }

    protected String aU() {
        return "mob.spider.death";
    }

    protected void a(int i, int j, int k, Block block) {
        this.makeSound("mob.spider.step", 0.15F, 1.0F);
    }

    protected Item getLoot() {
        return Items.STRING;
    }

    protected void dropDeathLoot(boolean flag, int i) {
        super.dropDeathLoot(flag, i);
        if (flag && (this.random.nextInt(3) == 0 || this.random.nextInt(1 + i) > 0)) {
            this.a(Items.SPIDER_EYE, 1);
        }
    }

    public boolean h_() {
        return this.bZ();
    }

    public void as() {}

    public EnumMonsterType getMonsterType() {
        return EnumMonsterType.ARTHROPOD;
    }

    public boolean d(MobEffect mobeffect) {
        return mobeffect.getEffectId() == MobEffectList.POISON.id ? false : super.d(mobeffect);
    }

    public boolean bZ() {
        return (this.datawatcher.getByte(16) & 1) != 0;
    }

    public void a(boolean flag) {
        byte b0 = this.datawatcher.getByte(16);

        if (flag) {
            b0 = (byte) (b0 | 1);
        } else {
            b0 &= -2;
        }

        this.datawatcher.watch(16, Byte.valueOf(b0));
    }

    public GroupDataEntity prepare(GroupDataEntity groupdataentity) {
        Object object = super.prepare(groupdataentity);

        if (this.world.random.nextInt(100) == 0) {
            EntitySkeleton entityskeleton = new EntitySkeleton(this.world);

            entityskeleton.setPositionRotation(this.locX, this.locY, this.locZ, this.yaw, 0.0F);
            entityskeleton.prepare((GroupDataEntity) null);
            this.world.addEntity(entityskeleton, org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason.JOCKEY); // CraftBukkit - add SpawnReason
            entityskeleton.mount(this);
        }

        if (object == null) {
            object = new GroupDataSpider();
            if (this.world.difficulty == EnumDifficulty.HARD && this.world.random.nextFloat() < 0.1F * this.world.b(this.locX, this.locY, this.locZ)) {
                ((GroupDataSpider) object).a(this.world.random);
            }
        }

        if (object instanceof GroupDataSpider) {
            int i = ((GroupDataSpider) object).a;

            if (i > 0 && MobEffectList.byId[i] != null) {
                this.addEffect(new MobEffect(i, Integer.MAX_VALUE));
            }
        }

        return (GroupDataEntity) object;
    }
}