package dev.nulledcode.spigot.threads;

public class TickThread extends AbstractThread {

    @Override
    public void run() {
        while (this.packets.size() > 0) {
            this.packets.poll().run();
        }
    }
}

