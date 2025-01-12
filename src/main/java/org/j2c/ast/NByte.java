package org.j2c.ast;

import org.jetbrains.annotations.NotNull;

public class NByte extends Node {
    private final byte value;
    public NByte(byte value) {
        super("NByte");
        this.value = value;
    }
    @Override
    @NotNull
    public String toString() {
        return String.valueOf(value);
    }
}