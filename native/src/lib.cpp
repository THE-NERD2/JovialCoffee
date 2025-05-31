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
public:
    string name;
    map<string, int> fieldOrder;
    StructType* type;
    ClassNode(string name, vector<string> types, map<string, int> fieldOrder): name(name), types(types), fieldOrder(fieldOrder) {
        this->initializeType();
    }
    ~ClassNode() {
        delete this->type;
    }
    void initializeType();
    void createTypeBody();
};

static unique_ptr<LLVMContext> ctx;
static unique_ptr<Module> mod;
static unique_ptr<IRBuilder<>> builder;
static map<string, Value*> vars;
static map<string, ClassNode*> classes;

void ClassNode::initializeType() {
    this->type = StructType::create(*ctx, this->name);
}
void ClassNode::createTypeBody() {
    vector<Type*> types;
    for(string type : this->types) {
        if(type == "boolean") {
            types.push_back(Type::getInt1Ty(*ctx));
        } else if(type == "byte" || type == "char") {
            types.push_back(Type::getInt8Ty(*ctx));
        } else if(type == "short") {
            types.push_back(Type::getInt16Ty(*ctx));
        } else if(type == "int") {
            types.push_back(Type::getInt32Ty(*ctx));
        } else if(type == "long") {
            types.push_back(Type::getInt64Ty(*ctx));
        } else if(type == "float") {
            types.push_back(Type::getFloatTy(*ctx));
        } else if(type == "double") {
            types.push_back(Type::getDoubleTy(*ctx));
        } else {
            // By this point, all StructType's are available, even if they don't have a body yet
            types.push_back(PointerType::getUnqual(classes[type]->type));
        }
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
        jfieldID nFieldDeclarationName = env->GetFieldID(nFieldDeclaration, "name", "Ljava/lang/String;");
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
        classes[nameStr] = new ClassNode(nameStr, types, fieldOrder);
    }
    JNIEXPORT void JNICALL Java_org_j2c_llvm_LLVM_createClasses() {
        for(pair<string, ClassNode*> clazz : classes) {
            clazz.second->createTypeBody();
        }
    }
    JNIEXPORT void JNICALL Java_org_j2c_llvm_LLVM_emit() {
        // TODO
    }
}