package dev.nulledcode.spigot.util;

import net.minecraft.server.Packet;
import net.minecraft.util.com.google.common.collect.Queues;
import net.minecraft.util.io.netty.channel.Channel;
import net.minecraft.util.io.netty.channel.ChannelFuture;
import net.minecraft.util.io.netty.channel.ChannelFutureListener;
import net.minecraft.util.io.netty.util.concurrent.Future;
import net.minecraft.util.io.netty.util.concurrent.GenericFutureListener;

import java.util.Queue;

public class NettyChannelWriter {

    public NettyChannelWriter(Channel channel) {
        this.channel = channel;
    }

    public static void writeThenFlush(Channel channel, Packet value, GenericFutureListener<? extends Future<? super Void>>[] listener) {
        NettyChannelWriter writer = new NettyChannelWriter(channel);
        queue.add(new PacketQueue(value, listener));
        if (tasks.addTask()) channel.pipeline().lastContext().executor().execute(writer::writeQueueAndFlush);
    }

    public void writeQueueAndFlush() {
        while (tasks.fetchTask()) {
            while (queue.size() > 0) {
                PacketQueue messages = queue.poll();
                if (messages != null) {
                    ChannelFuture future = this.channel.write(messages.getPacket());
                    if (messages.getListener() != null)
                        future.addListeners(messages.getListener());
                    future.addListener(ChannelFutureListener.FIRE_EXCEPTION_ON_FAILURE);
                }
            }
        }
        this.channel.flush();
    }

    private static final Queue<PacketQueue> queue = Queues.newConcurrentLinkedQueue();

    private static final Tasks tasks = new Tasks();

    private final Channel channel;

    private static class PacketQueue {
        private final Packet item;

        private final GenericFutureListener<? extends Future<? super Void>>[] listener;

        private PacketQueue(Packet item, GenericFutureListener<?>[] listener) {
            this.item = item;
            this.listener = (GenericFutureListener<? extends Future<? super Void>>[])listener;
        }

        public Packet getPacket() {
            return this.item;
        }

        public GenericFutureListener<? extends Future<? super Void>>[] getListener() {
            return this.listener;
        }
    }
}

