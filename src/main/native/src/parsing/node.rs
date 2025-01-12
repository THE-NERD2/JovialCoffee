use std::ops::Deref;

use jni::JNIEnv;
use jni::objects::JString;

use crate::JavaASTObject;
use crate::Node;

use super::*;

pub fn parse_node<'a>(env: &mut JNIEnv<'a>, object: &mut JavaASTObject<'a>) -> Option<Node> {
    let ast_name_field = env.get_field(object.object.deref(), "astName", "Ljava/lang/String;").unwrap().l().unwrap();
    let ast_name: String = env.get_string(&JString::from(ast_name_field)).unwrap().into();

    let mut last_result = None;
    while last_result == None {
        match ast_name.as_str() {
            "NBoolean" => {
                last_result = parse_nboolean(env, object);
            },
            "NByte" => {
                last_result = parse_nbyte(env, object);
            },
            "NShort" => {
                last_result = parse_nshort(env, object);
            },
            "NInt" => {
                last_result = parse_nint(env, object);
            },
            "NLong" => {
                last_result = parse_nlong(env, object);
            },
            "NFloat" => {
                last_result = parse_nfloat(env, object);
            },
            "NDouble" => {
                last_result = parse_ndouble(env, object);
            },
            "NNull" => {
                last_result = parse_nnull(env, object);
            },
            "NReference" => {
                last_result = parse_nreference(env, object);
            },
            "NAssignment" => {
                last_result = parse_nassignment(env, object);
            },
            "NStaticReference" => {
                last_result = parse_nstaticreference(env, object);
            },
            "NStaticAssignment" => {
                last_result = parse_nstaticassignment(env, object);
            },
            "NBoundReference" => {
                last_result = parse_nboundreference(env, object);
            },
            "NBoundAssignment" => {
                last_result = parse_nboundassignment(env, object);
            },
            "NStaticCall" => {
                last_result = parse_nstaticcall(env, object);
            },
            "NCall" => {
                last_result = parse_ncall(env, object);
            },
            "NNew" => {
                last_result = parse_nnew(env, object);
            },
            "NBinOp" => {
                last_result = parse_nbinop(env, object);
            },
            "NNewArray" => {
                last_result = parse_nnewarray(env, object);
            },
            "NArrayReference" => {
                last_result = parse_narrayreference(env, object);
            },
            "NArrayAssignment" => {
                last_result = parse_narrayassignment(env, object);
            },
            "NArrayLength" => {
                last_result = parse_narraylength(env, object);
            },
            "NReturn" => {
                last_result = parse_nreturn(env, object);
            },
            "NValueReturn" => {
                last_result = parse_nvaluereturn(env, object);
            },
            "NAThrow" => {
                last_result = parse_nathrow(env, object);
            },
            "NNot" => {
                last_result = parse_nnot(env, object);
            },
            "NIf" => {
                last_result = parse_nif(env, object);
            },
            "NLoop" => {
                last_result = parse_nloop(env, object);
            },
            "NOther" => {
                last_result = parse_nother(env, object);
            },
            thing => panic!("Unknown astName: {}", thing)
        }
    }
    last_result
}