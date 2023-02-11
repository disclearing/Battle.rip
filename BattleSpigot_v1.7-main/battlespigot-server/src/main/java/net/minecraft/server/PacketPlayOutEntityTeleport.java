package net.minecraft.server;

import org.apache.commons.math3.util.FastMath;

public class PacketPlayOutEntityTeleport extends Packet {

    public int a;
    public int b;
    public int c;
    public int d;
    public byte e;
    public byte f;
    public boolean onGround; // Spigot - protocol patch
    public boolean heightCorrection; // Spigot Update - 20140916a

    public PacketPlayOutEntityTeleport() {}

    public PacketPlayOutEntityTeleport(Entity entity) {
        this.a = entity.getId();
        this.b = (int) FastMath.floor(entity.locX * 32.0D);
        this.c = (int) FastMath.floor(entity.locY * 32.0D);
        this.d = (int) FastMath.floor(entity.locZ * 32.0D);
        this.e = (byte) ((int) (entity.yaw * 256.0F / 360.0F));
        this.f = (byte) ((int) (entity.pitch * 256.0F / 360.0F));
    }

    public PacketPlayOutEntityTeleport(int i, int j, int k, int l, byte b0, byte b1, boolean onGround, boolean heightCorrection) { // Spigot - protocol patch  // Spigot Update - 20140916a
        this.a = i;
        this.b = j;
        this.c = k;
        this.d = l;
        this.e = b0;
        this.f = b1;
        this.onGround = onGround; // Spigot - protocol patch
        this.heightCorrection = heightCorrection;  // Spigot Update - 20140916a
    }

    /**
     * PaperSpigot - Backwards compatible PacketPlayOutEntityTeleport contructor
     */
    public PacketPlayOutEntityTeleport(int i, int j, int k, int l, byte b0, byte b1, boolean onGround) {
        this(i, j, k, l, b0, b1, onGround, false);
    }

    /**
     * PaperSpigot - Backwards compatible PacketPlayOutEntityTeleport contructor
     */
    public PacketPlayOutEntityTeleport(int i, int j, int k, int l, byte b0, byte b1) {
        this(i, j, k, l, b0, b1, false, false);
    }

    public void a(PacketDataSerializer packetdataserializer) {
        this.a = packetdataserializer.readInt();
        this.b = packetdataserializer.readInt();
        this.c = packetdataserializer.readInt();
        this.d = packetdataserializer.readInt();
        this.e = packetdataserializer.readByte();
        this.f = packetdataserializer.readByte();
    }

    public void b(PacketDataSerializer packetdataserializer) {
        // Spigot start - protocol
        if (packetdataserializer.version < 16 )
        {
            packetdataserializer.writeInt( this.a );
        } else
        {
            packetdataserializer.b( a );
        }
        // Spigot end
        packetdataserializer.writeInt(this.b);
        packetdataserializer.writeInt((packetdataserializer.version >= 16 && heightCorrection) ? (this.c - 16) : this.c); // Spigot Update - 20140916a
        packetdataserializer.writeInt(this.d);
        packetdataserializer.writeByte(this.e);
        packetdataserializer.writeByte(this.f);
        // Spigot start - protocol patch
        if (packetdataserializer.version >= 22 )
        {
            packetdataserializer.writeBoolean( onGround );
        }
        // Spigot end
    }

    public void a(PacketPlayOutListener packetplayoutlistener) {
        packetplayoutlistener.a(this);
    }

    public int getA() {
        return this.a;
    }

    public int getB() {
        return this.b;
    }

    public int getC() {
        return this.c;
    }

    public int getD() {
        return this.d;
    }

    public byte getE() {
        return this.e;
    }

    public byte getF() {
        return this.f;
    }

    public void setB(int b) {
        this.b = b;
    }

    public void setC(int c) {
        this.c = c;
    }

    public void setD(int d) {
        this.d = d;
    }

    public void handle(PacketListener packetlistener) {
        this.a((PacketPlayOutListener) packetlistener);
    }
}
