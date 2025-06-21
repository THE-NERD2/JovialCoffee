@file:Suppress("unused")

package dev.the_nerd2.jovialcoffee.jastgen.ast.rules

import javassist.bytecode.Opcode
import dev.the_nerd2.jovialcoffee.jastgen.ast.NByte
import dev.the_nerd2.jovialcoffee.jastgen.ast.NNew
import dev.the_nerd2.jovialcoffee.jastgen.ast.NOther
import dev.the_nerd2.jovialcoffee.jastgen.ast.NShort
import dev.the_nerd2.jovialcoffee.jastgen.ast.findNClassByFullName
import dev.the_nerd2.jovialcoffee.jastgen.ast.rules.api.Rule
import dev.the_nerd2.jovialcoffee.jastgen.ast.rules.api.RuleContainer

@RuleContainer
object SIPUSH {
    val SIPUSH = Rule(Opcode.SIPUSH) { state ->
        val v = state.instructions.byteAt(state.pos + 1)
        state.stack.add(NShort(v.toShort()))
    }
}

@RuleContainer
object BIPUSH {
    val BIPUSH = Rule(Opcode.BIPUSH) { state ->
        val v = state.instructions.byteAt(state.pos + 1)
        state.stack.add(NByte(v.toByte()))
    }
}

@RuleContainer
object LDC {
    val LDC = Rule(Opcode.LDC) { state ->
        val i = state.instructions.byteAt(state.pos + 1)
        val v = state.const.getLdcValue(i)
        state.stack.add(NOther(v.toString()))
    }
}

@RuleContainer
object NEW {
    val NEW = Rule(Opcode.NEW) { state ->
        val i = state.instructions.u16bitAt(state.pos + 1)
        val c = state.const.getClassInfo(i)
        state.stack.add(NNew(findNClassByFullName(c).cname))
    }
}

@RuleContainer
object DUP {
    val DUP = Rule(Opcode.DUP) { state ->
        state.stack.add(state.stack.peek())
    }
    val DUP2 = Rule(Opcode.DUP2) { state ->
        val v = state.stack.peek()
        state.stack.add(v)
        state.stack.add(v)
    }
    val DUP_X1 = Rule(Opcode.DUP_X1) { state ->
        val v2 = state.stack.pop()
        val v1 = state.stack.pop()
        state.stack.add(v2)
        state.stack.add(v1)
        state.stack.add(v2)
    }
}

@RuleContainer
object POP {
    val POP = Rule(Opcode.POP) { state ->
        state.stack.pop()
    }
}

@RuleContainer
object NOP {
    val NOP = Rule(Opcode.NOP) { state -> }
}