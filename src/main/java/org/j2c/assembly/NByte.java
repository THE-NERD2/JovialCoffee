package org.j2c.assembly;

import org.jetbrains.annotations.NotNull;

public class NByte extends Node {
    private final byte value;
    public NByte(byte value) {
        this.value = value;
    }
    @Override
    @NotNull
    public String toString() {
        return String.valueOf(value);
    }
}