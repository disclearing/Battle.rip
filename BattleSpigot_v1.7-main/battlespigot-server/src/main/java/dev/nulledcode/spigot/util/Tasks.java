package dev.nulledcode.spigot.util;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Copyright 25/10/2020 Kevin Acaymo
 * Use and or redistribution of compiled JAR file and or source code is permitted only if given
 * explicit permission from original author: Kevin Acaymo
 */
public class Tasks {

    private final AtomicInteger state;

    public Tasks() {
        this.state = new AtomicInteger();
    }

    public boolean fetchTask() {
        int old = this.state.getAndDecrement();
        if (old == State.RUNNING_GOT_TASKS.ordinal())
            return true;
        if (old == State.RUNNING_NO_TASKS.ordinal())
            return false;
        throw new AssertionError();
    }

    public boolean addTask() {
        if (this.state.get() == State.RUNNING_GOT_TASKS.ordinal())
            return false;
        int old = this.state.getAndSet(State.RUNNING_GOT_TASKS.ordinal());
        return (old == State.WAITING.ordinal());
    }

    private enum State {
        WAITING,
        RUNNING_NO_TASKS,
        RUNNING_GOT_TASKS;
    }
}

