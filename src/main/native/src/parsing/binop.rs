use std::ops::Deref;

use jni::JNIEnv;
use jni::objects::JString;

use crate::JavaASTObject;
use crate::Node;

use super::parse_node;

pub fn parse_nbinop<'a>(env: &mut JNIEnv<'a>, object: &mut JavaASTObject<'a>) -> Node {
    let type_field = env.get_field(object.object.deref(), "type", "Ljava/lang/String;").unwrap().l().unwrap();
    let op_type: String = env.get_string(&JString::from(type_field)).unwrap().into();

    let op_field = env.get_field(object.object.deref(), "op", "Ljava/lang/String;").unwrap().l().unwrap();
    let op_value: String = env.get_string(&JString::from(op_field)).unwrap().into();

    let left_field = env.get_field(object.object.deref(), "left", "Lorg/j2c/ast/Node;").unwrap().l().unwrap();
    let mut left = JavaASTObject::new(left_field);
    parse_node(env, &mut left);

    let right_field = env.get_field(object.object.deref(), "right", "Lorg/j2c/ast/Node;").unwrap().l().unwrap();
    let mut right = JavaASTObject::new(right_field);
    parse_node(env, &mut right);

    object.data = Node::NBinOp {
        operand_type: op_type,
        op: op_value,
        left: Box::new(left.data),
        right: Box::new(right.data)
    };
    
    object.data.clone()
}