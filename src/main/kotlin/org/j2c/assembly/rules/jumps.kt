package org.j2c.assembly.rules

import javassist.bytecode.Opcode

@RuleContainer
object JUMPS {
    val GOTO = Rule(Opcode.GOTO) { instructions, pos, _, _, stack ->
        val i = instructions.s16bitAt(pos + 1)
        stack.add("goto $i")
    }
    val RETURN = Rule(Opcode.RETURN) { _, _, _, _, stack ->
        stack.add("return")
    }
    val ARETURN = Rule(Opcode.ARETURN) { _, _, _, _, stack ->
        val v = stack.pop()
        stack.add("return $v")
    }
    val IRETURN = Rule(Opcode.IRETURN) { _, _, _, _, stack ->
        val v = stack.pop()
        stack.add("return $v")
    }
    val ATHROW = Rule(Opcode.ATHROW) { _, _, _, _, stack ->
        val exception = stack.pop()
        stack.add("throw $exception")
    }
}