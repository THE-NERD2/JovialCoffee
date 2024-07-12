package org.j2c.ast;

import org.j2c.llvm.LLVM;

public class NLong extends Node {
    private final long value;
    public NLong(long value) {
        this.value = value;
    }
    @Override
    public void codeGen() {
        LLVM.createNumber(value);
    }
}