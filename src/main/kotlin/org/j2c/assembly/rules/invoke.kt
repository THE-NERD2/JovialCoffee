package org.j2c.assembly.rules

import javassist.bytecode.Opcode
import org.j2c.assembly.NCall
import org.j2c.assembly.NStaticCall
import org.j2c.assembly.Node
import org.j2c.assembly.findNClassByFullName
import org.j2c.getargc

@RuleContainer
object INVOKE_normal {
    val INVOKESTATIC = Rule(Opcode.INVOKESTATIC) { instructions, pos, const, _, stack ->
        val i = instructions.u16bitAt(pos + 1)
        val method = (findNClassByFullName(const.getMethodrefClassName(i))?.cname ?: "???"
                ) + "_" + const.getMethodrefName(i)
        val desc = const.getMethodrefType(i)

        val argc = getargc(desc)
        val args = arrayOfNulls<Node>(argc)
        for (i in argc - 1 downTo 0) {
            args[i] = stack.pop()
        }

        stack.add(NStaticCall(method, args.toList() as Collection<Node>))
    }
    val INVOKEINTERFACE = Rule(Opcode.INVOKEINTERFACE) { instructions, pos, const, _, stack ->
        val i = instructions.u16bitAt(pos + 1)
        val method = (findNClassByFullName(const.getInterfaceMethodrefClassName(i))?.cname ?: "???"
                ) + "_" + const.getInterfaceMethodrefName(i)
        val desc = const.getInterfaceMethodrefType(i)

        val argc = getargc(desc)
        val args = arrayOfNulls<Node>(argc)
        for (i in argc - 1 downTo 0) {
            args[i] = stack.pop()
        }
        val obj = stack.pop()

        stack.add(NCall(obj, method, args.toList() as Collection<Node>))
    }
    val INVOKEVIRTUAL = Rule(Opcode.INVOKEVIRTUAL) { instructions, pos, const, _, stack ->
        val i = instructions.u16bitAt(pos + 1)
        val method = (findNClassByFullName(const.getMethodrefClassName(i))?.cname ?: "???"
                ) + "_" + const.getMethodrefName(i)
        val desc = const.getInterfaceMethodrefType(i)

        val argc = getargc(desc)
        val args = arrayOfNulls<Node>(argc)
        for (i in argc - 1 downTo 0) {
            args[i] = stack.pop()
        }
        val obj = stack.pop()

        stack.add(NCall(obj, method, args.toList() as Collection<Node>))
    }
}

@RuleContainer
object INVOKE_strange {
    val INVOKESPECIAL = Rule(Opcode.INVOKESPECIAL) { instructions, pos, const, _, stack ->
        val i = instructions.u16bitAt(pos + 1)
        val method = (findNClassByFullName(const.getMethodrefClassName(i))?.cname ?: "???"
                ) + "_" + const.getMethodrefName(i)
        val desc = const.getInterfaceMethodrefType(i)

        val argc = getargc(desc)
        val args = arrayOfNulls<Node>(argc)
        for (i in argc - 1 downTo 0) {
            args[i] = stack.pop()
        }
        val obj = stack.pop()

        stack.add(NCall(obj, method, args.toList() as Collection<Node>))
    }
    //val INVOKEDYNAMIC = Rule(Opcode.INVOKEDYNAMIC) {... TODO
}