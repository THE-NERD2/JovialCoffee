package dev.the_nerd2.jovialcoffee.j2c.ast;

import org.jetbrains.annotations.NotNull;

public class NBoolean extends Node {
    private final boolean value;
    public NBoolean(boolean value) {
        super("NBoolean");
        this.value = value;
    }
    @Override
    @NotNull
    public String toString() {
        return String.valueOf(value);
    }
}