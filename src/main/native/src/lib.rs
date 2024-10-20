use std::ops::Deref;

use jni::JNIEnv;
use jni::objects::{JClass, JObject, JValueGen, JString};

use ast::Node;

mod ast;

struct JavaASTObject<'a> {
    pub object: Box<JObject<'a>>,
    pub scan_stage: i8,
    pub data: Node
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

static mut CLASSES: Vec<Node> = Vec::new();

#[no_mangle]
pub unsafe extern "system" fn Java_org_j2c_llvm_LLVM_createAST<'a: 'static>(mut env: JNIEnv<'a>, _: JClass<'a>, root: JObject<'a>) {
    let mut root = JavaASTObject::new(root);
    
    let mut last_result = None;
    while last_result == None {
        last_result = parse_nclass(&mut env, &mut root);
    }
    println!("{:#?}", root.data);
    CLASSES.push(root.data);
}
#[no_mangle]
pub extern "system" fn Java_org_j2c_llvm_LLVM_finishCodeGen() {
    println!("Finishing code generation");
}

fn parse_nclass<'a>(env: &mut JNIEnv<'a>, object: &mut JavaASTObject<'a>) -> Option<Node> {
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
fn parse_nfielddeclaration<'a>(env: &mut JNIEnv<'a>, object: &mut JavaASTObject<'a>) -> Option<Node> {
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
fn parse_nmethoddeclaration<'a>(env: &mut JNIEnv<'a>, object: &mut JavaASTObject<'a>) -> Option<Node> {
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
                    let next_node = env.call_method(object.object.deref(), "getBodyElement", "(I)Lorg/j2c/assembly/Node;", &[JValueGen::Int(body.len().try_into().unwrap())]).unwrap().l().unwrap();
                    let mut node = JavaASTObject::new(next_node);

                    let mut last_result = None;
                    while last_result == None {
                        last_result = parse_node(env, &mut node);
                    }
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
fn parse_node<'a>(env: &mut JNIEnv<'a>, object: &mut JavaASTObject<'a>) -> Option<Node> {
    Some(Node::NOther { str: String::from("SOMENODE") })
}