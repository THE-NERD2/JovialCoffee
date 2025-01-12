package org.j2c.ast;

import org.jetbrains.annotations.NotNull;

public class NInt extends Node {
    private final int value;
    public NInt(int value) {
        super("NInt");
        this.value = value;
    }
    @Override
    @NotNull
    public String toString() {
        return String.valueOf(value);
    }
}