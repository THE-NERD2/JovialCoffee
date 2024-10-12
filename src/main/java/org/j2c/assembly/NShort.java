package org.j2c.assembly;

import org.jetbrains.annotations.NotNull;

public class NShort implements Node {
    private final short value;
    public NShort(short value) {
        this.value = value;
    }
    @Override
    @NotNull
    public String toString() {
        return String.valueOf(value);
    }
}