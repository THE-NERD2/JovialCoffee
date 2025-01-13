use std::ops::Deref;

use jni::JNIEnv;
use jni::objects::JString;

use crate::JavaASTObject;
use crate::Node;

use super::parse_node;

pub fn parse_nreturn<'a>(_: &mut JNIEnv<'a>, object: &mut JavaASTObject<'a>) {
    object.data = Node::NReturn
}
pub fn parse_nvaluereturn<'a>(env: &mut JNIEnv<'a>, object: &mut JavaASTObject<'a>) {
    let type_field = env.get_field(object.object.deref(), "type", "Ljava/lang/String;").unwrap().l().unwrap();
    let return_type: String = env.get_string(&JString::from(type_field)).unwrap().into();

    let node_jvalue = env.get_field(object.object.deref(), "v", "Lorg/j2c/ast/Node;").unwrap().l().unwrap();
    let mut node = JavaASTObject::new(node_jvalue);
    parse_node(env, &mut node);

    object.data = Node::NValueReturn {
        return_type,
        v: Box::new(node.data)
    };
}
pub fn parse_nathrow<'a>(env: &mut JNIEnv<'a>, object: &mut JavaASTObject<'a>) {
    let node_jvalue = env.get_field(object.object.deref(), "v", "Lorg/j2c/ast/Node;").unwrap().l().unwrap();
    let mut node = JavaASTObject::new(node_jvalue);
    parse_node(env, &mut node);

    object.data = Node::NAThrow {
        v: Box::new(node.data)
    };
}