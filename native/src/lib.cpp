#include <llvm/IR/LLVMContext.h>
#include <llvm/IR/IRBuilder.h>
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
    bool isComplete;
    map<string, bool> missingTypes;
public:
    string name;
    map<string, int> fieldOrder;
    StructType* type;
    ClassNode(string name, vector<string> types, map<string, int> fieldOrder): name(name), types(types), fieldOrder(fieldOrder) {
        this->type = NULL;
        this->isComplete = false;
    }
    ~ClassNode() {
        delete this->type;
    }
    void tryComplete();
    void tryComplete(string name);
};

static unique_ptr<LLVMContext> ctx;
static unique_ptr<Module> mod;
static unique_ptr<IRBuilder<>> builder;
static map<string, Value*> vars;
static map<string, ClassNode*> classes;

void ClassNode::tryComplete() {
    if(this->isComplete) return;
    ArrayRef<Type*> types;
    for(string type : this->types) {
        if(type == "boolean") {
            types.vec().push_back(Type::getInt1Ty(*ctx));
        } else if(type == "byte" || type == "char") {
            types.vec().push_back(Type::getInt8Ty(*ctx));
        } else if(type == "short") {
            types.vec().push_back(Type::getInt16Ty(*ctx));
        } else if(type == "int") {
            types.vec().push_back(Type::getInt32Ty(*ctx));
        } else if(type == "long") {
            types.vec().push_back(Type::getInt64Ty(*ctx));
        } else if(type == "float") {
            types.vec().push_back(Type::getFloatTy(*ctx));
        } else if(type == "double") {
            types.vec().push_back(Type::getDoubleTy(*ctx));
        } else {
            if(classes.find(type) != classes.end()) {
                if(classes[type]->isComplete) {
                    types.vec().push_back(classes[type]->type);
                } else {
                    goto typeIsMissing;
                }
            } else {
typeIsMissing:
                this->missingTypes[type] = true;
                return;
            }
        }
    }
    this->type = StructType::create(*ctx, types);
    this->isComplete = true;
    for(pair<string, ClassNode*> clazz : classes) {
        clazz.second->tryComplete(this->name);
    }
}
void ClassNode::tryComplete(string name) {
    if(this->isComplete) return;
    if(this->missingTypes[name]) {
        this->missingTypes[name] = false;
        this->tryComplete();
    }
}

extern "C" {
    JNIEXPORT void JNICALL Java_org_j2c_llvm_LLVM_initCodegen() {
        ctx = make_unique<LLVMContext>();
        mod = make_unique<Module>("main", *ctx);
        builder = make_unique<IRBuilder<>>(*ctx);
    }
    JNIEXPORT void JNICALL Java_org_j2c_llvm_LLVM_createClass(JNIEnv* env, jclass thiz, jobject clazz) {
        jclass nClass = env->GetObjectClass(clazz);
        jfieldID cname = env->GetFieldID(nClass, "cname", "Ljava/lang/String;");
        jmethodID numFields = env->GetMethodID(nClass, "numFields", "()I");
        jmethodID getField = env->GetMethodID(nClass, "getField", "(I)Lorg/j2c/ast/NFieldDeclaration;");

        jclass nFieldDeclaration = env->FindClass("org/j2c/ast/NFieldDeclaration");
        jfieldID name = env->GetFieldID(nFieldDeclaration, "name", "Ljava/lang/String;");
        jfieldID type = env->GetFieldID(nFieldDeclaration, "type", "Ljava/lang/String;");

        map<string, int> fieldOrder;
        vector<string> types;
        int fieldLength = (int) env->CallIntMethod(clazz, numFields);
        for(int i = 0; i < fieldLength; i++) {
            jobject field = env->CallObjectMethod(clazz, getField, i);

            jstring fieldName = (jstring) env->GetObjectField(field, name);
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

        jstring className = (jstring) env->GetObjectField(clazz, cname);
        const char* nameText = env->GetStringUTFChars(className, 0);
        string nameStr(nameText);
        env->ReleaseStringUTFChars(className, nameText);
        classes[nameStr] = new ClassNode(nameStr, types, fieldOrder);
    }
    JNIEXPORT void JNICALL Java_org_j2c_llvm_LLVM_emit() {
        // TODO
    }
}