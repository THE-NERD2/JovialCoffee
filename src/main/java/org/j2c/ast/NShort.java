package org.j2c.ast;

import org.j2c.llvm.LLVM;

public class NShort extends Node {
    private final short value;
    public NShort(short value) {
        this.value = value;
    }
    @Override
    public void codeGen() {
        LLVM.createNumber(value);
    }
}