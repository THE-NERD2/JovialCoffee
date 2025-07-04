cmake_minimum_required(VERSION 3.24)

project(kna LANGUAGES C CXX)

message(STATUS "JAVA_HOME: $ENV{JAVA_HOME}")

find_package(JNI REQUIRED)
add_library(kna SHARED src/lib.cpp)

target_link_libraries(kna JNI::JNI)