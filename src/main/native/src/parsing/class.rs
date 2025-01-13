use std::ops::Deref;

use jni::JNIEnv;
use jni::objects::{JString, JValueGen};

use crate::JavaASTObject;
use crate::Node;

use super::parse_nfielddeclaration;
use super::parse_nmethoddeclaration;

pub fn parse_nclass<'a>(env: &mut JNIEnv<'a>, object: &mut JavaASTObject<'a>) -> Node {
    let name_field = env.get_field(object.object.deref(), "name", "Ljava/lang/String;").unwrap().l().unwrap();
    let name: String = env.get_string(&JString::from(name_field)).unwrap().into();

    let field_size = env.call_method(object.object.deref(), "numFields", "()I", &[]).unwrap().i().unwrap();
    let mut fields: Vec<Node> = Vec::new();
    while fields.len() < field_size.try_into().unwrap() {
        let next_field = env.call_method(object.object.deref(), "getField", "(I)Lorg/j2c/ast/NFieldDeclaration;", &[JValueGen::Int(fields.len().try_into().unwrap())]).unwrap().l().unwrap();
        let mut field = JavaASTObject::new(next_field);
        parse_nfielddeclaration(env, &mut field);

        fields.push(field.data);
    }

    let method_size = env.call_method(object.object.deref(), "numMethods", "()I", &[]).unwrap().i().unwrap();
    let mut methods: Vec<Node> = Vec::new();
    while methods.len() < method_size.try_into().unwrap() {
        let next_method = env.call_method(object.object.deref(), "getMethod", "(I)Lorg/j2c/ast/NMethodDeclaration;", &[JValueGen::Int(methods.len().try_into().unwrap())]).unwrap().l().unwrap();
        let mut method = JavaASTObject::new(next_method);
        parse_nmethoddeclaration(env, &mut method);
        
        methods.push(method.data);
    }

    object.data = Node::NClass { name, fields, methods };

    object.data.clone()
}