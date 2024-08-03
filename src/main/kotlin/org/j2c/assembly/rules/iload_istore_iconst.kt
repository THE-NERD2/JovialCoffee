package org.j2c.assembly.rules

import javassist.bytecode.Opcode
import org.j2c.assembly.NAssignment
import org.j2c.assembly.NInt
import org.j2c.assembly.NReference

@RuleContainer
object ILOAD {
    val ILOAD_1 = Rule(Opcode.ILOAD_1) { _, _, _, vars, stack ->
        stack.add(NReference(vars[1] ?: "???"))
    }
}

@RuleContainer
object ISTORE {
    val ISTORE_1 = Rule(Opcode.ISTORE_1) { _, _, _, vars, stack ->
        val newV = stack.pop()
        vars[1] = "ivar1"
        stack.add(NAssignment("ivar1", newV))
    }
}

@RuleContainer
object ICONST {
    val ICONST_0 = Rule(Opcode.ICONST_0) { _, _, _, _, stack ->
        stack.add(NInt(0))
    }
    val ICONST_1 = Rule(Opcode.ICONST_1) { _, _, _, _, stack ->
        stack.add(NInt(1))
    }
    val ICONST_M1 = Rule(Opcode.ICONST_M1) { _, _, _, _, stack ->
        stack.add(NInt(-1))
    }
}