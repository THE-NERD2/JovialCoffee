package org.j2c.ast;

import org.j2c.llvm.LLVM;

public class NDouble extends Node {
    private final double value;
    public NDouble(double value) {
        this.value = value;
    }
    @Override
    public void codeGen() {
        LLVM.createNumber(value);
    }
}