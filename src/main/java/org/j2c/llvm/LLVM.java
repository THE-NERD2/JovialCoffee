package org.j2c.llvm;

import org.j2c.ast.NClass;

public class LLVM {
    static {
        System.loadLibrary("j2c");
    }

    public static native void createAST(NClass root);
    public static native void codeGen();
}