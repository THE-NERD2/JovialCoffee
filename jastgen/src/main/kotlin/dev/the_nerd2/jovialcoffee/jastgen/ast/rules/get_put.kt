@file:Suppress("unused")

package dev.the_nerd2.jovialcoffee.jastgen.ast.rules

import javassist.bytecode.Opcode
import dev.the_nerd2.jovialcoffee.jastgen.ast.NBoundAssignment
import dev.the_nerd2.jovialcoffee.jastgen.ast.NBoundReference
import dev.the_nerd2.jovialcoffee.jastgen.ast.NStaticAssignment
import dev.the_nerd2.jovialcoffee.jastgen.ast.NStaticReference
import dev.the_nerd2.jovialcoffee.jastgen.ast.findNClassByFullName
import dev.the_nerd2.jovialcoffee.jastgen.ast.rules.api.Rule
import dev.the_nerd2.jovialcoffee.jastgen.ast.rules.api.RuleContainer

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
        val className = findNClassByFullName(state.const.getFieldrefClassName(i)).cname
        val fld = "${className}_" + state.const.getFieldrefName(i)
        state.stack.add(NBoundReference(className, obj, fld))
    }
    val PUTFIELD = Rule(Opcode.PUTFIELD) { state ->
        val i = state.instructions.u16bitAt(state.pos + 1)
        val newV = state.stack.pop()
        val obj = state.stack.pop()
        val className = findNClassByFullName(state.const.getFieldrefClassName(i)).cname
        val fld = "${className}_" + state.const.getFieldrefName(i)
        state.stack.add(NBoundAssignment(className, obj, fld, newV))
    }
}