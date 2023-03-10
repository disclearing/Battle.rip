package net.minecraft.server;

public class PacketPlayInArmAnimation extends Packet {

    private int a;
    private int b;

    public PacketPlayInArmAnimation() {}

    public void a(PacketDataSerializer packetdataserializer) {
        // Spigot start - protocol patch
        if (packetdataserializer.version < 16 )
        {
            this.a = packetdataserializer.readInt();
            this.b = packetdataserializer.readByte();
        } else {
            b = 1;
        }
        // Spigot end
    }

    public void b(PacketDataSerializer packetdataserializer) {
        packetdataserializer.writeInt(this.a);
        packetdataserializer.writeByte(this.b);
    }

    public void a(PacketPlayInListener packetplayinlistener) {
        packetplayinlistener.a(this);
    }

    public String b() {
        return String.format("id=%d, type=%d", new Object[] { Integer.valueOf(this.a), Integer.valueOf(this.b)});
    }

    public int d() {
        return this.b;
    }

    public void handle(PacketListener packetlistener) {
        this.a((PacketPlayInListener) packetlistener);
    }
}
