package org.j2c.assembly.rules

import javassist.bytecode.Opcode
import org.j2c.assembly.NAssignment
import org.j2c.assembly.NFloat
import org.j2c.assembly.NReference

@RuleContainer
object FLOAD {
    val FLOAD = Rule(Opcode.FLOAD) { instructions, pos, _, vars, stack ->
        val i = instructions.byteAt(pos + 1)
        stack.add(NReference(vars[i] ?: "???"))
    }
    val FLOAD_3 = Rule(Opcode.FLOAD_3) { _, _, _, vars, stack ->
        stack.add(NReference(vars[3] ?: "???"))
    }
}

@RuleContainer
object FSTORE {
    val FSTORE = Rule(Opcode.FSTORE) { instructions, pos, _, vars, stack ->
        val i = instructions.byteAt(pos + 1)
        val newV = stack.pop()
        vars[i] = "fvar$i"
        stack.add(NAssignment("fvar$i", newV))
    }
    val FSTORE_3 = Rule(Opcode.FSTORE_3) { _, _, _, vars, stack ->
        val newV = stack.pop()
        vars[3] = "fvar3"
        stack.add(NAssignment("fvar3", newV))
    }
}

@RuleContainer
object FCONST {
    val FCONST_0 = Rule(Opcode.FCONST_0) { _, _, _, _, stack ->
        stack.add(NFloat(0f))
    }
    val FCONST_1 = Rule(Opcode.FCONST_1) { _, _, _, _, stack ->
        stack.add(NFloat(1f))
    }
    val FCONST_2 = Rule(Opcode.FCONST_2) { _, _, _, _, stack ->
        stack.add(NFloat(2f))
    }
}