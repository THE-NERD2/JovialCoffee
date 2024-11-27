package org.j2c.ast.rules

import javassist.bytecode.Opcode
import org.j2c.ast.NBinOp
import org.j2c.ast.rules.api.Rule
import org.j2c.ast.rules.api.RuleContainer

@RuleContainer
object IMATH {
    val IADD = Rule(Opcode.IADD) { state ->
        val v2 = state.stack.pop()
        val v1 = state.stack.pop()
        state.stack.add(NBinOp("i", "+", v1, v2))
    }
    val ISUB = Rule(Opcode.ISUB) { state ->
        val v2 = state.stack.pop()
        val v1 = state.stack.pop()
        state.stack.add(NBinOp("i", "-", v1, v2))
    }
    val IMUL = Rule(Opcode.IMUL) { state ->
        val v2 = state.stack.pop()
        val v1 = state.stack.pop()
        state.stack.add(NBinOp("i", "*", v1, v2))
    }
    val IDIV = Rule(Opcode.IDIV) { state ->
        val v2 = state.stack.pop()
        val v1 = state.stack.pop()
        state.stack.add(NBinOp("i", "/", v1, v2))
    }
}