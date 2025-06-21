package dev.the_nerd2.jovialcoffee.jastgen.ast;

import org.jetbrains.annotations.NotNull;

public class NDouble extends Node {
    private final double value;
    public NDouble(double value) {
        super("NDouble");
        this.value = value;
    }
    @Override
    @NotNull
    public String toString() {
        return String.valueOf(value);
    }
}