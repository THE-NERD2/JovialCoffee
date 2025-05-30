cmake_minimum_required(VERSION 3.24)

project(J2C LANGUAGES C CXX)

#Use this normally. Due to a local error in IntelliJ IDEA, I'm overriding it.
#set(JAVA_INCLUDE_PATH "$ENV{JAVA_HOME}/include")
set(JAVA_INCLUDE_PATH "/usr/lib/jvm/temurin-21-jdk-arm64/include")
find_package(LLVM REQUIRED CONFIG)
find_package(JNI REQUIRED)


include_directories(${LLVM_INCLUDE_DIRS})
add_definitions(${LLVM_DEFINITIONS})

add_library(j2c SHARED src/lib.cpp)

llvm_map_components_to_libnames(llvm_libs support core)

target_link_libraries(j2c ${llvm_libs})
target_link_libraries(j2c JNI::JNI)