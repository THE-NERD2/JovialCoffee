use std::ops::Deref;

use jni::JNIEnv;
use jni::objects::{JString, JValueGen};

use crate::JavaASTObject;
use crate::Node;

use super::parse_nfielddeclaration;
use super::parse_nmethoddeclaration;

pub fn parse_nclass<'a>(env: &mut JNIEnv<'a>, object: &mut JavaASTObject<'a>) -> Option<Node> {
    if object.scan_stage == 0 {
        let name_field = env.get_field(object.object.deref(), "name", "Ljava/lang/String;").unwrap().l().unwrap();
        let name: String = env.get_string(&JString::from(name_field)).unwrap().into();
        object.data = Node::NClass {
            name,
            fields: Vec::new(),
            methods: Vec::new()
        };
        object.scan_stage += 1;
    } else if object.scan_stage == 1 {
        let size = env.call_method(object.object.deref(), "numFields", "()I", &[]).unwrap().i().unwrap();
        let mut temp_data = object.data.clone();
        match object.data.clone() {
            Node::NClass { fields, .. } => {
                if fields.len() == size.try_into().unwrap() {
                    object.scan_stage += 1;
                } else {
                    let next_field = env.call_method(object.object.deref(), "getField", "(I)Lorg/j2c/ast/NFieldDeclaration;", &[JValueGen::Int(fields.len().try_into().unwrap())]).unwrap().l().unwrap();
                    let mut field = JavaASTObject::new(next_field);

                    let mut last_result = None;
                    while last_result == None {
                        last_result = parse_nfielddeclaration(env, &mut field);
                    }
                    match temp_data {
                        Node::NClass { ref mut fields, .. } => {
                            fields.push(last_result.unwrap().clone());
                        },
                        _ => panic!("NClass data isn't an NClass!")
                    };
                }
            },
            _ => panic!("NClass data isn't an NClass!")
        };
        object.data = temp_data;
    } else if object.scan_stage == 2 {
        let size = env.call_method(object.object.deref(), "numMethods", "()I", &[]).unwrap().i().unwrap();
        let mut temp_data = object.data.clone();
        match object.data.clone() {
            Node::NClass { methods, .. } => {
                if methods.len() == size.try_into().unwrap() {
                    object.scan_stage += 1;
                } else {
                    let next_method = env.call_method(object.object.deref(), "getMethod", "(I)Lorg/j2c/ast/NMethodDeclaration;", &[JValueGen::Int(methods.len().try_into().unwrap())]).unwrap().l().unwrap();
                    let mut method = JavaASTObject::new(next_method);

                    let mut last_result = None;
                    while last_result == None {
                        last_result = parse_nmethoddeclaration(env, &mut method);
                    }
                    match temp_data {
                        Node::NClass { ref mut methods, .. } => {
                            methods.push(last_result.unwrap().clone());
                        },
                        _ => panic!("NClass data isn't an NClass!")
                    };
                }
            },
            _ => panic!("NClass data isn't an NClass!")
        };
        object.data = temp_data;
    } else {
        return Some(object.data.clone());
    }
    None
}