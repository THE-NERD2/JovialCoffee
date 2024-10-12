package org.j2c.assembly;

import org.jetbrains.annotations.NotNull;

public class NLong implements Node {
    private final long value;
    public NLong(long value) {
        this.value = value;
    }
    @Override
    @NotNull
    public String toString() {
        return String.valueOf(value);
    }
}