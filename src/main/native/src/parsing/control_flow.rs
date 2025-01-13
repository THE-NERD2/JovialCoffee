use std::ops::Deref;

use jni::JNIEnv;
use jni::objects::JValueGen;

use crate::JavaASTObject;
use crate::Node;

use super::parse_node;

pub fn parse_nif<'a>(env: &mut JNIEnv<'a>, object: &mut JavaASTObject<'a>) {
    let condition_object = env.get_field(object.object.deref(), "condition", "Lorg/j2c/ast/Node;").unwrap().l().unwrap();
    let mut condition = JavaASTObject::new(condition_object);
    parse_node(env, &mut condition);

    let if_size = env.call_method(object.object.deref(), "ifSize", "()I", &[]).unwrap().i().unwrap();
    let mut if_branch: Vec<Node> = Vec::new();
    while if_branch.len() < if_size.try_into().unwrap() {
        let next_node = env.call_method(object.object.deref(), "getIfElement", "(I)Lorg/j2c/ast/Node;", &[JValueGen::Int(if_branch.len().try_into().unwrap())]).unwrap().l().unwrap();
        let mut node = JavaASTObject::new(next_node);
        parse_node(env, &mut node);

        if_branch.push(node.data);
    }

    let else_size = env.call_method(object.object.deref(), "elseSize", "()I", &[]).unwrap().i().unwrap();
    let mut else_branch: Vec<Node> = Vec::new();
    while else_branch.len() < else_size.try_into().unwrap() {
        let next_node = env.call_method(object.object.deref(), "getElseElement", "(I)Lorg/j2c/ast/Node;", &[JValueGen::Int(else_branch.len().try_into().unwrap())]).unwrap().l().unwrap();
        let mut node = JavaASTObject::new(next_node);
        parse_node(env, &mut node);

        else_branch.push(node.data);
    }

    object.data = Node::NIf {
        condition: Box::new(condition.data),
        if_branch,
        else_branch
    };
}
pub fn parse_nloop<'a>(env: &mut JNIEnv<'a>, object: &mut JavaASTObject<'a>) {
    let condition_object = env.get_field(object.object.deref(), "condition", "Lorg/j2c/ast/Node;").unwrap().l().unwrap();
    let mut condition = JavaASTObject::new(condition_object);
    parse_node(env, &mut condition);

    let size = env.call_method(object.object.deref(), "bodySize", "()I", &[]).unwrap().i().unwrap();
    let mut body: Vec<Node> = Vec::new();
    while body.len() < size.try_into().unwrap() {
        let next_node = env.call_method(object.object.deref(), "getBodyElement", "(I)Lorg/j2c/ast/Node;", &[JValueGen::Int(body.len().try_into().unwrap())]).unwrap().l().unwrap();
        let mut node = JavaASTObject::new(next_node);
        parse_node(env, &mut node);

        body.push(node.data);
    }

    object.data = Node::NLoop {
        condition: Box::new(condition.data),
        body
    };
}