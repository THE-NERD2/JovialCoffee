@file:Suppress("unused")

package dev.the_nerd2.jovialcoffee.jastgen.ast.rules

import javassist.bytecode.Opcode
import dev.the_nerd2.jovialcoffee.jastgen.ast.NAssignment
import dev.the_nerd2.jovialcoffee.jastgen.ast.NNull
import dev.the_nerd2.jovialcoffee.jastgen.ast.NReference
import dev.the_nerd2.jovialcoffee.jastgen.ast.rules.api.Rule
import dev.the_nerd2.jovialcoffee.jastgen.ast.rules.api.RuleContainer

@RuleContainer
object ALOAD {
    val ALOAD = Rule(Opcode.ALOAD) { state ->
        val i = state.instructions.byteAt(state.pos + 1)
        state.stack.add(NReference(state.vars[i] ?: "???"))
    }
    val ALOAD_0 = Rule(Opcode.ALOAD_0) { state ->
        state.stack.add(NReference(state.vars[0] ?: "???"))
    }
    val ALOAD_1 = Rule(Opcode.ALOAD_1) { state ->
        state.stack.add(NReference(state.vars[1] ?: "???"))
    }
    val ALOAD_2 = Rule(Opcode.ALOAD_2) { state ->
        state.stack.add(NReference(state.vars[2] ?: "???"))
    }
    val ALOAD_3 = Rule(Opcode.ALOAD_3) { state ->
        state.stack.add(NReference(state.vars[3] ?: "???"))
    }
}

@RuleContainer
object ASTORE {
    val ASTORE = Rule(Opcode.ASTORE) { state ->
        val i = state.instructions.byteAt(state.pos + 1)
        val newV = state.stack.pop()
        state.vars[i] = "avar$i"
        state.stack.add(NAssignment("avar$i", newV))
    }
    val ASTORE_0 = Rule(Opcode.ASTORE_0) { state ->
        val newV = state.stack.pop()
        state.vars[1] = "avar0"
        state.stack.add(NAssignment("avar0", newV))
    }
    val ASTORE_1 = Rule(Opcode.ASTORE_1) { state ->
        val newV = state.stack.pop()
        state.vars[1] = "avar1"
        state.stack.add(NAssignment("avar1", newV))
    }
    val ASTORE_2 = Rule(Opcode.ASTORE_2) { state ->
        val newV = state.stack.pop()
        state.vars[2] = "avar2"
        state.stack.add(NAssignment("avar2", newV))
    }
    val ASTORE_3 = Rule(Opcode.ASTORE_3) { state ->
        val newV = state.stack.pop()
        state.vars[3] = "avar3"
        state.stack.add(NAssignment("avar3", newV))
    }
}

@RuleContainer
object ACONST {
    val ACONST_NULL = Rule(Opcode.ACONST_NULL) { state ->
        state.stack.add(NNull())
    }
}