package org.j2c.assembly.rules

import javassist.bytecode.Opcode
import org.j2c.assembly.NLAdd
import org.j2c.assembly.NLCmp
import org.j2c.assembly.NLMul

@RuleContainer
object LMATH {
    val LADD = Rule(Opcode.LADD) { _, _, _, _, stack ->
        val v2 = stack.pop()
        val v1 = stack.pop()
        stack.add(NLAdd(v1, v2))
    }
    val LMUL = Rule(Opcode.LMUL) { _, _, _, _, stack ->
        val v2 = stack.pop()
        val v1 = stack.pop()
        stack.add(NLMul(v1, v2))
    }
    val LCMP = Rule(Opcode.LCMP) { _, _, _, _, stack ->
        val v2 = stack.pop()
        val v1 = stack.pop()
        stack.add(NLCmp(v1, v2))
    }
}