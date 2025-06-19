@file:Suppress("unused")

package dev.the_nerd2.jovialcoffee.j2c.ast.rules

import javassist.bytecode.Opcode
import dev.the_nerd2.jovialcoffee.j2c.ast.NBinOp
import dev.the_nerd2.jovialcoffee.j2c.ast.rules.api.Rule
import dev.the_nerd2.jovialcoffee.j2c.ast.rules.api.RuleContainer

@RuleContainer
object FMATH {
    val FADD = Rule(Opcode.FADD) { state ->
        val v2 = state.stack.pop()
        val v1 = state.stack.pop()
        state.stack.add(NBinOp("f", "+", v1, v2))
    }
    val FSUB = Rule(Opcode.FSUB) { state ->
        val v2 = state.stack.pop()
        val v1 = state.stack.pop()
        state.stack.add(NBinOp("f", "-", v1, v2))
    }
    val FMUL = Rule(Opcode.FMUL) { state ->
        val v2 = state.stack.pop()
        val v1 = state.stack.pop()
        state.stack.add(NBinOp("f", "*", v1, v2))
    }
    val FDIV = Rule(Opcode.FDIV) { state ->
        val v2 = state.stack.pop()
        val v1 = state.stack.pop()
        state.stack.add(NBinOp("f", "/", v1, v2))
    }
    val FCMPG = Rule(Opcode.FCMPG) { state ->
        val v2 = state.stack.pop()
        val v1 = state.stack.pop()
        state.stack.add(NBinOp("f", "cmpg", v1, v2))
    }
}