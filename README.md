# JovialCoffee

JovialCoffee is a bunch of tools for interacting with JVM and native code. Many tools are
designed to work on the boundary between the JVM and assembly code, such as J2C, which
converts JVM code completely into native code.

## Individual tools overview

### J2C: Java bytecode to native executable

> **Note:** Very early stages; not much functionality
> 
> Currently, J2C can only convert a program to LLVM IR, and it is still very incomplete.

GraalVM native image doesn't seem to work for me, so I decided to make my own replacement.
This tool takes JVM code, usually in a .jar file, and converts it into native code, such
as an executable or a shared library.

### Jastgen: API for JVM decompilation

This converts a JVM program into an Abstract Syntax Tree, or AST. An AST is a data structure
that stores how a program works. The AST is similar to Java reflection objects--it stores
all the classes, their fields and their methods, and the bodies of those methods.
