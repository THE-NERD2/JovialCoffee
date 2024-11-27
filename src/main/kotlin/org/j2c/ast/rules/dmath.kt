package org.j2c.ast.rules

import javassist.bytecode.Opcode
import org.j2c.ast.NBinOp
import org.j2c.ast.rules.api.Rule
import org.j2c.ast.rules.api.RuleContainer

@RuleContainer
object DMATH {
    val DADD = Rule(Opcode.DADD) { state ->
        val v2 = state.stack.pop()
        val v1 = state.stack.pop()
        state.stack.add(NBinOp("d", "+", v1, v2))
    }
    val DSUB = Rule(Opcode.DSUB) { state ->
        val v2 = state.stack.pop()
        val v1 = state.stack.pop()
        state.stack.add(NBinOp("d", "-", v1, v2))
    }
    val DMUL = Rule(Opcode.DMUL) { state ->
        val v2 = state.stack.pop()
        val v1 = state.stack.pop()
        state.stack.add(NBinOp("d", "*", v1, v2))
    }
    val DDIV = Rule(Opcode.DDIV) { state ->
        val v2 = state.stack.pop()
        val v1 = state.stack.pop()
        state.stack.add(NBinOp("d", "/", v1, v2))
    }
}