use std::ops::Deref;

use jni::JNIEnv;

use crate::JavaASTObject;
use crate::Node;

use super::parse_node;

pub fn parse_nnot<'a>(env: &mut JNIEnv<'a>, object: &mut JavaASTObject<'a>) {
    let condition_object = env.get_field(object.object.deref(), "condition", "Lorg/j2c/ast/Node;").unwrap().l().unwrap();
    let mut condition = JavaASTObject::new(condition_object);
    parse_node(env, &mut condition);

    object.data = Node::NNot {
        condition: Box::new(condition.data)
    };
}