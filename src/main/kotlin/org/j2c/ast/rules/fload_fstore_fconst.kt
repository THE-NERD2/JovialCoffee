package org.j2c.ast.rules

import javassist.bytecode.Opcode
import org.j2c.ast.NAssignment
import org.j2c.ast.NFloat
import org.j2c.ast.NReference
import org.j2c.ast.rules.api.Rule
import org.j2c.ast.rules.api.RuleContainer

@RuleContainer
object FLOAD {
    val FLOAD = Rule(Opcode.FLOAD) { state ->
        val i = state.instructions.byteAt(state.pos + 1)
        state.stack.add(NReference(state.vars[i] ?: "???"))
    }
    val FLOAD_0 = Rule(Opcode.FLOAD_0) { state ->
        state.stack.add(NReference(state.vars[0] ?: "???"))
    }
    val FLOAD_1 = Rule(Opcode.FLOAD_1) { state ->
        state.stack.add(NReference(state.vars[1] ?: "???"))
    }
    val FLOAD_2 = Rule(Opcode.FLOAD_2) { state ->
        state.stack.add(NReference(state.vars[2] ?: "???"))
    }
    val FLOAD_3 = Rule(Opcode.FLOAD_3) { state ->
        state.stack.add(NReference(state.vars[3] ?: "???"))
    }
}

@RuleContainer
object FSTORE {
    val FSTORE = Rule(Opcode.FSTORE) { state ->
        val i = state.instructions.byteAt(state.pos + 1)
        val newV = state.stack.pop()
        state.vars[i] = "fvar$i"
        state.stack.add(NAssignment("fvar$i", newV))
    }
    val FSTORE_0 = Rule(Opcode.FSTORE_0) { state ->
        val newV = state.stack.pop()
        state.vars[0] = "fvar0"
        state.stack.add(NAssignment("fvar0", newV))
    }
    val FSTORE_1 = Rule(Opcode.FSTORE_1) { state ->
        val newV = state.stack.pop()
        state.vars[1] = "fvar1"
        state.stack.add(NAssignment("fvar1", newV))
    }
    val FSTORE_2 = Rule(Opcode.FSTORE_2) { state ->
        val newV = state.stack.pop()
        state.vars[2] = "fvar2"
        state.stack.add(NAssignment("fvar2", newV))
    }
    val FSTORE_3 = Rule(Opcode.FSTORE_3) { state ->
        val newV = state.stack.pop()
        state.vars[3] = "fvar3"
        state.stack.add(NAssignment("fvar3", newV))
    }
}

@RuleContainer
object FCONST {
    val FCONST_0 = Rule(Opcode.FCONST_0) { state ->
        state.stack.add(NFloat(0f))
    }
    val FCONST_1 = Rule(Opcode.FCONST_1) { state ->
        state.stack.add(NFloat(1f))
    }
    val FCONST_2 = Rule(Opcode.FCONST_2) { state ->
        state.stack.add(NFloat(2f))
    }
}