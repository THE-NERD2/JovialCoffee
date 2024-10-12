#include "j2c.h"
#include <vector>
using namespace std;

static vector<jobject> objectStack;

unique_ptr<string> getNodeType() {
    return unique_ptr<string>(new string("TODO"));
}
void dropInto(rust::String signature, rust::String field) {
    // TODO
}
void stepOut() {
    // TODO
}
SomePrimitive getPrimitive(rust::String signature, rust::String field) {
    SomePrimitive ret;
    ret.value_type = unique_ptr<string>(new string("string"));
    ret.bool_value = false;
    ret.byte_value = 0;
    ret.short_value = 0;
    ret.int_value = 0;
    ret.long_value = 0;
    ret.float_value = 0.0f;
    ret.double_value = 0.0;
    ret.string_value = unique_ptr<string>(new string("TODO"));
    return ret;
}

JNIEXPORT void JNICALL Java_org_j2c_llvm_LLVM_createAST(JNIEnv* env, jobject obj, jobject root) {
    objectStack.push_back(root);
    start_querying();
}
JNIEXPORT void JNICALL Java_org_j2c_llvm_LLVM_generateCurrentAST() {
    finalize_ast();
}
JNIEXPORT void JNICALL Java_org_j2c_llvm_LLVM_finishCodeGen() {
    generate_code();
}