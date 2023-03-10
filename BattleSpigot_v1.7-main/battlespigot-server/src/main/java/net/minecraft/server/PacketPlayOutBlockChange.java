package net.minecraft.server;

public class PacketPlayOutBlockChange extends Packet {

    public int a;
    public int b;
    public int c;
    public Block block; // CraftBukkit - public
    public int data; // CraftBukkit - public
    public boolean fake = false;

    public PacketPlayOutBlockChange() {}

    public PacketPlayOutBlockChange(int i, int j, int k, World world) {
        this.a = i;
        this.b = j;
        this.c = k;
        this.block = world.getType(i, j, k);
        this.data = world.getData(i, j, k);
    }

    public void a(PacketDataSerializer packetdataserializer) {
        this.a = packetdataserializer.readInt();
        this.b = packetdataserializer.readUnsignedByte();
        this.c = packetdataserializer.readInt();
        this.block = Block.getById(packetdataserializer.a());
        this.data = packetdataserializer.readUnsignedByte();
    }

    public void b(PacketDataSerializer packetdataserializer) {
        // Spigot start - protocol patch
        if (packetdataserializer.version < 25 )
        {
            packetdataserializer.writeInt( this.a );
            packetdataserializer.writeByte( this.b );
            packetdataserializer.writeInt( this.c );
            packetdataserializer.b( Block.getId( this.block ) );
            packetdataserializer.writeByte(this.data);
        } else
        {
            packetdataserializer.writePosition( a, b, c );
            int id = Block.getId( this.block );
            data = org.spigotmc.SpigotDebreakifier.getCorrectedData( id, data );
            packetdataserializer.b( (id << 4) | this.data );
        }
        // Spigot end
    }

    public void a(PacketPlayOutListener packetplayoutlistener) {
        packetplayoutlistener.a(this);
    }

    public String b() {
        return String.format("type=%d, data=%d, x=%d, y=%d, z=%d", new Object[] { Integer.valueOf(Block.getId(this.block)), Integer.valueOf(this.data), Integer.valueOf(this.a), Integer.valueOf(this.b), Integer.valueOf(this.c)});
    }

    public void handle(PacketListener packetlistener) {
        this.a((PacketPlayOutListener) packetlistener);
    }
}
