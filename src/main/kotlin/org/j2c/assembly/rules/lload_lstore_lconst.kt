package org.j2c.assembly.rules

import javassist.bytecode.Opcode
import org.j2c.assembly.NReference

@RuleContainer
object LLOAD {
    val LLOAD_0 = Rule(Opcode.LLOAD_0) { _, _, _, vars, stack ->
        stack.add(NReference(vars[0] ?: "???"))
    }
    val LLOAD_2 = Rule(Opcode.LLOAD_2) { _, _, _, vars, stack ->
        stack.add(NReference(vars[2] ?: "???"))
    }
}