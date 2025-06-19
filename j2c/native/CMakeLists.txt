cmake_minimum_required(VERSION 3.24)

project(j2c LANGUAGES C CXX)

message(STATUS "JAVA_HOME: $ENV{JAVA_HOME}")

find_package(LLVM REQUIRED CONFIG)
find_package(JNI REQUIRED)

include_directories(${LLVM_INCLUDE_DIRS})
add_definitions(${LLVM_DEFINITIONS})

add_library(j2c SHARED src/lib.cpp)

llvm_map_components_to_libnames(llvm_libs support core)

target_link_libraries(j2c ${llvm_libs})
target_link_libraries(j2c JNI::JNI)