package net.minecraft.server;

import net.minecraft.util.io.netty.buffer.ByteBuf;
import net.minecraft.util.io.netty.buffer.Unpooled;
import net.minecraft.util.io.netty.channel.ChannelHandlerContext;
import net.minecraft.util.io.netty.handler.codec.ByteToMessageDecoder;
import net.minecraft.util.io.netty.handler.codec.CorruptedFrameException;

import java.util.List;

public class PacketSplitter extends ByteToMessageDecoder {
    public PacketSplitter() {
    }

    protected void decode(ChannelHandlerContext var1, ByteBuf var2, List var3) {
        var2.markReaderIndex();
        byte[] var4 = new byte[3];

        for(int var5 = 0; var5 < var4.length; ++var5) {
            if (!var2.isReadable()) {
                var2.resetReaderIndex();
                return;
            }

            var4[var5] = var2.readByte();
            if (var4[var5] >= 0) {
                PacketDataSerializer var6 = new PacketDataSerializer(Unpooled.wrappedBuffer(var4));

                try {
                    int var7 = var6.a();
                    if (var2.readableBytes() < var7) {
                        var2.resetReaderIndex();
                        return;
                    }

                    var3.add(var2.readBytes(var7));
                } finally {
                    var6.release();
                }

                return;
            }
        }

        throw new CorruptedFrameException("length wider than 21-bit");
    }
}