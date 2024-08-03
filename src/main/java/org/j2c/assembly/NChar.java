package org.j2c.assembly;

import org.jetbrains.annotations.NotNull;

public class NChar extends Node {
    private final char value;
    public NChar(char value) {
        this.value = value;
    }
    @Override
    @NotNull
    public String toString() {
        return String.valueOf(value);
    }
}