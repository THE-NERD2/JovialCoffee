package org.j2c.assembly;

import org.jetbrains.annotations.NotNull;

public class NDouble extends Node {
    private final double value;
    public NDouble(double value) {
        this.value = value;
    }
    @Override
    @NotNull
    public String toString() {
        return String.valueOf(value);
    }
}