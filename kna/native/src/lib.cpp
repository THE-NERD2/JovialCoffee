#include <iostream> // TODO: remove; unnecessary
#include <jni.h>
using namespace std;

extern "C" {
    JNIEXPORT void JNICALL Java_dev_the_1nerd2_jovialcoffee_kna_Native_addLibrary(JNIEnv* env, jclass thiz, jobject libName) {
        // TODO
        cout << "called addLibrary" << endl;
    }
    JNIEXPORT void JNICALL Java_dev_the_1nerd2_jovialcoffee_kna_Native_addFunction(JNIEnv* env, jclass thiz, jobject qualifiedClassName, jobject functionName) {
        // TODO
        cout << "called addFunction" << endl;
    }
}