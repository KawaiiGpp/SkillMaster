package com.akira.skillmaster.manager;

import com.akira.skillmaster.base.Manager;
import com.akira.skillmaster.base.AttributeType;

import java.util.Set;
import java.util.stream.Stream;

public class AttributeTypeManager<T extends AttributeType> extends Manager<T> {
    public Set<T> copySet() {
        return super.copySet();
    }

    public T fromString(String registerName) {
        Stream<T> stream = set.stream().filter(t -> registerName.equals(t.getRegisterName()));
        return stream.findFirst().orElse(null);
    }
}
