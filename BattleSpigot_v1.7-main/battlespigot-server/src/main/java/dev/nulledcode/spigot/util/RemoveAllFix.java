package dev.nulledcode.spigot.util;

import java.util.ArrayList;
import java.util.Collection;

public class RemoveAllFix<T extends RemoveAllFix.Marker> extends ArrayList<T> {

    public RemoveAllFix(int initialCapacity) {
        super(initialCapacity);
    }

    public RemoveAllFix() {
    }

    public RemoveAllFix(Collection<? extends T> collection) {
        super(collection);
    }

    public RemoveAllFix<T> clone() {
        return new RemoveAllFix<T>(this);
    }

    @Override
    public boolean addAll(Collection<? extends T> collection) {
        for (T type : collection) {
            type.setRemovalState(false);
        }

        return super.addAll(collection);
    }

    @Override
    public boolean add(T type) {
        type.setRemovalState(false);
        return super.add(type);
    }

    @Override
    public boolean remove(Object object) {
        ((Marker) object).setRemovalState(true);
        return true;
    }

    @Override
    public boolean removeAll(Collection<?> collection) {
        if (collection != null) {
            for (Object object : collection) {
                ((Marker) object).setRemovalState(true);
            }
        }

        int insertAt = 0;

        int size = size();
        for (int i = 0; i < size; i++) {
            T type = get(i);
            if (type != null && !type.isToBeRemoved()) {
                set(insertAt++, type);
            }
        }

        subList(insertAt, size).clear();

        return size() != size;
    }

    public interface Marker {

        boolean isToBeRemoved();

        void setRemovalState(boolean state);
    }
}
