use crate::ffi::SomePrimitive;

#[cxx::bridge]
mod ffi {
    struct SomePrimitive {
        value_type: UniquePtr<CxxString>,
        bool_value: bool,
        byte_value: i8,
        short_value: i16,
        int_value: i32,
        long_value: i64,
        float_value: f32,
        double_value: f64,
        string_value: UniquePtr<CxxString>
    }
    extern "Rust" {
        fn start_querying();
        fn generate_code();
        fn finalize_ast();
    }
    unsafe extern "C++" {
        include!("native/include/j2c.h");

        fn getNodeType() -> UniquePtr<CxxString>;
        fn dropInto(signature: String, field: String);
        fn stepOut();
        fn getPrimitive(signature: String, field: String) -> SomePrimitive;
    }
}
fn get_node_type() -> String {
    return ffi::getNodeType().to_string();
}
fn drop_into(signature: &str, field: &str) {
    ffi::dropInto(String::from(signature), String::from(field));
}
fn step_out() {
    ffi::stepOut();
}
fn get_primitive(signature: &str, field: &str) -> SomePrimitive {
    return ffi::getPrimitive(String::from(signature), String::from(field));
}
pub fn start_querying() {
    println!("Started querying");
}
pub fn generate_code() {
    println!("Generating code");
}
pub fn finalize_ast() {
    println!("Finalizing AST");
}