package org.j2c.ast;

import org.j2c.llvm.LLVM;

public class NByte extends Node {
    private final byte value;
    public NByte(byte value) {
        this.value = value;
    }
    @Override
    public void codeGen() {
        LLVM.createNumber(value);
    }
}