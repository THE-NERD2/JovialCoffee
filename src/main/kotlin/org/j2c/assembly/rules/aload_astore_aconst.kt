package org.j2c.assembly.rules

import javassist.bytecode.Opcode

@RuleContainer
object ALOAD {
    val ALOAD_0 = Rule(Opcode.ALOAD_0) { _, _, _, vars, stack ->
        stack.add(vars[0])
    }
    val ALOAD_1 = Rule(Opcode.ALOAD_1) { _, _, _, vars, stack ->
        stack.add(vars[1])
    }
    val ALOAD_2 = Rule(Opcode.ALOAD_2) { _, _, _, vars, stack ->
        stack.add(vars[2])
    }
    val ALOAD_3 = Rule(Opcode.ALOAD_3) { _, _, _, vars, stack ->
        stack.add(vars[3])
    }
}

@RuleContainer
object ASTORE {
    val ASTORE_1 = Rule(Opcode.ASTORE_1) { _, _, _, vars, stack ->
        val newV = stack.pop()
        vars[1] = "avar1"
        stack.add("avar1 = $newV")
    }
    val ASTORE_2 = Rule(Opcode.ASTORE_2) { _, _, _, vars, stack ->
        val newV = stack.pop()
        vars[2] = "avar2"
        stack.add("avar2 = $newV")
    }
}

@RuleContainer
object ACONST {
    val ACONST_NULL = Rule(Opcode.ACONST_NULL) { _, _, _, _, stack ->
        stack.add("null")
    }
}