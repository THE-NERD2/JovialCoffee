use std::ops::Deref;

use jni::JNIEnv;
use jni::objects::{JString, JValueGen};

use crate::JavaASTObject;
use crate::Node;

use super::parse_node;

pub fn parse_nstaticcall<'a>(env: &mut JNIEnv<'a>, object: &mut JavaASTObject<'a>) {
    let method_field = env.get_field(object.object.deref(), "method", "Ljava/lang/String;").unwrap().l().unwrap();
    let method: String = env.get_string(&JString::from(method_field)).unwrap().into();

    let size = env.call_method(object.object.deref(), "numArgs", "()I", &[]).unwrap().i().unwrap();
    let mut args: Vec<Node> = Vec::new();
    while args.len() < size.try_into().unwrap() {
        let next_arg_jvalue = env.call_method(object.object.deref(), "getArg", "(I)Lorg/j2c/ast/Node;", &[JValueGen::Int(args.len().try_into().unwrap())]).unwrap().l().unwrap();
        let mut next_arg = JavaASTObject::new(next_arg_jvalue);
        parse_node(env, &mut next_arg);

        args.push(next_arg.data);
    }
    
    object.data = Node::NStaticCall { method, args };
}
pub fn parse_ncall<'a>(env: &mut JNIEnv<'a>, object: &mut JavaASTObject<'a>) {
    let method_field = env.get_field(object.object.deref(), "method", "Ljava/lang/String;").unwrap().l().unwrap();
    let method: String = env.get_string(&JString::from(method_field)).unwrap().into();

    let obj_jvalue = env.get_field(object.object.deref(), "obj", "Lorg/j2c/ast/Node;").unwrap().l().unwrap();
    let mut obj = JavaASTObject::new(obj_jvalue);
    parse_node(env, &mut obj);

    let size = env.call_method(object.object.deref(), "numArgs", "()I", &[]).unwrap().i().unwrap();
    let mut args: Vec<Node> = Vec::new();
    while args.len() < size.try_into().unwrap() {
        let next_arg_jvalue = env.call_method(object.object.deref(), "getArg", "(I)Lorg/j2c/ast/Node;", &[JValueGen::Int(args.len().try_into().unwrap())]).unwrap().l().unwrap();
        let mut next_arg = JavaASTObject::new(next_arg_jvalue);
        parse_node(env, &mut next_arg);

        args.push(next_arg.data);
    }

    object.data = Node::NCall {
        obj: Box::new(obj.data),
        method,
        args
    };
}