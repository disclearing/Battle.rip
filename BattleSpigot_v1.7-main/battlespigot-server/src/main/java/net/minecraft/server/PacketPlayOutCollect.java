package net.minecraft.server;

public class PacketPlayOutCollect extends Packet {

    private int a;
    private int b;

    public PacketPlayOutCollect() {}

    public PacketPlayOutCollect(int i, int j) {
        this.a = i;
        this.b = j;
    }

    public void a(PacketDataSerializer packetdataserializer) {
        this.a = packetdataserializer.readInt();
        this.b = packetdataserializer.readInt();
    }

    public void b(PacketDataSerializer packetdataserializer) {
        // Spigot start - protocol patch
        if (packetdataserializer.version < 16 )
        {
            packetdataserializer.writeInt( this.a );
            packetdataserializer.writeInt( this.b );
        } else
        {
            packetdataserializer.b( this.a );
            packetdataserializer.b( this.b );
        }
        // Spigot end
    }

    public void a(PacketPlayOutListener packetplayoutlistener) {
        packetplayoutlistener.a(this);
    }

    public void handle(PacketListener packetlistener) {
        this.a((PacketPlayOutListener) packetlistener);
    }
}
