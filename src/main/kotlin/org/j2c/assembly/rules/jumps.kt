package org.j2c.assembly.rules

import javassist.bytecode.CodeIterator
import javassist.bytecode.Opcode
import java.util.EmptyStackException
import java.util.Stack

@RuleContainer
object GOTO { // GOTO is a little weird, needs its own group
    @NoRule val posStack = Stack<Int>()
    fun follow(instructions: CodeIterator, pos: Int, offset: Int) {
        posStack.add(pos)
        instructions.move(pos + offset)
    }
    fun endFollow(instructions: CodeIterator) {
        try {
            // Don't re-follow the GOTO! (3 because GOTO takes up more than 1)
            instructions.move(posStack.pop() + 3)
        } catch(_: EmptyStackException) {}
    }

    val GOTO = Rule(Opcode.GOTO) { instructions, pos, _, _, _ ->
        val i = instructions.s16bitAt(pos + 1)
        follow(instructions, pos, i)
    }
}

@RuleContainer
object JUMPS {
    val RETURN = Rule(Opcode.RETURN) { instructions, _, _, _, stack ->
        stack.add("return")
        GOTO.endFollow(instructions)
    }
    val ARETURN = Rule(Opcode.ARETURN) { instructions, _, _, _, stack ->
        val v = stack.pop()
        stack.add("return $v")
        GOTO.endFollow(instructions)
    }
    val IRETURN = Rule(Opcode.IRETURN) { instructions, _, _, _, stack ->
        val v = stack.pop()
        stack.add("return $v")
        GOTO.endFollow(instructions)
    }
    val ATHROW = Rule(Opcode.ATHROW) { instructions, _, _, _, stack ->
        val exception = stack.pop()
        stack.add("throw $exception")
        GOTO.endFollow(instructions)
    }
}