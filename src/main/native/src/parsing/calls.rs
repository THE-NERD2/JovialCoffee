use std::ops::Deref;

use jni::JNIEnv;
use jni::objects::{JString, JValueGen};

use crate::JavaASTObject;
use crate::Node;

use super::parse_node;

pub fn parse_nstaticcall<'a>(env: &mut JNIEnv<'a>, object: &mut JavaASTObject<'a>) -> Option<Node> {
    if object.scan_stage == 0 {
        let method_field = env.get_field(object.object.deref(), "method", "Ljava/lang/String;").unwrap().l().unwrap();
        let method: String = env.get_string(&JString::from(method_field)).unwrap().into();
        object.data = Node::NStaticCall {
            method,
            args: Vec::new()
        };
        object.scan_stage += 1;
    } else if object.scan_stage == 1 {
        let size = env.call_method(object.object.deref(), "numArgs", "()I", &[]).unwrap().i().unwrap();
        let mut temp_data = object.data.clone();
        match object.data.clone() {
            Node::NStaticCall { args, .. } => {
                if args.len() == size.try_into().unwrap() {
                    object.scan_stage += 1;
                } else {
                    let next_arg_jvalue = env.call_method(object.object.deref(), "getArg", "(I)Lorg/j2c/ast/Node;", &[JValueGen::Int(args.len().try_into().unwrap())]).unwrap().l().unwrap();
                    let mut next_arg = JavaASTObject::new(next_arg_jvalue);

                    let last_result = parse_node(env, &mut next_arg);
                    match temp_data {
                        Node::NStaticCall { ref mut args, .. } => {
                            args.push(last_result.unwrap().clone());
                        },
                        _ => panic!("NStaticCall data isn't an NStaticCall!")
                    }
                }
            },
            _ => panic!("NStaticCall data isn't an NStaticCall!")
        };
        object.data = temp_data;
        object.scan_stage += 1;
    } else {
        return Some(object.data.clone());
    }
    None
}
pub fn parse_ncall<'a>(env: &mut JNIEnv<'a>, object: &mut JavaASTObject<'a>) -> Option<Node> {
    if object.scan_stage == 0 {
        let method_field = env.get_field(object.object.deref(), "method", "Ljava/lang/String;").unwrap().l().unwrap();
        let method: String = env.get_string(&JString::from(method_field)).unwrap().into();
        object.data = Node::NCall {
            obj: Box::new(Node::Placeholder),
            method,
            args: Vec::new()
        };
        object.scan_stage += 1;
    } else if object.scan_stage == 1 {
        let node_jvalue = env.get_field(object.object.deref(), "obj", "Lorg/j2c/ast/Node;").unwrap().l().unwrap();
        let mut node = JavaASTObject::new(node_jvalue);

        let last_result = parse_node(env, &mut node);
        let mut temp_data = object.data.clone();
        match temp_data {
            Node::NCall { ref mut obj, .. } => {
                *obj = Box::new(last_result.unwrap().clone());
            },
            _ => panic!("NCall data isn't an NCall!")
        }
        object.data = temp_data;
        object.scan_stage += 1;
    } else if object.scan_stage == 2 {
        let size = env.call_method(object.object.deref(), "numArgs", "()I", &[]).unwrap().i().unwrap();
        let mut temp_data = object.data.clone();
        match object.data.clone() {
            Node::NCall { args, .. } => {
                if args.len() == size.try_into().unwrap() {
                    object.scan_stage += 1;
                } else {
                    let next_arg_jvalue = env.call_method(object.object.deref(), "getArg", "(I)Lorg/j2c/ast/Node;", &[JValueGen::Int(args.len().try_into().unwrap())]).unwrap().l().unwrap();
                    let mut next_arg = JavaASTObject::new(next_arg_jvalue);

                    let last_result = parse_node(env, &mut next_arg);
                    match temp_data {
                        Node::NCall { ref mut args, .. } => {
                            args.push(last_result.unwrap().clone());
                        },
                        _ => panic!("NCall data isn't an NCall!")
                    }
                }
            },
            _ => panic!("NCall data isn't an NCall!")
        };
        object.data = temp_data;
        object.scan_stage += 1;
    } else {
        return Some(object.data.clone());
    }
    None
}