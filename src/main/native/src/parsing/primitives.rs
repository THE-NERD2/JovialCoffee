use std::ops::Deref;

use jni::JNIEnv;

use crate::JavaASTObject;
use crate::Node;

pub fn parse_nboolean<'a>(env: &mut JNIEnv<'a>, object: &mut JavaASTObject<'a>) -> Option<Node> {
    let value = env.get_field(object.object.deref(), "value", "Z").unwrap().z().unwrap();
    Some(Node::NBoolean { value })
}
pub fn parse_nbyte<'a>(env: &mut JNIEnv<'a>, object: &mut JavaASTObject<'a>) -> Option<Node> {
    let value = env.get_field(object.object.deref(), "value", "B").unwrap().b().unwrap();
    Some(Node::NByte { value })
}
pub fn parse_nshort<'a>(env: &mut JNIEnv<'a>, object: &mut JavaASTObject<'a>) -> Option<Node> {
    let value = env.get_field(object.object.deref(), "value", "S").unwrap().s().unwrap();
    Some(Node::NShort { value })
}
pub fn parse_nint<'a>(env: &mut JNIEnv<'a>, object: &mut JavaASTObject<'a>) -> Option<Node> {
    let value = env.get_field(object.object.deref(), "value", "I").unwrap().i().unwrap();
    Some(Node::NInt { value })
}
pub fn parse_nlong<'a>(env: &mut JNIEnv<'a>, object: &mut JavaASTObject<'a>) -> Option<Node> {
    let value = env.get_field(object.object.deref(), "value", "J").unwrap().j().unwrap();
    Some(Node::NLong { value })
}
pub fn parse_nfloat<'a>(env: &mut JNIEnv<'a>, object: &mut JavaASTObject<'a>) -> Option<Node> {
    let value = env.get_field(object.object.deref(), "value", "F").unwrap().f().unwrap();
    Some(Node::NFloat { value })
}
pub fn parse_ndouble<'a>(env: &mut JNIEnv<'a>, object: &mut JavaASTObject<'a>) -> Option<Node> {
    let value = env.get_field(object.object.deref(), "value", "D").unwrap().d().unwrap();
    Some(Node::NDouble { value })
}
pub fn parse_nnull<'a>(_: &mut JNIEnv<'a>, _: &mut JavaASTObject<'a>) -> Option<Node> {
    Some(Node::NNull)
}