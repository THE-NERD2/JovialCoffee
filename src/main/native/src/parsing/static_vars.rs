use std::ops::Deref;

use jni::JNIEnv;
use jni::objects::JString;

use crate::JavaASTObject;
use crate::Node;

use super::parse_node;

pub fn parse_nstaticreference<'a>(env: &mut JNIEnv<'a>, object: &mut JavaASTObject<'a>) -> Option<Node> {
    if object.scan_stage == 0 {
        let field_field = env.get_field(object.object.deref(), "field", "Ljava/lang/String;").unwrap().l().unwrap();
        let field: String = env.get_string(&JString::from(field_field)).unwrap().into();
        object.data = Node::NStaticReference {
            field
        };
        object.scan_stage += 1;
    } else {
        return Some(object.data.clone());
    }
    None
}
pub fn parse_nstaticassignment<'a>(env: &mut JNIEnv<'a>, object: &mut JavaASTObject<'a>) -> Option<Node> {
    if object.scan_stage == 0 {
        let field_field = env.get_field(object.object.deref(), "field", "Ljava/lang/String;").unwrap().l().unwrap();
        let field: String = env.get_string(&JString::from(field_field)).unwrap().into();
        object.data = Node::NStaticAssignment {
            field,
            v: Box::new(Node::Placeholder)
        };
        object.scan_stage += 1;
    } else if object.scan_stage == 1 {
        let node_jvalue = env.get_field(object.object.deref(), "v", "Lorg/j2c/ast/Node;").unwrap().l().unwrap();
        let mut node = JavaASTObject::new(node_jvalue);

        let last_result = parse_node(env, &mut node);
        let mut temp_data = object.data.clone();
        match temp_data {
            Node::NStaticAssignment { ref mut v, .. } => {
                *v = Box::new(last_result.unwrap().clone());
            },
            _ => panic!("NStaticAssignment data isn't an NStaticAssignment!")
        };
        object.data = temp_data;
        object.scan_stage += 1;
    } else {
        return Some(object.data.clone());
    }
    None
}