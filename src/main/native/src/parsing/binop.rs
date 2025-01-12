use std::ops::Deref;

use jni::JNIEnv;
use jni::objects::JString;

use crate::JavaASTObject;
use crate::Node;

use super::parse_node;

pub fn parse_nbinop<'a>(env: &mut JNIEnv<'a>, object: &mut JavaASTObject<'a>) -> Option<Node> {
    if object.scan_stage == 0 {
        object.data = Node::NBinOp {
            operand_type: String::from(""),
            op: String::from(""),
            left: Box::new(Node::Placeholder),
            right: Box::new(Node::Placeholder)
        };
        object.scan_stage += 1;
    } else if object.scan_stage == 1 {
        let type_field = env.get_field(object.object.deref(), "type", "Ljava/lang/String;").unwrap().l().unwrap();
        let op_type: String = env.get_string(&JString::from(type_field)).unwrap().into();

        let op_field = env.get_field(object.object.deref(), "op", "Ljava/lang/String;").unwrap().l().unwrap();
        let op_value: String = env.get_string(&JString::from(op_field)).unwrap().into();

        let left_field = env.get_field(object.object.deref(), "left", "Lorg/j2c/ast/Node;").unwrap().l().unwrap();
        let mut left = JavaASTObject::new(left_field);
        let left_value = parse_node(env, &mut left);

        let right_field = env.get_field(object.object.deref(), "right", "Lorg/j2c/ast/Node;").unwrap().l().unwrap();
        let mut right = JavaASTObject::new(right_field);
        let right_value = parse_node(env, &mut right);

        let mut temp_data = object.data.clone();
        match temp_data {
            Node::NBinOp { ref mut operand_type, ref mut op, ref mut left, ref mut right } => {
                *operand_type = op_type;
                *op = op_value;
                *left = Box::new(left_value.unwrap().clone());
                *right = Box::new(right_value.unwrap().clone());
            },
            _ => panic!("NDDiv data isn't an NDDiv!")
        }
        object.data = temp_data;
        object.scan_stage += 1;
    } else {
        return Some(object.data.clone());
    }
    None
}