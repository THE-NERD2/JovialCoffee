#pragma once
#include <memory>
#include <string>
#include "jni.h"
#include "rust/cxx.h"
#include "native/src/lib.rs.h"
using namespace std;

unique_ptr<string> getNodeType();
void dropInto(rust::String signature, rust::String field);
void stepOut();
SomePrimitive getPrimitive(rust::String signature, rust::String field);

JNIEXPORT void JNICALL Java_org_j2c_llvm_LLVM_createAST(JNIEnv* env, jobject obj, jobject NClass_root);
JNIEXPORT void JNICALL Java_org_j2c_llvm_LLVM_generateCurrentAST();
JNIEXPORT void JNICALL Java_org_j2c_llvm_LLVM_finishCodeGen();