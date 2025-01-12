use std::ops::Deref;

use jni::JNIEnv;
use jni::objects::{JString, JValueGen};

use crate::JavaASTObject;
use crate::Node;

use super::parse_node;

pub fn parse_nfielddeclaration<'a>(env: &mut JNIEnv<'a>, object: &mut JavaASTObject<'a>) -> Option<Node> {
    if object.scan_stage == 0 {
        let name_field = env.get_field(object.object.deref(), "name", "Ljava/lang/String;").unwrap().l().unwrap();
        let name: String = env.get_string(&JString::from(name_field)).unwrap().into();
        let type_field = env.get_field(object.object.deref(), "type", "Ljava/lang/String;").unwrap().l().unwrap();
        let value_type: String = env.get_string(&JString::from(type_field)).unwrap().into();
        object.data = Node::NFieldDeclaration {
            name,
            value_type
        };
        object.scan_stage += 1;
    } else {
        return Some(object.data.clone());
    }
    None
}
pub fn parse_nmethoddeclaration<'a>(env: &mut JNIEnv<'a>, object: &mut JavaASTObject<'a>) -> Option<Node> {
    if object.scan_stage == 0 {
        let name_field = env.get_field(object.object.deref(), "name", "Ljava/lang/String;").unwrap().l().unwrap();
        let name: String = env.get_string(&JString::from(name_field)).unwrap().into();
        let ret_field = env.get_field(object.object.deref(), "ret", "Ljava/lang/String;").unwrap().l().unwrap();
        let ret: String = env.get_string(&JString::from(ret_field)).unwrap().into();
        object.data = Node::NMethodDeclaration {
            name,
            ret,
            args: Vec::new(),
            body: Vec::new()
        };
        object.scan_stage += 1;
    } else if object.scan_stage == 1 {
        let size = env.call_method(object.object.deref(), "numArgs", "()I", &[]).unwrap().i().unwrap();
        let mut temp_data = object.data.clone();
        match object.data.clone() {
            Node::NMethodDeclaration { args, .. } => {
                if args.len() == size.try_into().unwrap() {
                    object.scan_stage += 1;
                } else {
                    let next_arg_jvalue = env.call_method(object.object.deref(), "getArg", "(I)Ljava/lang/String;", &[JValueGen::Int(args.len().try_into().unwrap())]).unwrap().l().unwrap();
                    let next_arg: String = env.get_string(&JString::from(next_arg_jvalue)).unwrap().into();
                    match temp_data {
                        Node::NMethodDeclaration { ref mut args, .. } => {
                            args.push(next_arg);
                        },
                        _ => panic!("NMethodDeclaration data isn't an NMethodDeclaration!")
                    }
                }
            },
            _ => panic!("NMethodDeclaration data isn't an NMethodDeclaration!")
        };
        object.data = temp_data;
    } else if object.scan_stage == 2 {
        let size = env.call_method(object.object.deref(), "bodySize", "()I", &[]).unwrap().i().unwrap();
        let mut temp_data = object.data.clone();
        match object.data.clone() {
            Node::NMethodDeclaration { body, .. } => {
                if body.len() == size.try_into().unwrap() {
                    object.scan_stage += 1;
                } else {
                    let next_node = env.call_method(object.object.deref(), "getBodyElement", "(I)Lorg/j2c/ast/Node;", &[JValueGen::Int(body.len().try_into().unwrap())]).unwrap().l().unwrap();
                    let mut node = JavaASTObject::new(next_node);

                    let last_result = parse_node(env, &mut node);
                    match temp_data {
                        Node::NMethodDeclaration { ref mut body, .. } => {
                            body.push(last_result.unwrap().clone());
                        },
                        _ => panic!("NMethodDeclaration data isn't an NMethodDeclaration!")
                    }
                }
            },
            _ => panic!("NMethodDeclaration data isn't an NMethodDeclaration!")
        };
        object.data = temp_data;
    } else {
        return Some(object.data.clone());
    }
    None
}