package org.j2c.assembly.rules

import javassist.bytecode.Opcode
import org.j2c.assembly.NDAdd
import org.j2c.assembly.NDDiv
import org.j2c.assembly.NDMul
import org.j2c.assembly.NDSub

@RuleContainer
object DMATH {
    val DADD = Rule(Opcode.DADD) { _, _, _, _, stack ->
        val v2 = stack.pop()
        val v1 = stack.pop()
        stack.add(NDAdd(v1, v2))
    }
    val DSUB = Rule(Opcode.DSUB) { _, _, _, _, stack ->
        val v2 = stack.pop()
        val v1 = stack.pop()
        stack.add(NDSub(v1, v2))
    }
    val DMUL = Rule(Opcode.DMUL) { _, _, _, _, stack ->
        val v2 = stack.pop()
        val v1 = stack.pop()
        stack.add(NDMul(v1, v2))
    }
    val DDIV = Rule(Opcode.DDIV) { _, _, _, _, stack ->
        val v2 = stack.pop()
        val v1 = stack.pop()
        stack.add(NDDiv(v1, v2))
    }
}