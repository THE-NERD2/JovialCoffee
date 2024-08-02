package org.j2c.assembly.rules

import javassist.bytecode.Opcode

@RuleContainer
object IF {
    val IFEQ = Rule(Opcode.IFEQ) { instructions, pos, _, _, stack ->
        val v = stack.pop()
        val i = instructions.s16bitAt(pos + 1)
        stack.add("if($v == 0)")
        GOTO.follow(instructions, pos, i, true)
    }
    val IFNE = Rule(Opcode.IFNE) { instructions, pos, _, _, stack ->
        val v = stack.pop()
        val i = instructions.s16bitAt(pos + 1)
        stack.add("if($v != 0)")
        GOTO.follow(instructions, pos, i, true)
    }
    val IFGE = Rule(Opcode.IFGE) { instructions, pos, _, _, stack ->
        val i = instructions.s16bitAt(pos + 1)
        val v = stack.pop()
        stack.add("if($v >= 0)")
        GOTO.follow(instructions, pos, i, true)
    }
}

@RuleContainer
object IF_A {
    val IFNONNULL = Rule(Opcode.IFNONNULL) { instructions, pos, _, _, stack ->
        val v = stack.pop()
        val i = instructions.s16bitAt(pos + 1)
        stack.add("if($v != null)")
        GOTO.follow(instructions, pos, i, true)
    }
    val IF_ACMPEQ = Rule(Opcode.IF_ACMPEQ) { instructions, pos, _, _, stack ->
        val i = instructions.s16bitAt(pos + 1)
        val v2 = stack.pop()
        val v1 = stack.pop()
        stack.add("if($v1 == $v2)")
        GOTO.follow(instructions, pos, i, true)
    }
    val IF_ACMPNE = Rule(Opcode.IF_ACMPNE) { instructions, pos, _, _, stack ->
        val i = instructions.s16bitAt(pos + 1)
        val v2 = stack.pop()
        val v1 = stack.pop()
        stack.add("if($v1 != $v2)")
        GOTO.follow(instructions, pos, i, true)
    }
}