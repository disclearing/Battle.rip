package dev.nulledcode.spigot;

import dev.nulledcode.spigot.threads.AbstractThread;
import dev.nulledcode.spigot.threads.HitsThread;
import dev.nulledcode.spigot.threads.KnockbackThread;
import dev.nulledcode.spigot.threads.TickThread;
import org.bukkit.Bukkit;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class BattleThread {

    private final ExecutorService service;
    private AbstractThread asyncHitDetection;
    private AbstractThread tickThread;
    private AbstractThread asyncKnockbackDetection;

    public BattleThread(final int threads) {
        this.service = Executors.newFixedThreadPool(threads);
    }

    public void requestRunnable(Runnable runnable) {
        this.service.submit(runnable);
    }

    public Future<?> requestTask(final Callable<?> callable) {
        return this.service.submit(callable);
    }

    public void loadAsyncThreads() {
        try {
            this.asyncHitDetection = new HitsThread();
            this.asyncKnockbackDetection = new KnockbackThread();
            this.tickThread = new TickThread();
        }
        catch (Exception ex) {
            Bukkit.getLogger().warning("Could not load async threads for KB and hitDetection!");
        }
    }

    public AbstractThread getAsyncHitDetection() {
        return this.asyncHitDetection;
    }

    public AbstractThread getTickThread() {
        return tickThread;
    }

    public AbstractThread getAsyncKnockbackDetection() {
        return this.asyncKnockbackDetection;
    }
}

