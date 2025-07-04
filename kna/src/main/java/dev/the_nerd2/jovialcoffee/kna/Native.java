package dev.the_nerd2.jovialcoffee.kna;

class Native {
    static {
        System.loadLibrary("kna");
    }
    public static native void addLibrary(String name);
    public static native void addFunction(String qualifiedClassName, String functionName);
}
