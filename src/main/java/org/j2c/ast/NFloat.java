package org.j2c.ast;

import org.j2c.llvm.LLVM;

public class NFloat extends Node {
    private final float value;
    public NFloat(float value) {
        this.value = value;
    }
    @Override
    public void codeGen() {
        LLVM.createNumber(value);
    }
}