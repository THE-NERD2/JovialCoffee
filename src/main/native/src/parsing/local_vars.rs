use std::ops::Deref;

use jni::JNIEnv;
use jni::objects::JString;

use crate::JavaASTObject;
use crate::Node;

use super::parse_node;

pub fn parse_nreference<'a>(env: &mut JNIEnv<'a>, object: &mut JavaASTObject<'a>) -> Option<Node> {
    if object.scan_stage == 0 {
        let identifier_field = env.get_field(object.object.deref(), "identifier", "Ljava/lang/String;").unwrap().l().unwrap();
        let identifier: String = env.get_string(&JString::from(identifier_field)).unwrap().into();
        object.data = Node::NReference {
            identifier
        };
        object.scan_stage += 1;
    } else {
        return Some(object.data.clone());
    }
    None
}
pub fn parse_nassignment<'a>(env: &mut JNIEnv<'a>, object: &mut JavaASTObject<'a>) -> Option<Node> {
    if object.scan_stage == 0 {
        let dest_field = env.get_field(object.object.deref(), "dest", "Ljava/lang/String;").unwrap().l().unwrap();
        let dest: String = env.get_string(&JString::from(dest_field)).unwrap().into();
        object.data = Node::NAssignment {
            dest,
            v: Box::new(Node::Placeholder)
        };
        object.scan_stage += 1;
    } else if object.scan_stage == 1 {
        let node_jvalue = env.get_field(object.object.deref(), "v", "Lorg/j2c/ast/Node;").unwrap().l().unwrap();
        let mut node = JavaASTObject::new(node_jvalue);

        let last_result = parse_node(env, &mut node);
        let mut temp_data = object.data.clone();
        match temp_data {
            Node::NAssignment { ref mut v, .. } => {
                *v = Box::new(last_result.unwrap().clone());
            },
            _ => panic!("NAssignment data isn't an NAssignment!")
        };
        object.data = temp_data;
        object.scan_stage += 1;
    } else {
        return Some(object.data.clone());
    }
    None
}