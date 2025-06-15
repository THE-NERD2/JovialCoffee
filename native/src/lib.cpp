#include <llvm/IR/LLVMContext.h>
#include <llvm/IR/IRBuilder.h>
#include <llvm/Support/FileSystem.h>
#include <jni.h>
#include <iostream> // TODO: remove; unnecessary
#include <vector>
#include <string>
#include <map>
#include <memory>

using namespace std;
using namespace llvm;

class ClassNode {
private:
    vector<string> types;
public:
    string name;
    map<string, int> fieldOrder;
    StructType* type;
    ClassNode(string name, vector<string> types, map<string, int> fieldOrder): name(name), types(types), fieldOrder(fieldOrder) {
        this->initializeType();
    }
    void initializeType();
    void createTypeBody();
};

static unique_ptr<LLVMContext> ctx;
static unique_ptr<Module> mod;
static unique_ptr<IRBuilder<>> builder;
static Function* currentMethod;
static map<string, Value*> vars;
static map<string, unique_ptr<ClassNode>> classes;
static map<int, Value*> tempValues;

Type* nameToType(string type) {
    if(type == "void") {
        return Type::getVoidTy(*ctx);
    } else if(type == "boolean") {
        return Type::getInt1Ty(*ctx);
    } else if(type == "byte" || type == "char") {
        return Type::getInt8Ty(*ctx);
    } else if(type == "short") {
        return Type::getInt16Ty(*ctx);
    } else if(type == "int") {
        return Type::getInt32Ty(*ctx);
    } else if(type == "long") {
        return Type::getInt64Ty(*ctx);
    } else if(type == "float") {
        return Type::getFloatTy(*ctx);
    } else if(type == "double") {
        return Type::getDoubleTy(*ctx);
    } else {
        // By this point, all StructType's are available, even if they don't have a body yet
        return PointerType::getUnqual(classes[type]->type);
    }
}

void ClassNode::initializeType() {
    this->type = StructType::create(*ctx, this->name);
}
void ClassNode::createTypeBody() {
    vector<Type*> types;
    for(string type : this->types) {
        types.push_back(nameToType(type));
    }
    this->type->setBody(types);
}

extern "C" {
    JNIEXPORT void JNICALL Java_org_j2c_llvm_LLVM_initCodegen() {
        ctx = make_unique<LLVMContext>();
        mod = make_unique<Module>("main", *ctx);
        builder = make_unique<IRBuilder<>>(*ctx);
    }
    JNIEXPORT void JNICALL Java_org_j2c_llvm_LLVM_addClass(JNIEnv* env, jclass thiz, jobject clazz) {
        jclass classData = env->GetObjectClass(clazz);
        jfieldID classDataName = env->GetFieldID(classData, "name", "Ljava/lang/String;");
        jmethodID numFields = env->GetMethodID(classData, "numFields", "()I");
        jmethodID getField = env->GetMethodID(classData, "getField", "(I)Lorg/j2c/ast/NFieldDeclaration;");

        jclass nFieldDeclaration = env->FindClass("org/j2c/ast/NFieldDeclaration");
        jfieldID nFieldDeclarationName = env->GetFieldID(nFieldDeclaration, "cname", "Ljava/lang/String;");
        jfieldID type = env->GetFieldID(nFieldDeclaration, "type", "Ljava/lang/String;");

        map<string, int> fieldOrder;
        vector<string> types;
        int fieldLength = (int) env->CallIntMethod(clazz, numFields);
        for(int i = 0; i < fieldLength; i++) {
            jobject field = env->CallObjectMethod(clazz, getField, i);

            jstring fieldName = (jstring) env->GetObjectField(field, nFieldDeclarationName);
            const char* nameText = env->GetStringUTFChars(fieldName, 0);
            string nameStr(nameText);
            env->ReleaseStringUTFChars(fieldName, nameText);

            jstring fieldType = (jstring) env->GetObjectField(field, type);
            const char* typeText = env->GetStringUTFChars(fieldType, 0);
            string typeStr(typeText);
            env->ReleaseStringUTFChars(fieldType, typeText);

            fieldOrder[nameStr] = i;
            types.push_back(typeStr);
        }

        jstring className = (jstring) env->GetObjectField(clazz, classDataName);
        const char* nameText = env->GetStringUTFChars(className, 0);
        string nameStr(nameText);
        env->ReleaseStringUTFChars(className, nameText);
        classes[nameStr] = make_unique<ClassNode>(nameStr, types, fieldOrder);
    }
    JNIEXPORT void JNICALL Java_org_j2c_llvm_LLVM_createClasses() {
        for(auto& [name, clazz] : classes) {
            clazz->createTypeBody();
        }
    }
    JNIEXPORT void JNICALL Java_org_j2c_llvm_LLVM_createMethod(JNIEnv* env, jclass thiz, jobject method) {
        jclass methodData = env->GetObjectClass(method);
        jfieldID name = env->GetFieldID(methodData, "name", "Ljava/lang/String;");
        jfieldID ret = env->GetFieldID(methodData, "ret", "Ljava/lang/String;");
        jmethodID numArgs = env->GetMethodID(methodData, "numArgs", "()I");
        jmethodID getArg = env->GetMethodID(methodData, "getArg", "(I)Ljava/lang/String;");

        jstring methodRet = (jstring) env->GetObjectField(method, ret);
        const char* retText = env->GetStringUTFChars(methodRet, 0);
        string retStr(retText);
        env->ReleaseStringUTFChars(methodRet, retText);

        jstring methodName = (jstring) env->GetObjectField(method, name);
        const char* nameText = env->GetStringUTFChars(methodName, 0);
        string nameStr(nameText);
        env->ReleaseStringUTFChars(methodName, nameText);

        vector<string> typeNames;
        vector<Type*> types;
        int argc = (int) env->CallIntMethod(method, numArgs);
        for(int i = 0; i < argc; i++) {
            jstring arg = (jstring) env->CallObjectMethod(method, getArg, i);
            const char* argText = env->GetStringUTFChars(arg, 0);
            string argStr(argText);
            env->ReleaseStringUTFChars(arg, argText);
            typeNames.push_back(argStr);
            types.push_back(nameToType(argStr));
        }

        FunctionType* functionType = FunctionType::get(nameToType(retStr), types, false);
        currentMethod = Function::Create(functionType, Function::ExternalLinkage, nameStr, mod.get());
        int i = 0;
        for(Argument& arg : currentMethod->args()) {
            string varName = "param";
            varName += to_string(i); // e.x. param0
            arg.setName(varName);
            vars[varName] = &arg;
            i++;
        }

        BasicBlock* block = BasicBlock::Create(*ctx, "entry", currentMethod);
        builder->SetInsertPoint(block);
    }
    JNIEXPORT void JNICALL Java_org_j2c_llvm_LLVM_createReference(JNIEnv* env, jclass thiz, jint id, jobject var) {
        jstring varName = (jstring) var;
        const char* nameText = env->GetStringUTFChars(varName, 0);
        string nameStr(nameText);
        env->ReleaseStringUTFChars(varName, nameText);
        tempValues[(int) id] = vars[nameStr];
    }
    JNIEXPORT void JNICALL Java_org_j2c_llvm_LLVM_createBoundReference(JNIEnv* env, jclass thiz, jint id, jint objId, jobject className, jobject field) {
        jstring name = (jstring) className;
        const char* nameText = env->GetStringUTFChars(name, 0);
        string nameStr(nameText);
        env->ReleaseStringUTFChars(name, nameText);

        jstring fieldName = (jstring) field;
        const char* fieldText = env->GetStringUTFChars(fieldName, 0);
        string fieldStr(fieldText);
        env->ReleaseStringUTFChars(fieldName, fieldText);

        Value* fieldAddr = builder->CreateStructGEP(
            classes[nameStr]->type,
            tempValues[(int) objId],
            classes[nameStr]->fieldOrder[fieldStr],
            string("ptr_id") + to_string((int) objId) + string("_") + fieldStr // e.x. ptr_id98_someField
        );
        tempValues[(int) id] = builder->CreateLoad(
            fieldAddr->getType()->getPointerElementType(),
            fieldAddr,
            string("val_id") + to_string((int) objId) + string("_") + fieldStr // e.x. val_id98_someField
        );
    }
    JNIEXPORT void JNICALL Java_org_j2c_llvm_LLVM_createBoundAssignment(JNIEnv* env, jclass thiz, jint objId, jobject className, jobject field, jint valueId) {
        jstring name = (jstring) className;
        const char* nameText = env->GetStringUTFChars(name, 0);
        string nameStr(nameText);
        env->ReleaseStringUTFChars(name, nameText);

        jstring fieldName = (jstring) field;
        const char* fieldText = env->GetStringUTFChars(fieldName, 0);
        string fieldStr(fieldText);
        env->ReleaseStringUTFChars(fieldName, fieldText);

        Value* fieldAddr = builder->CreateStructGEP(
            classes[nameStr]->type,
            tempValues[(int) objId],
            classes[nameStr]->fieldOrder[fieldStr],
            string("ptr_id") + to_string((int) objId) + string("_") + fieldStr // e.x. ptr_id98_someField
        );
        builder->CreateStore(tempValues[(int) valueId], fieldAddr);
    }
    JNIEXPORT void JNICALL Java_org_j2c_llvm_LLVM_createVoidReturn() {
        builder->CreateRetVoid();
    }
    JNIEXPORT void JNICALL Java_org_j2c_llvm_LLVM_free(JNIEnv* env, jclass thiz, jint id) {
        tempValues.erase((int) id);
    }
    JNIEXPORT void JNICALL Java_org_j2c_llvm_LLVM_emit() {
        mod->print(outs(), nullptr); // TODO: remove when codegen is consistently accurate
        // Print to file
        error_code EC;
        raw_fd_ostream out("output.ll", EC, sys::fs::OF_None);
        if (EC) {
            cerr << "Error opening file: " << EC.message() << endl;
            return;
        }
        mod->print(out, nullptr);
    }
}