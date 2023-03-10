package net.minecraft.server;

public class TileEntityEnderChest extends TileEntity {

    public float a;
    public float i;
    public int j;
    private int k;

    public TileEntityEnderChest() {
    }

    public void h() {
        super.h();
        if (++this.k % 4 == 0) { // PaperSpigot Reduced (20 * 4) -> 4 interval due to reduced tick rate from Improved Tick Handling
            this.world.playBlockAction(this.x, this.y, this.z, Blocks.ENDER_CHEST, 1, this.j);
        }

        this.i = this.a;

        // PaperSpigot start - Move chest sound handling out of the tick loop
        /*
        float f = 0.1F;
        double d0;

        if (this.j > 0 && this.a == 0.0F) {
            double d1 = (double) this.x + 0.5D;

            d0 = (double) this.z + 0.5D;
            this.world.makeSound(d1, (double) this.y + 0.5D, d0, "random.chestopen", 0.5F, this.world.random.nextFloat() * 0.1F + 0.9F);
        }

        if (this.j == 0 && this.a > 0.0F || this.j > 0 && this.a < 1.0F) {
            float f1 = this.a;

            if (this.j > 0) {
                this.a += f;
            } else {
                this.a -= f;
            }

            if (this.a > 1.0F) {
                this.a = 1.0F;
            }

            float f2 = 0.5F;

            if (this.a < f2 && f1 >= f2) {
                d0 = (double) this.x + 0.5D;
                double d2 = (double) this.z + 0.5D;

                this.world.makeSound(d0, (double) this.y + 0.5D, d2, "random.chestclosed", 0.5F, this.world.random.nextFloat() * 0.1F + 0.9F);
            }

            if (this.a < 0.0F) {
                this.a = 0.0F;
            }
        }
        */
        // PaperSpigot end
    }

    public boolean c(int i, int j) {
        if (i == 1) {
            this.j = j;
            return true;
        } else {
            return super.c(i, j);
        }
    }

    public void s() {
        this.u();
        super.s();
    }

    /*public void a() {
        ++this.j;
        this.world.playBlockAction(this.x, this.y, this.z, Blocks.ENDER_CHEST, 1, this.j);

        // PaperSpigot start - Move chest open sound handling down to here
        double d0;

        if (this.j > 0 && this.a == 0.0F) {
            double d1 = (double) this.x + 0.5D;

            d0 = (double) this.z + 0.5D;
            this.world.makeSound(d1, (double) this.y + 0.5D, d0, "random.chestopen", 0.5F, this.world.random.nextFloat() * 0.1F + 0.9F);
        }
        // PaperSpigot end
    }*/

    public void b() {
        --this.j;
        this.world.playBlockAction(this.x, this.y, this.z, Blocks.ENDER_CHEST, 1, this.j);

        // PaperSpigot start - Move chest close sound handling down to here
        //float f = 0.1F;
        double d0;

        /*if (this.j == 0 && this.a == 0.0F || this.j > 0 && this.a < 1.0F) {
            float f1 = this.a;
            d0 = (double) this.x + 0.5D;
            double d2 = (double) this.z + 0.5D;

            this.world.makeSound(d0, (double) this.y + 0.5D, d2, "random.chestclosed", 0.5F, this.world.random.nextFloat() * 0.1F + 0.9F);

            if (this.a < 0.0F) {
                this.a = 0.0F;
            }
        }*/
        // PaperSpigot end
    }

    public boolean a(EntityHuman entityhuman) {
        return this.world.getTileEntity(this.x, this.y, this.z) == this && entityhuman.e((double) this.x + 0.5D, (double) this.y + 0.5D, (double) this.z + 0.5D) <= 64.0D;
    }
}
