@file:Suppress("unused")

package dev.the_nerd2.jovialcoffee.jastgen.ast.rules

import javassist.bytecode.Opcode
import dev.the_nerd2.jovialcoffee.jastgen.ast.NCall
import dev.the_nerd2.jovialcoffee.jastgen.ast.NStaticCall
import dev.the_nerd2.jovialcoffee.jastgen.ast.Node
import dev.the_nerd2.jovialcoffee.jastgen.ast.findNClassByFullName
import dev.the_nerd2.jovialcoffee.jastgen.ast.rules.api.Rule
import dev.the_nerd2.jovialcoffee.jastgen.ast.rules.api.RuleContainer
import dev.the_nerd2.jovialcoffee.jastgen.getargc

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