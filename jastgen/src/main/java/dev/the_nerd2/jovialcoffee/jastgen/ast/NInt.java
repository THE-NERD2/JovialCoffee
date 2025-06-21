package dev.the_nerd2.jovialcoffee.jastgen.ast;

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