package org.j2c.assembly.rules

import javassist.bytecode.Opcode
import org.j2c.assembly.NDouble
import org.j2c.assembly.NReference

@RuleContainer
object DLOAD {
    val DLOAD_0 = Rule(Opcode.DLOAD_0) { _, _, _, vars, stack ->
        stack.add(NReference(vars[0] ?: "???"))
    }
}

@RuleContainer
object DCONST {
    val DCONST_1 = Rule(Opcode.DCONST_1) { _, _, _, _, stack ->
        stack.add(NDouble(1.0))
    }
}