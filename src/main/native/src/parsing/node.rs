use std::ops::Deref;

use jni::JNIEnv;
use jni::objects::JString;

use crate::JavaASTObject;

use super::*;

pub fn parse_node<'a>(env: &mut JNIEnv<'a>, object: &mut JavaASTObject<'a>) {
    let ast_name_field = env.get_field(object.object.deref(), "astName", "Ljava/lang/String;").unwrap().l().unwrap();
    let ast_name: String = env.get_string(&JString::from(ast_name_field)).unwrap().into();

    match ast_name.as_str() {
        "NBoolean" => {
            parse_nboolean(env, object);
        },
        "NByte" => {
            parse_nbyte(env, object);
        },
        "NShort" => {
            parse_nshort(env, object);
        },
        "NInt" => {
            parse_nint(env, object);
        },
        "NLong" => {
            parse_nlong(env, object);
        },
        "NFloat" => {
            parse_nfloat(env, object);
        },
        "NDouble" => {
            parse_ndouble(env, object);
        },
        "NNull" => {
            parse_nnull(env, object);
        },
        "NReference" => {
            parse_nreference(env, object);
        },
        "NAssignment" => {
            parse_nassignment(env, object);
        },
        "NStaticReference" => {
            parse_nstaticreference(env, object);
        },
        "NStaticAssignment" => {
            parse_nstaticassignment(env, object);
        },
        "NBoundReference" => {
            parse_nboundreference(env, object);
        },
        "NBoundAssignment" => {
            parse_nboundassignment(env, object);
        },
        "NStaticCall" => {
            parse_nstaticcall(env, object);
        },
        "NCall" => {
            parse_ncall(env, object);
        },
        "NNew" => {
            parse_nnew(env, object);
        },
        "NBinOp" => {
            parse_nbinop(env, object);
        },
        "NNewArray" => {
            parse_nnewarray(env, object);
        },
        "NArrayReference" => {
            parse_narrayreference(env, object);
        },
        "NArrayAssignment" => {
            parse_narrayassignment(env, object);
        },
        "NArrayLength" => {
            parse_narraylength(env, object);
        },
        "NReturn" => {
            parse_nreturn(env, object);
        },
        "NValueReturn" => {
            parse_nvaluereturn(env, object);
        },
        "NAThrow" => {
            parse_nathrow(env, object);
        },
        "NNot" => {
            parse_nnot(env, object);
        },
        "NIf" => {
            parse_nif(env, object);
        },
        "NLoop" => {
            parse_nloop(env, object);
        },
        "NOther" => {
            parse_nother(env, object);
        },
        thing => panic!("Unknown astName: {}", thing)
    }
}