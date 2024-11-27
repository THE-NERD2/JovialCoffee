package org.j2c.ast.rules

import javassist.bytecode.Opcode
import org.j2c.ast.NBinOp
import org.j2c.ast.rules.api.Rule
import org.j2c.ast.rules.api.RuleContainer

@RuleContainer
object LMATH {
    val LADD = Rule(Opcode.LADD) { state ->
        val v2 = state.stack.pop()
        val v1 = state.stack.pop()
        state.stack.add(NBinOp("l", "+", v1, v2))
    }
    val LSUB = Rule(Opcode.LSUB) { state ->
        val v2 = state.stack.pop()
        val v1 = state.stack.pop()
        state.stack.add(NBinOp("l", "-", v1, v2))
    }
    val LMUL = Rule(Opcode.LMUL) { state ->
        val v2 = state.stack.pop()
        val v1 = state.stack.pop()
        state.stack.add(NBinOp("l", "*", v1, v2))
    }
    val LDIV = Rule(Opcode.LDIV) { state ->
        val v2 = state.stack.pop()
        val v1 = state.stack.pop()
        state.stack.add(NBinOp("l", "/", v1, v2))
    }
    val LCMP = Rule(Opcode.LCMP) { state ->
        val v2 = state.stack.pop()
        val v1 = state.stack.pop()
        state.stack.add(NBinOp("l", "vs", v1, v2))
    }
}