package org.j2c.assembly;

import org.jetbrains.annotations.NotNull;

public class NFloat extends Node {
    private final float value;
    public NFloat(float value) {
        this.value = value;
    }
    @Override
    @NotNull
    public String toString() {
        return String.valueOf(value);
    }
}