package org.j2c.assembly;

import org.jetbrains.annotations.NotNull;

public class NBoolean implements Node {
    private final boolean value;
    public NBoolean(boolean value) {
        this.value = value;
    }
    @Override
    @NotNull
    public String toString() {
        return String.valueOf(value);
    }
}