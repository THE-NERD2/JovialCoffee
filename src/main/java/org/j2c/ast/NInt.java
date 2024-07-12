package org.j2c.ast;

import org.j2c.llvm.LLVM;

public class NInt extends Node {
    private final int value;
    public NInt(int value) {
        this.value = value;
    }
    @Override
    public void codeGen() {
        LLVM.createNumber(value);
    }
}