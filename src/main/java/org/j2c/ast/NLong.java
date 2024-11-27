package org.j2c.ast;

import org.jetbrains.annotations.NotNull;

public class NLong extends Node {
    private final long value;
    public NLong(long value) {
        super("NLong");
        this.value = value;
    }
    @Override
    @NotNull
    public String toString() {
        return String.valueOf(value);
    }
}