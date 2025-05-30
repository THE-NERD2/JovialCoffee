#include <llvm/IR/LLVMContext.h>
#include <llvm/IR/IRBuilder.h>
#include <jni.h>
#include <vector>
#include <iostream> // TODO: remove; unnecessary
#include <string>
#include <map>
#include <memory>

using namespace std;
using namespace llvm;

static unique_ptr<LLVMContext> ctx;
static unique_ptr<Module> mod;
static unique_ptr<IRBuilder<>> builder;
static map<string, Value*> vars;

extern "C" {
    JNIEXPORT void JNICALL Java_org_j2c_llvm_LLVM_initCodegen() {
        ctx = make_unique<LLVMContext>();
        mod = make_unique<Module>("main", *ctx);
        builder = make_unique<IRBuilder<>>(*ctx);
    }
    JNIEXPORT void JNICALL Java_org_j2c_llvm_LLVM_emit() {
        // TODO
    }
}