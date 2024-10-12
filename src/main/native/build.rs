fn main() {
    let java_home = std::env::var("JAVA_HOME").expect("JAVA_HOME environment variable not set but needed to build.");
    let jni_include_dir = format!("{}/include", java_home);
    let jni_platform_dir = if cfg!(target_os = "linux") {
        format!("{}/include/linux", java_home)
    } else if cfg!(target_os = "windows") {
        format!("{}/include/win32", java_home)
    } else if cfg!(target_os = "macos") {
        format!("{}/include/darwin", java_home)
    } else {
        panic!("Unsupported platform")
    };
    cxx_build::bridge("src/lib.rs")
        .file("src/j2c.cpp")
        .include("include")
        .include("target/cxxbridge")
        .include(&jni_include_dir)
        .include(&jni_platform_dir)
        .flag_if_supported("-std=c++14")
        .compile("j2c");
    println!("cargo:rustc-link-lib=dylib=jvm");
    println!("cargo:rustc-link-search=native={}/lib/server", java_home);
}