package net.minecraft.server;

public class PacketPlayOutUpdateHealth extends Packet {

    public float a;
    public int b;
    public float c;

    public PacketPlayOutUpdateHealth() {}

    public PacketPlayOutUpdateHealth(float f, int i, float f1) {
        this.a = f;
        this.b = i;
        this.c = f1;
    }

    public void a(PacketDataSerializer packetdataserializer) {
        this.a = packetdataserializer.readFloat();
        this.b = packetdataserializer.readShort();
        this.c = packetdataserializer.readFloat();
    }

    public void b(PacketDataSerializer packetdataserializer) {
        packetdataserializer.writeFloat(this.a);
        // Spigot start - protocol patch
        if (packetdataserializer.version < 16 )
        {
            packetdataserializer.writeShort( this.b );
        } else
        {
            packetdataserializer.b( this.b );
        }
        // Spigot end
        packetdataserializer.writeFloat(this.c);
    }

    public void a(PacketPlayOutListener packetplayoutlistener) {
        packetplayoutlistener.a(this);
    }

    public void handle(PacketListener packetlistener) {
        this.a((PacketPlayOutListener) packetlistener);
    }
}
