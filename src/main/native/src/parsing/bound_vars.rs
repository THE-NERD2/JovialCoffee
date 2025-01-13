use std::ops::Deref;

use jni::JNIEnv;
use jni::objects::JString;

use crate::JavaASTObject;
use crate::Node;

use super::parse_node;

pub fn parse_nboundreference<'a>(env: &mut JNIEnv<'a>, object: &mut JavaASTObject<'a>) -> Node {
    let field_field = env.get_field(object.object.deref(), "field", "Ljava/lang/String;").unwrap().l().unwrap();
    let field: String = env.get_string(&JString::from(field_field)).unwrap().into();

    let node_jvalue = env.get_field(object.object.deref(), "obj", "Lorg/j2c/ast/Node;").unwrap().l().unwrap();
    let mut node = JavaASTObject::new(node_jvalue);
    parse_node(env, &mut node);

    object.data = Node::NBoundReference {
        obj: Box::new(node.data),
        field
    };

    object.data.clone()
}
pub fn parse_nboundassignment<'a>(env: &mut JNIEnv<'a>, object: &mut JavaASTObject<'a>) -> Node {
    let field_field = env.get_field(object.object.deref(), "field", "Ljava/lang/String;").unwrap().l().unwrap();
    let field: String = env.get_string(&JString::from(field_field)).unwrap().into();

    let obj_jvalue = env.get_field(object.object.deref(), "obj", "Lorg/j2c/ast/Node;").unwrap().l().unwrap();
    let mut obj = JavaASTObject::new(obj_jvalue);
    parse_node(env, &mut obj);

    let v_jvalue = env.get_field(object.object.deref(), "v", "Lorg/j2c/ast/Node;").unwrap().l().unwrap();
    let mut v = JavaASTObject::new(v_jvalue);
    parse_node(env, &mut v);

    object.data = Node::NBoundAssignment {
        obj: Box::new(obj.data),
        dest: field,
        v: Box::new(v.data)
    };

    object.data.clone()
}