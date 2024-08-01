package org.j2c.assembly.rules

import javassist.bytecode.Opcode

@RuleContainer
object IMATH {
    val IADD = Rule(Opcode.IADD) { _, _, _, _, stack ->
        val v2 = stack.pop()
        val v1 = stack.pop()
        stack.add("$v1 + $v2")
    }
    val IMUL = Rule(Opcode.IMUL) { _, _, _, _, stack ->
        val v2 = stack.pop()
        val v1 = stack.pop()
        stack.add("$v1 * $v2")
    }
}