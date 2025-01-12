use std::ops::Deref;

use jni::JNIEnv;
use jni::objects::JString;

use crate::JavaASTObject;
use crate::Node;

use super::parse_node;

pub fn parse_nreturn<'a>(_: &mut JNIEnv<'a>, _: &mut JavaASTObject<'a>) -> Option<Node> {
    Some(Node::NReturn)
}
pub fn parse_nvaluereturn<'a>(env: &mut JNIEnv<'a>, object: &mut JavaASTObject<'a>) -> Option<Node> {
    if object.scan_stage == 0 {
        let type_field = env.get_field(object.object.deref(), "type", "Ljava/lang/String;").unwrap().l().unwrap();
        let return_type: String = env.get_string(&JString::from(type_field)).unwrap().into();
        object.data = Node::NValueReturn {
            return_type,
            v: Box::new(Node::Placeholder)
        };
        object.scan_stage += 1;
    } else if object.scan_stage == 1 {
        let node_jvalue = env.get_field(object.object.deref(), "v", "Lorg/j2c/ast/Node;").unwrap().l().unwrap();
        let mut node = JavaASTObject::new(node_jvalue);

        let last_value = parse_node(env, &mut node);
        let mut temp_data = object.data.clone();
        match temp_data {
            Node::NValueReturn { ref mut v, .. } => {
                *v = Box::new(last_value.unwrap().clone());
            },
            _ => panic!("NValueReturn data isn't an NValueReturn!")
        };
        object.data = temp_data;
        object.scan_stage += 1;
    } else {
        return Some(object.data.clone());
    }
    None
}
pub fn parse_nathrow<'a>(env: &mut JNIEnv<'a>, object: &mut JavaASTObject<'a>) -> Option<Node> {
    if object.scan_stage == 0 {
        object.data = Node::NAThrow {
            v: Box::new(Node::Placeholder)
        };
        object.scan_stage += 1;
    } else if object.scan_stage == 1 {
        let node_jvalue = env.get_field(object.object.deref(), "v", "Lorg/j2c/ast/Node;").unwrap().l().unwrap();
        let mut node = JavaASTObject::new(node_jvalue);

        let last_value = parse_node(env, &mut node);
        let mut temp_data = object.data.clone();
        match temp_data {
            Node::NAThrow { ref mut v } => {
                *v = Box::new(last_value.unwrap().clone());
            },
            _ => panic!("NAThrow data isn't an NAThrow!")
        };
        object.data = temp_data;
        object.scan_stage += 1;
    } else {
        return Some(object.data.clone());
    }
    None
}