package org.j2c.assembly.rules

import javassist.bytecode.Opcode
import org.j2c.assembly.NFAdd
import org.j2c.assembly.NFDiv
import org.j2c.assembly.NFMul
import org.j2c.assembly.NFSub

@RuleContainer
object FMATH {
    val FADD = Rule(Opcode.FADD) { _, _, _, _, stack ->
        val v2 = stack.pop()
        val v1 = stack.pop()
        stack.add(NFAdd(v1, v2))
    }
    val FSUB = Rule(Opcode.FSUB) { _, _, _, _, stack ->
        val v2 = stack.pop()
        val v1 = stack.pop()
        stack.add(NFSub(v1, v2))
    }
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