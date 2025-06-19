package dev.the_nerd2.jovialcoffee.j2c.ast;

import org.jetbrains.annotations.NotNull;

public class NChar extends Node {
    private final char value;
    public NChar(char value) {
        super("NChar");
        this.value = value;
    }
    @Override
    @NotNull
    public String toString() {
        return String.valueOf(value);
    }
}