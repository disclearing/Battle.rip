package net.minecraft.server;

import net.minecraft.util.com.google.common.collect.BiMap;
import net.minecraft.util.io.netty.buffer.ByteBuf;
import net.minecraft.util.io.netty.channel.ChannelHandlerContext;
import net.minecraft.util.io.netty.handler.codec.ByteToMessageDecoder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;

import java.io.IOException;
import java.util.List;

public class PacketDecoder extends ByteToMessageDecoder {

    private static final Logger a = LogManager.getLogger();
    private static final Marker b = MarkerManager.getMarker("PACKET_RECEIVED", NetworkManager.b);
    private final NetworkStatistics c;

    public PacketDecoder(NetworkStatistics networkstatistics) {
        this.c = networkstatistics;
    }

    protected void decode(ChannelHandlerContext channelhandlercontext, ByteBuf bytebuf, List list) throws IOException {
        int i = bytebuf.readableBytes();

        if (i != 0) {
            PacketDataSerializer packetdataserializer = new PacketDataSerializer(bytebuf, NetworkManager.getVersion(channelhandlercontext.channel())); // Spigot
            int j = packetdataserializer.a();
            Packet packet = Packet.a((BiMap) channelhandlercontext.channel().attr(NetworkManager.e).get(), j);

            if (packet == null) {
                throw new IOException("Bad packet id " + j);
            } else {
                packet.a(packetdataserializer);
                if (packetdataserializer.readableBytes() > 0) {
                    throw new IOException("Packet was larger than I expected, found " + packetdataserializer.readableBytes() + " bytes extra whilst reading packet " + j);
                } else {
                    list.add(packet);
                    this.c.a(j, i);
                    if (a.isDebugEnabled()) {
                        a.debug(b, " IN: [{}:{}] {}[{}]", channelhandlercontext.channel().attr(NetworkManager.d).get(), Integer.valueOf(j), packet.getClass().getName(), packet.b());
                    }
                }
            }
        }
    }
}
