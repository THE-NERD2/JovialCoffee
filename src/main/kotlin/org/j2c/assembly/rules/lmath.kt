package org.j2c.assembly.rules

import javassist.bytecode.Opcode
import org.j2c.assembly.NLCmp

@RuleContainer
object LMATH {
    val LCMP = Rule(Opcode.LCMP) { _, _, _, _, stack ->
        val v2 = stack.pop()
        val v1 = stack.pop()
        stack.add(NLCmp(v1, v2))
    }
}