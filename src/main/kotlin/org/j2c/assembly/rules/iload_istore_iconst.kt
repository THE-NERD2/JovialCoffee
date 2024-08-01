package org.j2c.assembly.rules

import javassist.bytecode.Opcode

@RuleContainer
object ILOAD {
    val ILOAD_1 = Rule(Opcode.ILOAD_1) { _, _, _, vars, stack ->
        stack.add(vars[1])
    }
}

@RuleContainer
object ISTORE {
    val ISTORE_1 = Rule(Opcode.ISTORE_1) { _, _, _, vars, stack ->
        val newV = stack.pop()
        vars[1] = "ivar1"
        stack.add("ivar1 = $newV")
    }
}

@RuleContainer
object ICONST {
    val ICONST_0 = Rule(Opcode.ICONST_0) { _, _, _, _, stack ->
        stack.add("0")
    }
    val ICONST_1 = Rule(Opcode.ICONST_1) { _, _, _, _, stack ->
        stack.add("1")
    }
    val ICONST_M1 = Rule(Opcode.ICONST_M1) { _, _, _, _, stack ->
        stack.add("-1")
    }
}