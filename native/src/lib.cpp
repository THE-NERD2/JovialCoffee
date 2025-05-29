#include <jni.h>
#include <vector>
#include <iostream> // TODO: remove; unnecessary
#include "ast.hpp"

extern void initializeCodegen();

using namespace std;

static vector<NClass> asts;

extern "C" {
    JNIEXPORT void JNICALL Java_org_j2c_llvm_LLVM_addClassAST(JNIEnv* env, jclass clazz, jobject obj) {
        // TODO: parse and add to asts
    }
    JNIEXPORT void JNICALL Java_org_j2c_llvm_LLVM_compile(JNIEnv* env, jclass clazz) {
        initializeCodegen();
        for(NClass ast : asts) {
            // TODO
        }
    }
}