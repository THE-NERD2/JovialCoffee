@file:Suppress("unused")

package dev.the_nerd2.jovialcoffee.jastgen.ast.rules

import javassist.bytecode.Opcode
import dev.the_nerd2.jovialcoffee.jastgen.ast.NBinOp
import dev.the_nerd2.jovialcoffee.jastgen.ast.rules.api.Rule
import dev.the_nerd2.jovialcoffee.jastgen.ast.rules.api.RuleContainer

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
    val DCMPL = Rule(Opcode.DCMPL) { state ->
        val v2 = state.stack.pop()
        val v1 = state.stack.pop()
        state.stack.add(NBinOp("d", "cmpl", v1, v2))
    }
}