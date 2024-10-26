package org.j2c.assembly.rules

import javassist.bytecode.Opcode
import org.j2c.assembly.NFloat
import org.j2c.assembly.NReference

@RuleContainer
object FLOAD {
    val FLOAD = Rule(Opcode.FLOAD) { instructions, pos, _, vars, stack ->
        val i = instructions.byteAt(pos + 1)
        stack.add(NReference(vars[i] ?: "???"))
    }
}

@RuleContainer
object FCONST {
    val FCONST_2 = Rule(Opcode.FCONST_2) { _, _, _, _, stack ->
        stack.add(NFloat(2f))
    }
}