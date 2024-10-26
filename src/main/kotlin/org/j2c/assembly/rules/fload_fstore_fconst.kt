package org.j2c.assembly.rules

import javassist.bytecode.Opcode
import org.j2c.assembly.NFloat

@RuleContainer
object FCONST {
    val FCONST_2 = Rule(Opcode.FCONST_2) { _, _, _, _, stack ->
        stack.add(NFloat(2f))
    }
}