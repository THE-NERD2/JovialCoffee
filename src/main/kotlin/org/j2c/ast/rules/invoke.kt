package org.j2c.ast.rules

import javassist.bytecode.Opcode
import org.j2c.ast.NCall
import org.j2c.ast.NStaticCall
import org.j2c.ast.Node
import org.j2c.ast.findNClassByFullName
import org.j2c.ast.rules.api.Rule
import org.j2c.ast.rules.api.RuleContainer
import org.j2c.getargc

@RuleContainer
object INVOKE_normal {
    val INVOKESTATIC = Rule(Opcode.INVOKESTATIC) { state ->
        val i = state.instructions.u16bitAt(state.pos + 1)
        val method = findNClassByFullName(state.const.getMethodrefClassName(i)).cname + "_" + state.const.getMethodrefName(i)
        val desc = state.const.getMethodrefType(i)

        val argc = getargc(desc)
        val args = arrayOfNulls<Node>(argc)
        for (j in argc - 1 downTo 0) {
            args[j] = state.stack.pop()
        }

        state.stack.add(NStaticCall(method, ArrayList(args.map { it!! })))
    }
    val INVOKEINTERFACE = Rule(Opcode.INVOKEINTERFACE) { state ->
        val i = state.instructions.u16bitAt(state.pos + 1)
        val method = findNClassByFullName(state.const.getInterfaceMethodrefClassName(i)).cname + "_" + state.const.getInterfaceMethodrefName(i)
        val desc = state.const.getInterfaceMethodrefType(i)

        val argc = getargc(desc)
        val args = arrayOfNulls<Node>(argc)
        for (j in argc - 1 downTo 0) {
            args[j] = state.stack.pop()
        }
        val obj = state.stack.pop()

        state.stack.add(NCall(obj, method, ArrayList(args.map { it!! })))
    }
    val INVOKEVIRTUAL = Rule(Opcode.INVOKEVIRTUAL) { state ->
        val i = state.instructions.u16bitAt(state.pos + 1)
        val method = findNClassByFullName(state.const.getMethodrefClassName(i)).cname + "_" + state.const.getMethodrefName(i)
        val desc = state.const.getInterfaceMethodrefType(i)

        val argc = getargc(desc)
        val args = arrayOfNulls<Node>(argc)
        for (j in argc - 1 downTo 0) {
            args[j] = state.stack.pop()
        }
        val obj = state.stack.pop()

        state.stack.add(NCall(obj, method, ArrayList(args.map { it!! })))
    }
}

@RuleContainer
object INVOKE_strange {
    val INVOKESPECIAL = Rule(Opcode.INVOKESPECIAL) { state ->
        val i = state.instructions.u16bitAt(state.pos + 1)
        val method = findNClassByFullName(state.const.getMethodrefClassName(i)).cname + "_" + state.const.getMethodrefName(i)
        val desc = state.const.getInterfaceMethodrefType(i)

        val argc = getargc(desc)
        val args = arrayOfNulls<Node>(argc)
        for (j in argc - 1 downTo 0) {
            args[j] = state.stack.pop()
        }
        val obj = state.stack.pop()

        state.stack.add(NCall(obj, method, ArrayList(args.map { it!! })))
    }
    //val INVOKEDYNAMIC = Rule(Opcode.INVOKEDYNAMIC) {... TODO
}