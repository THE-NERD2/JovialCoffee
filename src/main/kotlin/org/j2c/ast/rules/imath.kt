package org.j2c.ast.rules

import javassist.bytecode.Opcode
import org.j2c.ast.NAssignment
import org.j2c.ast.NBinOp
import org.j2c.ast.NInt
import org.j2c.ast.NReference
import org.j2c.ast.rules.api.Rule
import org.j2c.ast.rules.api.RuleContainer

@RuleContainer
object IMATH {
    val IADD = Rule(Opcode.IADD) { state ->
        val v2 = state.stack.pop()
        val v1 = state.stack.pop()
        state.stack.add(NBinOp("i", "+", v1, v2))
    }
    val ISUB = Rule(Opcode.ISUB) { state ->
        val v2 = state.stack.pop()
        val v1 = state.stack.pop()
        state.stack.add(NBinOp("i", "-", v1, v2))
    }
    val IMUL = Rule(Opcode.IMUL) { state ->
        val v2 = state.stack.pop()
        val v1 = state.stack.pop()
        state.stack.add(NBinOp("i", "*", v1, v2))
    }
    val IDIV = Rule(Opcode.IDIV) { state ->
        val v2 = state.stack.pop()
        val v1 = state.stack.pop()
        state.stack.add(NBinOp("i", "/", v1, v2))
    }
    val IREM = Rule(Opcode.IREM) { state ->
        val v2 = state.stack.pop()
        val v1 = state.stack.pop()
        state.stack.add(NBinOp("i", "%", v1, v2))
    }
    val IOR = Rule(Opcode.IOR) { state ->
        val v2 = state.stack.pop()
        val v1 = state.stack.pop()
        state.stack.add(NBinOp("i", "|", v1, v2))
    }
    val IAND = Rule(Opcode.IAND) { state ->
        val v2 = state.stack.pop()
        val v1 = state.stack.pop()
        state.stack.add(NBinOp("i", "&", v1, v2))
    }
    val IXOR = Rule(Opcode.IXOR) { state ->
        val v2 = state.stack.pop()
        val v1 = state.stack.pop()
        state.stack.add(NBinOp("i", "xor", v1, v2))
    }
    val IUSHR = Rule(Opcode.IUSHR) { state ->
        val v2 = state.stack.pop()
        val v1 = state.stack.pop()
        state.stack.add(NBinOp("i", "ushr", v1, v2))
    }
    val ISHR = Rule(Opcode.ISHR) { state ->
        val v2 = state.stack.pop()
        val v1 = state.stack.pop()
        state.stack.add(NBinOp("i", "shr", v1, v2))
    }
    val ISHL = Rule(Opcode.ISHL) { state ->
        val v2 = state.stack.pop()
        val v1 = state.stack.pop()
        state.stack.add(NBinOp("i", "shl", v1, v2))
    }

    val IINC = Rule(Opcode.IINC) { state ->
        val ref = state.instructions.byteAt(state.pos + 1)
        val v = state.instructions.byteAt(state.pos + 2)
        state.stack.add(NAssignment(state.vars[ref] ?: "???", NBinOp("i", "+", NReference(state.vars[ref] ?: "???"), NInt(v))))
    }
    val INEG = Rule(Opcode.INEG) { state ->
        val v = state.stack.pop()
        state.stack.add(NBinOp("i", "*", NInt(-1), v))
    }
}