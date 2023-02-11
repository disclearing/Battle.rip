package cc.stormworth.meetup.util;

import cc.stormworth.meetup.Meetup;
import com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.util.concurrent.ThreadFactory;

public class TaskUtil {

    public static ThreadFactory newThreadFactory(String name) {
        return (new ThreadFactoryBuilder()).setNameFormat(name).build();
    }

    public static void run(Callable callable) {
        Meetup.getInstance().getServer().getScheduler().runTask(Meetup.getInstance(), callable::call);
    }

    public static void runAsync(Callable callable) {
        Meetup.getInstance().getServer().getScheduler().runTaskAsynchronously(Meetup.getInstance(), callable::call);
    }

    public static void runLater(Callable callable, long delay) {
        Meetup.getInstance().getServer().getScheduler().runTaskLater(Meetup.getInstance(), callable::call, delay);
    }

    public static void runAsyncLater(Callable callable, long delay) {
        Meetup.getInstance().getServer().getScheduler().runTaskLaterAsynchronously(Meetup.getInstance(), callable::call, delay);
    }

    public static void runTimer(Callable callable, long delay, long interval) {
        Meetup.getInstance().getServer().getScheduler().runTaskTimer(Meetup.getInstance(), callable::call, delay, interval);
    }

    public static void runAsyncTimer(Callable callable, long delay, long interval) {
        Meetup.getInstance().getServer().getScheduler().runTaskTimerAsynchronously(Meetup.getInstance(), callable::call, delay, interval);
    }

    public interface Callable {
        void call();
    }
}
