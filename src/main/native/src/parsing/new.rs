use std::ops::Deref;

use jni::JNIEnv;
use jni::objects::JString;

use crate::JavaASTObject;
use crate::Node;

pub fn parse_nnew<'a>(env: &mut JNIEnv<'a>, object: &mut JavaASTObject<'a>) -> Option<Node> {
    if object.scan_stage == 0 {
        let clazz_field = env.get_field(object.object.deref(), "clazz", "Ljava/lang/String;").unwrap().l().unwrap();
        let clazz: String = env.get_string(&JString::from(clazz_field)).unwrap().into();
        object.data = Node::NNew {
            class: clazz
        };
        object.scan_stage += 1;
    } else {
        return Some(object.data.clone());
    }
    None
}