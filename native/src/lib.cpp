#include <jni.h>
#include <iostream>

extern "C" void Java_org_j2c_llvm_LLVM_addClassAST(JNIEnv* env, jclass clazz, jobject root) {
    std::cout << "1\n";
    return;
}
extern "C" void Java_org_j2c_llvm_LLVM_compile() {
    std::cout << "2\n";
    return;
}