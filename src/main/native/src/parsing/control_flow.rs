use std::ops::Deref;

use jni::JNIEnv;
use jni::objects::JValueGen;

use crate::JavaASTObject;
use crate::Node;

use super::parse_node;

pub fn parse_nif<'a>(env: &mut JNIEnv<'a>, object: &mut JavaASTObject<'a>) -> Option<Node> {
    if object.scan_stage == 0 {
        let condition_object = env.get_field(object.object.deref(), "condition", "Lorg/j2c/ast/Node;").unwrap().l().unwrap();
        let mut condition = JavaASTObject::new(condition_object);
        let last_result = parse_node(env, &mut condition);
        object.data = Node::NIf {
            condition: Box::new(last_result.unwrap().clone()),
            if_branch: Vec::new(),
            else_branch: Vec::new()
        };
        object.scan_stage += 1;
    } else if object.scan_stage == 1 {
        let size = env.call_method(object.object.deref(), "ifSize", "()I", &[]).unwrap().i().unwrap();
        let mut temp_data = object.data.clone();
        match object.data.clone() {
            Node::NIf { if_branch, .. } => {
                if if_branch.len() == size.try_into().unwrap() {
                    object.scan_stage += 1;
                } else {
                    let next_node = env.call_method(object.object.deref(), "getIfElement", "(I)Lorg/j2c/ast/Node;", &[JValueGen::Int(if_branch.len().try_into().unwrap())]).unwrap().l().unwrap();
                    let mut node = JavaASTObject::new(next_node);

                    let last_result = parse_node(env, &mut node);
                    match temp_data {
                        Node::NIf { ref mut if_branch, .. } => {
                            if_branch.push(last_result.unwrap().clone());
                        },
                        _ => panic!("NIf data isn't an NIf!")
                    }
                }
            },
            _ => panic!("NIf data isn't an NIf!")
        }
        object.data = temp_data;
    } else if object.scan_stage == 2 {
        let size = env.call_method(object.object.deref(), "elseSize", "()I", &[]).unwrap().i().unwrap();
        let mut temp_data = object.data.clone();
        match object.data.clone() {
            Node::NIf { else_branch, .. } => {
                if else_branch.len() == size.try_into().unwrap() {
                    object.scan_stage += 1;
                } else {
                    let next_node = env.call_method(object.object.deref(), "getElseElement", "(I)Lorg/j2c/ast/Node;", &[JValueGen::Int(else_branch.len().try_into().unwrap())]).unwrap().l().unwrap();
                    let mut node = JavaASTObject::new(next_node);

                    let last_result = parse_node(env, &mut node);
                    match temp_data {
                        Node::NIf { ref mut else_branch, .. } => {
                            else_branch.push(last_result.unwrap().clone());
                        },
                        _ => panic!("NIf data isn't an NIf!")
                    }
                }
            },
            _ => panic!("NIf data isn't an NIf!")
        }
        object.data = temp_data;
    } else {
        return Some(object.data.clone());
    }
    None
}
pub fn parse_nloop<'a>(env: &mut JNIEnv<'a>, object: &mut JavaASTObject<'a>) -> Option<Node> {
    if object.scan_stage == 0 {
        let condition_object = env.get_field(object.object.deref(), "condition", "Lorg/j2c/ast/Node;").unwrap().l().unwrap();
        let mut condition = JavaASTObject::new(condition_object);
        let last_result = parse_node(env, &mut condition);
        object.data = Node::NLoop {
            condition: Box::new(last_result.unwrap().clone()),
            body: Vec::new()
        };
        object.scan_stage += 1;
    } else if object.scan_stage == 1 {
        let size = env.call_method(object.object.deref(), "bodySize", "()I", &[]).unwrap().i().unwrap();
        let mut temp_data = object.data.clone();
        match object.data.clone() {
            Node::NLoop { body, .. } => {
                if body.len() == size.try_into().unwrap() {
                    object.scan_stage += 1;
                } else {
                    let next_node = env.call_method(object.object.deref(), "getBodyElement", "(I)Lorg/j2c/ast/Node;", &[JValueGen::Int(body.len().try_into().unwrap())]).unwrap().l().unwrap();
                    let mut node = JavaASTObject::new(next_node);

                    let last_result = parse_node(env, &mut node);
                    match temp_data {
                        Node::NLoop { ref mut body, .. } => {
                            body.push(last_result.unwrap().clone());
                        },
                        _ => panic!("NLoop data isn't an NLoop!")
                    }
                }
            },
            _ => panic!("NLoop data isn't an NLoop!")
        }
        object.data = temp_data;
    } else {
        return Some(object.data.clone());
    }
    None
}