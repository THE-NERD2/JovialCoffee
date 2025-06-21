@file:Suppress("unused")

package dev.the_nerd2.jovialcoffee.jastgen.ast.rules

import javassist.bytecode.Opcode
import dev.the_nerd2.jovialcoffee.jastgen.ast.NArrayAssignment
import dev.the_nerd2.jovialcoffee.jastgen.ast.NArrayLength
import dev.the_nerd2.jovialcoffee.jastgen.ast.NArrayReference
import dev.the_nerd2.jovialcoffee.jastgen.ast.NNewArray
import dev.the_nerd2.jovialcoffee.jastgen.ast.rules.api.Rule
import dev.the_nerd2.jovialcoffee.jastgen.ast.rules.api.RuleContainer

val types = mapOf(4 to "boolean", 5 to "char", 6 to "float", 7 to "double", 8 to "byte", 9 to "short", 10 to "int", 11 to "long")

@RuleContainer
object NEWARRAY {
    val NEWARRAY = Rule(Opcode.NEWARRAY) { state ->
        val type = types[state.instructions.byteAt(state.pos + 1)]
        val length = state.stack.pop()
        state.stack.add(NNewArray(type ?: "???", length))
    }
}
@RuleContainer
object ARRAYLENGTH {
    val ARRAYLENGTH = Rule(Opcode.ARRAYLENGTH) { state ->
        state.stack.add(NArrayLength(state.stack.pop()))
    }
}
@RuleContainer
object _ALOAD {
    val AALOAD = Rule(Opcode.AALOAD) { state ->
        val index = state.stack.pop()
        val array = state.stack.pop()
        state.stack.add(NArrayReference(array, index))
    }
    val BALOAD = Rule(Opcode.BALOAD) { state ->
        val index = state.stack.pop()
        val array = state.stack.pop()
        state.stack.add(NArrayReference(array, index))
    }
    val CALOAD = Rule(Opcode.CALOAD) { state ->
        val index = state.stack.pop()
        val array = state.stack.pop()
        state.stack.add(NArrayReference(array, index))
    }
    val FALOAD = Rule(Opcode.FALOAD) { state ->
        val index = state.stack.pop()
        val array = state.stack.pop()
        state.stack.add(NArrayReference(array, index))
    }
    val IALOAD = Rule(Opcode.IALOAD) { state ->
        val index = state.stack.pop()
        val array = state.stack.pop()
        state.stack.add(NArrayReference(array, index))
    }
    val LALOAD = Rule(Opcode.LALOAD) { state ->
        val index = state.stack.pop()
        val array = state.stack.pop()
        state.stack.add(NArrayReference(array, index))
    }
}
@RuleContainer
object _ASTORE {
    val AASTORE = Rule(Opcode.AASTORE) { state ->
        val v = state.stack.pop()
        val index = state.stack.pop()
        val array = state.stack.pop()
        state.stack.add(NArrayAssignment(array, index, v))
    }
    val BASTORE = Rule(Opcode.BASTORE) { state ->
        val v = state.stack.pop()
        val index = state.stack.pop()
        val array = state.stack.pop()
        state.stack.add(NArrayAssignment(array, index, v))
    }
    val CASTORE = Rule(Opcode.CASTORE) { state ->
        val v = state.stack.pop()
        val index = state.stack.pop()
        val array = state.stack.pop()
        state.stack.add(NArrayAssignment(array, index, v))
    }
    val DASTORE = Rule(Opcode.DASTORE) { state ->
        val v = state.stack.pop()
        val index = state.stack.pop()
        val array = state.stack.pop()
        state.stack.add(NArrayAssignment(array, index, v))
    }
    val SASTORE = Rule(Opcode.SASTORE) { state ->
        val v = state.stack.pop()
        val index = state.stack.pop()
        val array = state.stack.pop()
        state.stack.add(NArrayAssignment(array, index, v))
    }
    val IASTORE = Rule(Opcode.IASTORE) { state ->
        val v = state.stack.pop()
        val index = state.stack.pop()
        val array = state.stack.pop()
        state.stack.add(NArrayAssignment(array, index, v))
    }
    val LASTORE = Rule(Opcode.LASTORE) { state ->
        val v = state.stack.pop()
        val index = state.stack.pop()
        val array = state.stack.pop()
        state.stack.add(NArrayAssignment(array, index, v))
    }
}