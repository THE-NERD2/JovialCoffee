@file:Suppress("unused")

package dev.the_nerd2.jovialcoffee.jastgen.ast.rules

import javassist.bytecode.Opcode
import dev.the_nerd2.jovialcoffee.jastgen.ast.NBinOp
import dev.the_nerd2.jovialcoffee.jastgen.ast.rules.api.Rule
import dev.the_nerd2.jovialcoffee.jastgen.ast.rules.api.RuleContainer

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
    val LUSHR = Rule(Opcode.LUSHR) { state ->
        val v2 = state.stack.pop()
        val v1 = state.stack.pop()
        state.stack.add(NBinOp("l", "ushr", v1, v2))
    }
    val LSHR = Rule(Opcode.LSHR) { state ->
        val v2 = state.stack.pop()
        val v1 = state.stack.pop()
        state.stack.add(NBinOp("l", "shr", v1, v2))
    }
    val LSHL = Rule(Opcode.LSHL) { state ->
        val v2 = state.stack.pop()
        val v1 = state.stack.pop()
        state.stack.add(NBinOp("l", "shl", v1, v2))
    }
    val LOR = Rule(Opcode.LOR) { state ->
        val v2 = state.stack.pop()
        val v1 = state.stack.pop()
        state.stack.add(NBinOp("l", "|", v1, v2))
    }
    val LAND = Rule(Opcode.LAND) { state ->
        val v2 = state.stack.pop()
        val v1 = state.stack.pop()
        state.stack.add(NBinOp("l", "&", v1, v2))
    }
    val LXOR = Rule(Opcode.LXOR) { state ->
        val v2 = state.stack.pop()
        val v1 = state.stack.pop()
        state.stack.add(NBinOp("l", "xor", v1, v2))
    }
}