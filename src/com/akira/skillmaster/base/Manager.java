package com.akira.skillmaster.base;

import org.apache.commons.lang3.Validate;

import java.util.HashSet;
import java.util.Set;

public abstract class Manager<T> {
    protected Set<T> set = new HashSet<>();

    protected Manager() {}

    public void register(T t) {
        Validate.isTrue(!set.contains(t), "Value already registered.");
        set.add(t);
    }

    protected void unregister(T t) {
        Validate.isTrue(set.contains(t), "Value not registered yet.");
        set.remove(t);
    }

    protected Set<T> copySet() {
        return new HashSet<>(set);
    }

    protected abstract T fromString(String string);
}
