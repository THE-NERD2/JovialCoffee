package org.j2c.exceptions

class UnknownOpcodeException(msg: String): Exception("Unknown opcode: $msg")
class InfiniteLoopException: Exception("Code parsing ran into an infinite loop")