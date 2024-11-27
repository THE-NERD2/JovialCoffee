package org.j2c.ast.rules

import javassist.bytecode.Opcode
import org.j2c.ast.NBinOp
import org.j2c.ast.rules.api.Rule
import org.j2c.ast.rules.api.RuleContainer

@RuleContainer
object LMATH {
    val LADD = Rule(Opcode.LADD) { _, _, _, _, stack ->
        val v2 = stack.pop()
        val v1 = stack.pop()
        stack.add(NBinOp("l", "+", v1, v2))
    }
    val LSUB = Rule(Opcode.LSUB) { _, _, _, _, stack ->
        val v2 = stack.pop()
        val v1 = stack.pop()
        stack.add(NBinOp("l", "-", v1, v2))
    }
    val LMUL = Rule(Opcode.LMUL) { _, _, _, _, stack ->
        val v2 = stack.pop()
        val v1 = stack.pop()
        stack.add(NBinOp("l", "*", v1, v2))
    }
    val LDIV = Rule(Opcode.LDIV) { _, _, _, _, stack ->
        val v2 = stack.pop()
        val v1 = stack.pop()
        stack.add(NBinOp("l", "/", v1, v2))
    }
    val LCMP = Rule(Opcode.LCMP) { _, _, _, _, stack ->
        val v2 = stack.pop()
        val v1 = stack.pop()
        stack.add(NBinOp("l", "vs", v1, v2))
    }
}