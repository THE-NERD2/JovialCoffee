package org.j2c.exceptions

class UnknownOpcodeException(msg: String): Exception("Unknown opcode: $msg")