@file:Suppress("unused")

package dev.the_nerd2.jovialcoffee.j2c.ast.rules

import javassist.bytecode.Opcode
import dev.the_nerd2.jovialcoffee.j2c.ast.NAssignment
import dev.the_nerd2.jovialcoffee.j2c.ast.NInt
import dev.the_nerd2.jovialcoffee.j2c.ast.NReference
import dev.the_nerd2.jovialcoffee.j2c.ast.rules.api.Rule
import dev.the_nerd2.jovialcoffee.j2c.ast.rules.api.RuleContainer

@RuleContainer
object ILOAD {
    val ILOAD = Rule(Opcode.ILOAD) { state ->
        val i = state.instructions.byteAt(state.pos + 1)
        state.stack.add(NReference(state.vars[i] ?: "???"))
    }
    val ILOAD_0 = Rule(Opcode.ILOAD_0) { state ->
        state.stack.add(NReference(state.vars[0] ?: "???"))
    }
    val ILOAD_1 = Rule(Opcode.ILOAD_1) { state ->
        state.stack.add(NReference(state.vars[1] ?: "???"))
    }
    val ILOAD_2 = Rule(Opcode.ILOAD_2) { state ->
        state.stack.add(NReference(state.vars[2] ?: "???"))
    }
    val ILOAD_3 = Rule(Opcode.ILOAD_3) { state ->
        state.stack.add(NReference(state.vars[3] ?: "???"))
    }
}

@RuleContainer
object ISTORE {
    val ISTORE = Rule(Opcode.ISTORE) { state ->
        val i = state.instructions.byteAt(state.pos + 1)
        val newV = state.stack.pop()
        state.vars[i] = "ivar$i"
        state.stack.add(NAssignment("ivar$i", newV))
    }
    val ISTORE_0 = Rule(Opcode.ISTORE_0) { state ->
        val newV = state.stack.pop()
        state.vars[0] = "ivar0"
        state.stack.add(NAssignment("ivar0", newV))
    }
    val ISTORE_1 = Rule(Opcode.ISTORE_1) { state ->
        val newV = state.stack.pop()
        state.vars[1] = "ivar1"
        state.stack.add(NAssignment("ivar1", newV))
    }
    val ISTORE_2 = Rule(Opcode.ISTORE_2) { state ->
        val newV = state.stack.pop()
        state.vars[2] = "ivar2"
        state.stack.add(NAssignment("ivar2", newV))
    }
    val ISTORE_3 = Rule(Opcode.ISTORE_3) { state ->
        val newV = state.stack.pop()
        state.vars[3] = "ivar3"
        state.stack.add(NAssignment("ivar3", newV))
    }
}

@RuleContainer
object ICONST {
    val ICONST_0 = Rule(Opcode.ICONST_0) { state ->
        state.stack.add(NInt(0))
    }
    val ICONST_1 = Rule(Opcode.ICONST_1) { state ->
        state.stack.add(NInt(1))
    }
    val ICONST_2 = Rule(Opcode.ICONST_2) { state ->
        state.stack.add(NInt(2))
    }
    val ICONST_3 = Rule(Opcode.ICONST_3) { state ->
        state.stack.add(NInt(3))
    }
    val ICONST_4 = Rule(Opcode.ICONST_4) { state ->
        state.stack.add(NInt(4))
    }
    val ICONST_5 = Rule(Opcode.ICONST_5) { state ->
        state.stack.add(NInt(5))
    }
    val ICONST_M1 = Rule(Opcode.ICONST_M1) { state ->
        state.stack.add(NInt(-1))
    }
}