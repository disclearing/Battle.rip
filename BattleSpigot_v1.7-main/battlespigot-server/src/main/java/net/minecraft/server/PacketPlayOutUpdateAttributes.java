package net.minecraft.server;

import java.io.IOException;
import java.util.*;

public class PacketPlayOutUpdateAttributes extends Packet {

    private int a;
    private final List b = new ArrayList();

    public PacketPlayOutUpdateAttributes() {}

    public PacketPlayOutUpdateAttributes(int i, Collection collection) {
        this.a = i;
        Iterator iterator = collection.iterator();

        while (iterator.hasNext()) {
            AttributeInstance attributeinstance = (AttributeInstance) iterator.next();

            this.b.add(new AttributeSnapshot(this, attributeinstance.getAttribute().getName(), attributeinstance.b(), attributeinstance.c()));
        }
    }

    public void a(PacketDataSerializer packetdataserializer) throws IOException {
        this.a = packetdataserializer.readInt();
        int i = packetdataserializer.readInt();

        for (int j = 0; j < i; ++j) {
            String s = packetdataserializer.c(64);
            double d0 = packetdataserializer.readDouble();
            ArrayList arraylist = new ArrayList();
            short short1 = packetdataserializer.readShort();

            for (int k = 0; k < short1; ++k) {
                UUID uuid = new UUID(packetdataserializer.readLong(), packetdataserializer.readLong());

                arraylist.add(new AttributeModifier(uuid, "Unknown synced attribute modifier", packetdataserializer.readDouble(), packetdataserializer.readByte()));
            }

            this.b.add(new AttributeSnapshot(this, s, d0, arraylist));
        }
    }

    public void b(PacketDataSerializer packetdataserializer) throws IOException {
        // Spigot start - protocol patch
        if (packetdataserializer.version < 16)
        {
            packetdataserializer.writeInt( this.a );
        } else
        {
            packetdataserializer.b( a );
        }
        // Spigot end
        packetdataserializer.writeInt(this.b.size());
        Iterator iterator = this.b.iterator();

        while (iterator.hasNext()) {
            AttributeSnapshot attributesnapshot = (AttributeSnapshot) iterator.next();

            packetdataserializer.a(attributesnapshot.a());
            packetdataserializer.writeDouble(attributesnapshot.b());
            // Spigot start - protocol patch
            if (packetdataserializer.version < 16 )
            {
                packetdataserializer.writeShort( attributesnapshot.c().size() );
            } else {
                packetdataserializer.b( attributesnapshot.c().size() );
            }
            // Spigot end
            Iterator iterator1 = attributesnapshot.c().iterator();

            while (iterator1.hasNext()) {
                AttributeModifier attributemodifier = (AttributeModifier) iterator1.next();

                packetdataserializer.writeLong(attributemodifier.a().getMostSignificantBits());
                packetdataserializer.writeLong(attributemodifier.a().getLeastSignificantBits());
                packetdataserializer.writeDouble(attributemodifier.d());
                packetdataserializer.writeByte(attributemodifier.c());
            }
        }
    }

    public void a(PacketPlayOutListener packetplayoutlistener) {
        packetplayoutlistener.a(this);
    }

    public void handle(PacketListener packetlistener) {
        this.a((PacketPlayOutListener) packetlistener);
    }
}
