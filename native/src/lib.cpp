#include <jni.h>
#include <vector>
#include <iostream>

using namespace std;

static vector<jobject> objects;

extern "C" {
    JNIEXPORT void JNICALL Java_org_j2c_llvm_LLVM_addClassAST(JNIEnv* env, jclass clazz, jobject obj) {
        cout << "1\n";
        objects.push_back(obj);
        return;
    }
    JNIEXPORT void JNICALL Java_org_j2c_llvm_LLVM_compile() {
        cout << "2\n";
        return;
    }
}