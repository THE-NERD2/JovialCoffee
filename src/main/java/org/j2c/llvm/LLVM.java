package org.j2c.llvm;

public class LLVM {
    static {
        System.loadLibrary("j2c");
    }

    public static native void initCodegen();
    public static native void addClass(ClassData clazz);
    public static native void createClasses();
    public static native void createMethod(MethodData method);
    public static native void emit();
}