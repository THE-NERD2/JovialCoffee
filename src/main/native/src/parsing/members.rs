use std::ops::Deref;

use jni::JNIEnv;
use jni::objects::{JString, JValueGen};

use crate::JavaASTObject;
use crate::Node;

use super::parse_node;

pub fn parse_nfielddeclaration<'a>(env: &mut JNIEnv<'a>, object: &mut JavaASTObject<'a>) -> Node {
    let name_field = env.get_field(object.object.deref(), "name", "Ljava/lang/String;").unwrap().l().unwrap();
    let name: String = env.get_string(&JString::from(name_field)).unwrap().into();
    let type_field = env.get_field(object.object.deref(), "type", "Ljava/lang/String;").unwrap().l().unwrap();
    let value_type: String = env.get_string(&JString::from(type_field)).unwrap().into();

    object.data = Node::NFieldDeclaration {
        name,
        value_type
    };
    object.data.clone()
}
pub fn parse_nmethoddeclaration<'a>(env: &mut JNIEnv<'a>, object: &mut JavaASTObject<'a>) -> Node {
    let name_field = env.get_field(object.object.deref(), "name", "Ljava/lang/String;").unwrap().l().unwrap();
    let name: String = env.get_string(&JString::from(name_field)).unwrap().into();
    let ret_field = env.get_field(object.object.deref(), "ret", "Ljava/lang/String;").unwrap().l().unwrap();
    let ret: String = env.get_string(&JString::from(ret_field)).unwrap().into();

    let args_size = env.call_method(object.object.deref(), "numArgs", "()I", &[]).unwrap().i().unwrap();
    let mut args: Vec<String> = Vec::new();
    while args.len() < args_size.try_into().unwrap() {
        let next_arg_jvalue = env.call_method(object.object.deref(), "getArg", "(I)Ljava/lang/String;", &[JValueGen::Int(args.len().try_into().unwrap())]).unwrap().l().unwrap();
        let next_arg: String = env.get_string(&JString::from(next_arg_jvalue)).unwrap().into();

        args.push(next_arg);
    }

    let body_size = env.call_method(object.object.deref(), "bodySize", "()I", &[]).unwrap().i().unwrap();
    let mut body: Vec<Node> = Vec::new();
    while body.len() < body_size.try_into().unwrap() {
        let next_node = env.call_method(object.object.deref(), "getBodyElement", "(I)Lorg/j2c/ast/Node;", &[JValueGen::Int(body.len().try_into().unwrap())]).unwrap().l().unwrap();
        let mut node = JavaASTObject::new(next_node);
        parse_node(env, &mut node);

        body.push(node.data);
    }

    object.data = Node::NMethodDeclaration { name, ret, args, body };
    object.data.clone()
}