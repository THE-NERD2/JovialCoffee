@file:Suppress("unused")

package dev.the_nerd2.jovialcoffee.jastgen.ast.rules

import javassist.bytecode.Opcode
import dev.the_nerd2.jovialcoffee.jastgen.ast.NAssignment
import dev.the_nerd2.jovialcoffee.jastgen.ast.NDouble
import dev.the_nerd2.jovialcoffee.jastgen.ast.NReference
import dev.the_nerd2.jovialcoffee.jastgen.ast.rules.api.Rule
import dev.the_nerd2.jovialcoffee.jastgen.ast.rules.api.RuleContainer

@RuleContainer
object DLOAD {
    val DLOAD = Rule(Opcode.DLOAD) { state ->
        val i = state.instructions.byteAt(state.pos + 1)
        state.stack.add(NReference(state.vars[i] ?: "???"))
    }
    val DLOAD_0 = Rule(Opcode.DLOAD_0) { state ->
        state.stack.add(NReference(state.vars[0] ?: "???"))
    }
    val DLOAD_1 = Rule(Opcode.DLOAD_1) { state ->
        state.stack.add(NReference(state.vars[1] ?: "???"))
    }
    val DLOAD_2 = Rule(Opcode.DLOAD_2) { state ->
        state.stack.add(NReference(state.vars[2] ?: "???"))
    }
    val DLOAD_3 = Rule(Opcode.DLOAD_3) { state ->
        state.stack.add(NReference(state.vars[3] ?: "???"))
    }
}

@RuleContainer
object DSTORE {
    val DSTORE = Rule(Opcode.DSTORE) { state ->
        val i = state.instructions.byteAt(state.pos + 1)
        val newV = state.stack.pop()
        state.vars[i]  = "dvar$i"
        state.stack.add(NAssignment("dvar$i", newV))
    }
    val DSTORE_0 = Rule(Opcode.DSTORE_0) { state ->
        val newV = state.stack.pop()
        state.vars[0] = "dvar0"
        state.stack.add(NAssignment("dvar0", newV))
    }
    val DSTORE_1 = Rule(Opcode.DSTORE_1) { state ->
        val newV = state.stack.pop()
        state.vars[1] = "dvar1"
        state.stack.add(NAssignment("dvar1", newV))
    }
    val DSTORE_3 = Rule(Opcode.DSTORE_3) { state ->
        val newV = state.stack.pop()
        state.vars[3] = "dvar3"
        state.stack.add(NAssignment("dvar3", newV))
    }
}

@RuleContainer
object DCONST {
    val DCONST_0 = Rule(Opcode.DCONST_0) { state ->
        state.stack.add(NDouble(0.0))
    }
    val DCONST_1 = Rule(Opcode.DCONST_1) { state ->
        state.stack.add(NDouble(1.0))
    }
}