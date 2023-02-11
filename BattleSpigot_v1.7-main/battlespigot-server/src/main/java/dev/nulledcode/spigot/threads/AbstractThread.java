package dev.nulledcode.spigot.threads;

import dev.nulledcode.spigot.util.NettyChannelWriter;
import net.minecraft.server.NetworkManager;
import net.minecraft.server.Packet;
import net.minecraft.util.io.netty.channel.Channel;
import net.minecraft.util.io.netty.util.concurrent.Future;
import net.minecraft.util.io.netty.util.concurrent.GenericFutureListener;
import org.apache.commons.math3.util.FastMath;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public abstract class AbstractThread {

    private boolean running;
    private int TICK_TIME;
    private Thread t;
    protected Queue<Runnable> packets;

    public AbstractThread() {
        this.running = false;
        this.TICK_TIME = 16666666;
        this.packets = new ConcurrentLinkedQueue<>();
        this.running = true;
        (this.t = new Thread(this::loop)).start();
    }

    public void loop() {
        long lastTick = System.nanoTime();
        long catchupTime = 0L;
        while (this.running) {
            final long curTime = System.nanoTime();
            final long wait = this.TICK_TIME - (curTime - lastTick) - catchupTime;
            if (wait > 0L) {
                try {
                    Thread.sleep(wait / 1000000L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                catchupTime = 0L;
            } else {
                catchupTime = FastMath.min(1000000000L, FastMath.abs(wait));
                this.run();
                lastTick = curTime;
            }
        }
    }

    public abstract void run();

    public void addPacket(final Packet packet, final NetworkManager manager, GenericFutureListener<? extends Future<? super Void>>[] agenericfuturelistener) {
        this.packets.add(() -> NettyChannelWriter.writeThenFlush(manager.m, packet, agenericfuturelistener));
    }

    public Thread getThread() {
        return this.t;
    }
}