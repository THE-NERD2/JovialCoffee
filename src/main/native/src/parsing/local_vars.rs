use std::ops::Deref;

use jni::JNIEnv;
use jni::objects::JString;

use crate::JavaASTObject;
use crate::Node;

use super::parse_node;

pub fn parse_nreference<'a>(env: &mut JNIEnv<'a>, object: &mut JavaASTObject<'a>) {
    let identifier_field = env.get_field(object.object.deref(), "identifier", "Ljava/lang/String;").unwrap().l().unwrap();
    let identifier: String = env.get_string(&JString::from(identifier_field)).unwrap().into();

    object.data = Node::NReference {
        identifier
    };
}
pub fn parse_nassignment<'a>(env: &mut JNIEnv<'a>, object: &mut JavaASTObject<'a>) {
    let dest_field = env.get_field(object.object.deref(), "dest", "Ljava/lang/String;").unwrap().l().unwrap();
    let dest: String = env.get_string(&JString::from(dest_field)).unwrap().into();

    let node_jvalue = env.get_field(object.object.deref(), "v", "Lorg/j2c/ast/Node;").unwrap().l().unwrap();
    let mut node = JavaASTObject::new(node_jvalue);
    parse_node(env, &mut node);

    object.data = Node::NAssignment {
        dest,
        v: Box::new(node.data)
    };
}