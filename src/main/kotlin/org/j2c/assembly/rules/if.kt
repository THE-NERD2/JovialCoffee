package org.j2c.assembly.rules

import javassist.bytecode.Opcode

@RuleContainer
object IF {
    val IFEQ = Rule(Opcode.IFEQ) { instructions, pos, _, _, stack ->
        val v = stack.pop()
        val i = instructions.s16bitAt(pos + 1)
        stack.add("if($v == 0) goto ${pos + i}")
    }
    val IFNE = Rule(Opcode.IFNE) { instructions, pos, _, _, stack ->
        val v = stack.pop()
        val i = instructions.s16bitAt(pos + 1)
        stack.add("if($v != 0) goto ${pos + i}")
    }
    val IFGE = Rule(Opcode.IFGE) { instructions, pos, _, _, stack ->
        val i = instructions.s16bitAt(pos + 1)
        val v = stack.pop()
        stack.add("if($v >= 0) goto ${pos + i}")
    }
}

@RuleContainer
object IF_A {
    val IFNONNULL = Rule(Opcode.IFNONNULL) { instructions, pos, _, _, stack ->
        val v = stack.pop()
        val i = instructions.s16bitAt(pos + 1)
        stack.add("if($v != null) goto ${pos + i}")
    }
    val IF_ACMPEQ = Rule(Opcode.IF_ACMPEQ) { instructions, pos, _, _, stack ->
        val i = instructions.s16bitAt(pos + 1)
        val v2 = stack.pop()
        val v1 = stack.pop()
        stack.add("if($v1 == $v2) goto ${pos + i}")
    }
    val IF_ACMPNE = Rule(Opcode.IF_ACMPNE) { instructions, pos, _, _, stack ->
        val i = instructions.s16bitAt(pos + 1)
        val v2 = stack.pop()
        val v1 = stack.pop()
        stack.add("if($v1 != $v2) goto ${pos + i}")
    }
}