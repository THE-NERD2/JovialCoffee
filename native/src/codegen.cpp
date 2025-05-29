#include <llvm/IR/LLVMContext.h>
#include <llvm/IR/IRBuilder.h>
#include <string>
#include <map>
#include <memory>

using namespace std;
using namespace llvm;

static unique_ptr<LLVMContext> ctx;
static unique_ptr<Module> mod;
static unique_ptr<IRBuilder<>> builder;
static map<string, Value*> vars;

void initializeCodegen() {
    ctx = make_unique<LLVMContext>();
    mod = make_unique<Module>("main", *ctx);
    builder = make_unique<IRBuilder<>>(*ctx);
}