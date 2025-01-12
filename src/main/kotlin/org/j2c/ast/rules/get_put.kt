package org.j2c.ast.rules

import javassist.bytecode.Opcode
import org.j2c.ast.NBoundAssignment
import org.j2c.ast.NBoundReference
import org.j2c.ast.NStaticAssignment
import org.j2c.ast.NStaticReference
import org.j2c.ast.findNClassByFullName
import org.j2c.ast.rules.api.Rule
import org.j2c.ast.rules.api.RuleContainer

@RuleContainer
object STATIC {
    val GETSTATIC = Rule(Opcode.GETSTATIC) { state ->
        val i = state.instructions.u16bitAt(state.pos + 1)
        val fld = state.const.getFieldrefName(i)
        state.stack.add(NStaticReference(fld))
    }
    val PUTSTATIC = Rule(Opcode.PUTSTATIC) { state ->
        val i = state.instructions.u16bitAt(state.pos + 1)
        val fld = state.const.getFieldrefName(i)
        val newV = state.stack.pop()
        state.stack.add(NStaticAssignment(fld, newV))
    }
}

@RuleContainer
object FIELD {
    val GETFIELD = Rule(Opcode.GETFIELD) { state ->
        val i = state.instructions.u16bitAt(state.pos + 1)
        val obj = state.stack.pop()
        val fld = findNClassByFullName(state.const.getFieldrefClassName(i)).cname + "_" + state.const.getFieldrefName(i)
        state.stack.add(NBoundReference(obj, fld))
    }
    val PUTFIELD = Rule(Opcode.PUTFIELD) { state ->
        val i = state.instructions.u16bitAt(state.pos + 1)
        val newV = state.stack.pop()
        val obj = state.stack.pop()
        val fld = findNClassByFullName(state.const.getFieldrefClassName(i)).cname + "_" + state.const.getFieldrefName(i)
        state.stack.add(NBoundAssignment(obj, fld, newV))
    }
}