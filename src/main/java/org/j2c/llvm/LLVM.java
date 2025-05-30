package org.j2c.llvm;

import org.j2c.ast.NClass;

public class LLVM {
    static {
        System.loadLibrary("j2c");
    }

    public static native void initCodegen();
    public static native void createClass(NClass clazz);
    public static native void emit();
}