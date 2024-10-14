use std::ops::Deref;

use jni::JNIEnv;
use jni::objects::{JClass, JObject, JValueGen, JString};

use ast::Node;

mod ast;

struct JavaASTObject<'a> {
    pub object: Box<JObject<'a>>,
    pub scan_stage: i8,
    pub data: Node<'a>
}
impl<'a> JavaASTObject<'a> {
    pub fn new(object: JObject<'a>) -> Self {
        Self {
            object: Box::new(object),
            scan_stage: 0,
            data: Node::Placeholder
        }
    }
}

static mut classes: Vec<Node<'static>> = Vec::new();

#[no_mangle]
pub unsafe extern "system" fn Java_org_j2c_llvm_LLVM_createAST<'a: 'static>(mut env: JNIEnv<'a>, _: JClass<'a>, root: JObject<'a>) {
    let mut root = JavaASTObject::new(root);
    
    let mut last_result = None;
    while last_result == None {
        last_result = parse_nclass(&mut env, &mut root);
    }
    println!("{:?}", root.data);
    classes.push(root.data);
}
#[no_mangle]
pub extern "system" fn Java_org_j2c_llvm_LLVM_finishCodeGen() {
    println!("Finishing code generation");
}

fn parse_nclass<'a>(env: &mut JNIEnv<'a>, object: &mut JavaASTObject<'a>) -> Option<Node<'a>> {
    if object.scan_stage == 0 {
        object.data = Node::NClass {
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
                    let next_field = env.call_method(object.object.deref(), "getField", "(I)Lorg/j2c/assembly/NFieldDeclaration;", &[JValueGen::Int(fields.len().try_into().unwrap())]).unwrap().l().unwrap();
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
                    let next_method = env.call_method(object.object.deref(), "getMethod", "(I)Lorg/j2c/assembly/NMethodDeclaration;", &[JValueGen::Int(methods.len().try_into().unwrap())]).unwrap().l().unwrap();
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
fn parse_nfielddeclaration<'a>(env: &mut JNIEnv<'a>, object: &mut JavaASTObject<'a>) -> Option<Node<'a>> {
    Some(Node::NOther { str: "SOMEFIELD" })
}
fn parse_nmethoddeclaration<'a>(env: &mut JNIEnv<'a>, object: &mut JavaASTObject<'a>) -> Option<Node<'a>> {
    Some(Node::NOther { str: "SOMEMETHOD" })
}