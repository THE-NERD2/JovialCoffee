package org.j2c.assembly.rules

import javassist.bytecode.Opcode
import org.j2c.assembly.NFDiv
import org.j2c.assembly.NFMul

@RuleContainer
object FMATH {
    val FMUL = Rule(Opcode.FMUL) { _, _, _, _, stack ->
        val v2 = stack.pop()
        val v1 = stack.pop()
        stack.add(NFMul(v1, v2))
    }
    val FDIV = Rule(Opcode.FDIV) { _, _, _, _, stack ->
        val v2 = stack.pop()
        val v1 = stack.pop()
        stack.add(NFDiv(v1, v2))
    }
}