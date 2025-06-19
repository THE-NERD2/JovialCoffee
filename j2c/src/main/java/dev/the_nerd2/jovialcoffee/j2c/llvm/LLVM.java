package dev.the_nerd2.jovialcoffee.j2c.llvm;

public class LLVM {
    static {
        System.loadLibrary("j2c");
    }

    public static native void initCodegen();
    public static native void addClass(ClassData clazz);
    public static native void createClasses();
    public static native void createMethod(MethodData method);
    public static native void createReference(int id, String var);
    public static native void createBoundReference(int id, int objId, String className, String field);
    public static native void createBoundAssignment(int objId, String className, String field, int valueId);
    public static native void createVoidReturn();
    public static native void free(int id);
    public static native void emit();
}