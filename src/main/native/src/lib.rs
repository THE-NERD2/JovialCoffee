use jni::JNIEnv;
use jni::objects::JObject;

#[no_mangle]
pub extern "system" fn Java_org_j2c_llvm_LLVM_createAST<'a>(mut env: JNIEnv<'a>, obj: JObject<'a>, root: JObject<'a>) {
    println!("Creating AST");
}
#[no_mangle]
pub extern "system" fn Java_org_j2c_llvm_LLVM_generateCurrentAST() {
    println!("Generating code for current AST");
}
#[no_mangle]
pub extern "system" fn Java_org_j2c_llvm_LLVM_finishCodeGen() {
    println!("Finishing code generation");
}