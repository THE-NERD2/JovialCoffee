use std::ops::Deref;

use jni::JNIEnv;
use jni::objects::JString;

use crate::JavaASTObject;
use crate::Node;

use super::parse_node;

pub fn parse_nstaticreference<'a>(env: &mut JNIEnv<'a>, object: &mut JavaASTObject<'a>) -> Node {
    let field_field = env.get_field(object.object.deref(), "field", "Ljava/lang/String;").unwrap().l().unwrap();
    let field: String = env.get_string(&JString::from(field_field)).unwrap().into();

    object.data = Node::NStaticReference { field };
    object.data.clone()
}
pub fn parse_nstaticassignment<'a>(env: &mut JNIEnv<'a>, object: &mut JavaASTObject<'a>) -> Node {
    let field_field = env.get_field(object.object.deref(), "field", "Ljava/lang/String;").unwrap().l().unwrap();
    let field: String = env.get_string(&JString::from(field_field)).unwrap().into();

    let node_jvalue = env.get_field(object.object.deref(), "v", "Lorg/j2c/ast/Node;").unwrap().l().unwrap();
    let mut node = JavaASTObject::new(node_jvalue);
    parse_node(env, &mut node);

    object.data = Node::NStaticAssignment {
        field,
        v: Box::new(node.data)
    };
    object.data.clone()
}