# J2C: Java bytecode to native executable

GraalVM native image doesn't seem to work for me, so I decided to make my own replacement.

## Features

> **Note:** Very early stages; not much functionality

- Printing high-level pseudocode with most opcodes
- Printing (to console and to file) LLVM IR (incomplete support)
- Recursive search for unknown classes
