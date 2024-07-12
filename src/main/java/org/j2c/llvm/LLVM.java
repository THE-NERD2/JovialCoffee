package org.j2c.llvm;

import org.j2c.ast.Node;

public class LLVM {
    static {
        System.loadLibrary("j2c");
    }

    public static native void createNumber(byte value);
    public static native void createNumber(short value);
    public static native void createNumber(int value);
    public static native void createNumber(long value);
    public static native void createNumber(float value);
    public static native void createNumber(double value);
    public static native void createBoolean(boolean value);
    public static native void createCharacter(char value);
    public static native void declareVariable(String name, String type);
    public static native void declareMethod(String name, String ret, String[] args, Node[] body);
}