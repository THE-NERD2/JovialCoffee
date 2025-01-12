package org.j2c.ast.rules

import javassist.bytecode.Opcode
import org.j2c.ast.NAssignment
import org.j2c.ast.NLong
import org.j2c.ast.NReference
import org.j2c.ast.rules.api.Rule
import org.j2c.ast.rules.api.RuleContainer

@RuleContainer
object LLOAD {
    val LLOAD = Rule(Opcode.LLOAD) { state ->
        val i = state.instructions.byteAt(state.pos + 1)
        state.stack.add(NReference(state.vars[i] ?: "???"))
    }
    val LLOAD_0 = Rule(Opcode.LLOAD_0) { state ->
        state.stack.add(NReference(state.vars[0] ?: "???"))
    }
    val LLOAD_1 = Rule(Opcode.LLOAD_1) { state ->
        state.stack.add(NReference(state.vars[1] ?: "???"))
    }
    val LLOAD_2 = Rule(Opcode.LLOAD_2) { state ->
        state.stack.add(NReference(state.vars[2] ?: "???"))
    }
    val LLOAD_3 = Rule(Opcode.LLOAD_3) { state ->
        state.stack.add(NReference(state.vars[3] ?: "???"))
    }
}

@RuleContainer
object LSTORE {
    val LSTORE = Rule(Opcode.LSTORE) { state ->
        val i = state.instructions.byteAt(state.pos + 1)
        val newV = state.stack.pop()
        state.vars[i] =  "lvar$i"
        state.stack.add(NAssignment("lvar$i", newV))
    }
    val LSTORE_0 = Rule(Opcode.LSTORE_0) { state ->
        val newV = state.stack.pop()
        state.vars[0] = "lvar0"
        state.stack.add(NAssignment("lvar0", newV))
    }
    val LSTORE_1 = Rule(Opcode.LSTORE_1) { state ->
        val newV = state.stack.pop()
        state.vars[1] = "lvar1"
        state.stack.add(NAssignment("lvar1", newV))
    }
    val LSTORE_2 = Rule(Opcode.LSTORE_2) { state ->
        val newV = state.stack.pop()
        state.vars[1] = "lvar2"
        state.stack.add(NAssignment("lvar2", newV))
    }
    val LSTORE_3 = Rule(Opcode.LSTORE_3) { state ->
        val newV = state.stack.pop()
        state.vars[1] = "lvar3"
        state.stack.add(NAssignment("lvar3", newV))
    }
}

@RuleContainer
object LCONST {
    val LCONST_0 = Rule(Opcode.LCONST_0) { state ->
        state.stack.add(NLong(0L))
    }
    val LCONST_1 = Rule(Opcode.LCONST_1) { state ->
        state.stack.add(NLong(1L))
    }
}