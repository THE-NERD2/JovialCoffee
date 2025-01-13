use std::ops::Deref;

use jni::JNIEnv;
use jni::objects::JString;

use crate::JavaASTObject;
use crate::Node;

pub fn parse_nother<'a>(env: &mut JNIEnv<'a>, object: &mut JavaASTObject<'a>) {
    let str_field = env.get_field(object.object.deref(), "str", "Ljava/lang/String;").unwrap().l().unwrap();
    let str: String = env.get_string(&JString::from(str_field)).unwrap().into();

    object.data = Node::NOther { str };
}