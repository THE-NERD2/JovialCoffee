package dev.the_nerd2.jovialcoffee.j2c.ast;

import org.jetbrains.annotations.NotNull;

public class NShort extends Node {
    private final short value;
    public NShort(short value) {
        super("NShort");
        this.value = value;
    }
    @Override
    @NotNull
    public String toString() {
        return String.valueOf(value);
    }
}