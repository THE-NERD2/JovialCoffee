package org.j2c.ast;

import org.j2c.llvm.LLVM;

public class NBoolean extends Node {
    private final boolean value;
    public NBoolean(boolean value) {
        this.value = value;
    }
    @Override
    public void codeGen() {
        LLVM.createBoolean(value);
    }
}