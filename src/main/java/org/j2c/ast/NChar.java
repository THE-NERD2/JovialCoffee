package org.j2c.ast;

import org.j2c.llvm.LLVM;

public class NChar extends Node {
    private final char value;
    public NChar(char value) {
        this.value = value;
    }
    @Override
    public void codeGen() {
        LLVM.createCharacter(value);
    }
}