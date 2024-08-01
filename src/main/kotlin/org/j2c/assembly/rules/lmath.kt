package org.j2c.assembly.rules

import javassist.bytecode.Opcode

@RuleContainer
object LMATH {
    val LCMP = Rule(Opcode.LCMP) { _, _, _, _, stack ->
        val v2 = stack.pop()
        val v1 = stack.pop()
        stack.add("$v1 vs $v2")
    }
}