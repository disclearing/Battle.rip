package net.minecraft.server;

public class PacketPlayOutEntity extends Packet {

    public int a;
    public byte b;
    public byte c;
    public byte d;
    public byte e;
    public byte f;
    public boolean g;

    public PacketPlayOutEntity() {}

    public PacketPlayOutEntity(int i) {
        this.a = i;
    }

    public void a(PacketDataSerializer packetdataserializer) {
        this.a = packetdataserializer.readInt();
    }

    public void b(PacketDataSerializer packetdataserializer) {
        // Spigot start - protocol patch
        if ( packetdataserializer.version < 16 )
        {
            packetdataserializer.writeInt( this.a );
        } else
        {
            packetdataserializer.b( a );
        }
        // Spigot end
    }

    public void a(PacketPlayOutListener packetplayoutlistener) {
        packetplayoutlistener.a(this);
    }

    public String b() {
        return String.format("id=%d", new Object[] { Integer.valueOf(this.a)});
    }

    public String toString() {
        return "Entity_" + super.toString();
    }

    public int getA() {
        return this.a;
    }

    public byte getB() {
        return this.b;
    }

    public byte getC() {
        return this.c;
    }

    public byte getD() {
        return this.d;
    }

    public byte getE() {
        return this.e;
    }

    public byte getF() {
        return this.f;
    }

    public boolean isG() {
        return this.g;
    }

    public void setB(byte b) {
        this.b = b;
    }

    public void setC(byte c) {
        this.c = c;
    }

    public void setD(byte d) {
        this.d = d;
    }

    public void handle(PacketListener packetlistener) {
        this.a((PacketPlayOutListener) packetlistener);
    }
}