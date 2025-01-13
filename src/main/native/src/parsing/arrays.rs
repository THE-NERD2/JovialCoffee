use std::ops::Deref;

use jni::JNIEnv;
use jni::objects::JString;

use crate::JavaASTObject;
use crate::Node;

use super::parse_node;

pub fn parse_nnewarray<'a>(env: &mut JNIEnv<'a>, object: &mut JavaASTObject<'a>) -> Node {
    let type_jvalue = env.get_field(object.object.deref(), "type", "Ljava/lang/String;").unwrap().l().unwrap();
    let array_type: String = env.get_string(&JString::from(type_jvalue)).unwrap().into();
    
    let length_jvalue = env.get_field(object.object.deref(), "length", "Lorg/j2c/ast/Node;").unwrap().l().unwrap();
    let mut length = JavaASTObject::new(length_jvalue);
    let last_result = parse_node(env, &mut length);

    object.data = Node::NNewArray {
        array_type,
        length: Box::new(last_result.unwrap().clone())
    };

    object.data.clone()
}
pub fn parse_narrayreference<'a>(env: &mut JNIEnv<'a>, object: &mut JavaASTObject<'a>) -> Node {
    let array_jvalue = env.get_field(object.object.deref(), "array", "Lorg/j2c/ast/Node;").unwrap().l().unwrap();
    let mut array = JavaASTObject::new(array_jvalue);
    let array_last_result = parse_node(env, &mut array);

    let index_jvalue = env.get_field(object.object.deref(), "index", "Lorg/j2c/ast/Node;").unwrap().l().unwrap();
    let mut index = JavaASTObject::new(index_jvalue);
    let index_last_result = parse_node(env, &mut index);

    object.data = Node::NArrayReference {
        array: Box::new(array_last_result.unwrap().clone()),
        index: Box::new(index_last_result.unwrap().clone())
    };

    object.data.clone()
}
pub fn parse_narrayassignment<'a>(env: &mut JNIEnv<'a>, object: &mut JavaASTObject<'a>) -> Node {
    let array_jvalue = env.get_field(object.object.deref(), "array", "Lorg/j2c/ast/Node;").unwrap().l().unwrap();
    let mut array = JavaASTObject::new(array_jvalue);
    let array_last_result = parse_node(env, &mut array);

    let index_jvalue = env.get_field(object.object.deref(), "index", "Lorg/j2c/ast/Node;").unwrap().l().unwrap();
    let mut index = JavaASTObject::new(index_jvalue);
    let index_last_result = parse_node(env, &mut index);

    let v_jvalue = env.get_field(object.object.deref(), "v", "Lorg/j2c/ast/Node;").unwrap().l().unwrap();
    let mut v = JavaASTObject::new(v_jvalue);
    let v_last_result = parse_node(env, &mut v);

    object.data = Node::NArrayAssignment {
        array: Box::new(array_last_result.unwrap().clone()),
        index: Box::new(index_last_result.unwrap().clone()),
        v: Box::new(v_last_result.unwrap().clone())
    };

    object.data.clone()
}
pub fn parse_narraylength<'a>(env: &mut JNIEnv<'a>, object: &mut JavaASTObject<'a>) -> Node {
    let node_jvalue = env.get_field(object.object.deref(), "array", "Lorg/j2c/ast/NArrayLength;").unwrap().l().unwrap();
    let mut node = JavaASTObject::new(node_jvalue);

    parse_node(env, &mut node);
    object.data = Node::NArrayLength {
        array: Box::new(node.data)
    };

    object.data.clone()
}