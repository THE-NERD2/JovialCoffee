package org.j2c.ast.rules

import javassist.bytecode.CodeIterator
import javassist.bytecode.Opcode
import org.j2c.ast.*
import org.j2c.ast.rules.api.NoRule
import org.j2c.ast.rules.api.Rule
import org.j2c.ast.rules.api.RuleContainer
import java.util.EmptyStackException
import java.util.Stack

@RuleContainer
object GOTO { // GOTO is a little weird, needs its own group
    @NoRule
    val posStack = Stack<Int>()
    @NoRule
    val followingIfStack = Stack<Boolean>()
    fun follow(instructions: CodeIterator, pos: Int, offset: Int, isIf: Boolean = false) {
        posStack.add(pos)
        followingIfStack.add(isIf)
        instructions.move(pos + offset)
    }
    fun endFollow(instructions: CodeIterator, stack: Stack<Node>) {
        try {
            // Do not go back to GOTO, but go back to else branch
            var isIf = false
            while(!isIf && followingIfStack.size > 0) {
                isIf = followingIfStack.pop()
                if (isIf) {
                    // Don't re-follow the jump! (3 because jump takes up more than 1)
                    instructions.move(posStack.pop() + 3)
                    stack.add(NOther("else"))
                } else posStack.pop()
            }
        } catch(_: EmptyStackException) {}
    }

    val GOTO = Rule(Opcode.GOTO) { state ->
        val i = state.instructions.s16bitAt(state.pos + 1)
        follow(state.instructions, state.pos, i)
    }
}

@RuleContainer
object JUMPS {
    val RETURN = Rule(Opcode.RETURN) { state ->
        state.stack.add(NReturn())
        GOTO.endFollow(state.instructions, state.stack)
    }
    val ARETURN = Rule(Opcode.ARETURN) { state ->
        val v = state.stack.pop()
        state.stack.add(NValueReturn("a", v))
        GOTO.endFollow(state.instructions, state.stack)
    }
    val IRETURN = Rule(Opcode.IRETURN) { state ->
        val v = state.stack.pop()
        state.stack.add(NValueReturn("i", v))
        GOTO.endFollow(state.instructions, state.stack)
    }
    val LRETURN = Rule(Opcode.LRETURN) { state ->
        val v = state.stack.pop()
        state.stack.add(NValueReturn("l", v))
        GOTO.endFollow(state.instructions, state.stack)
    }
    val FRETURN = Rule(Opcode.FRETURN) { state ->
        val v = state.stack.pop()
        state.stack.add(NValueReturn("f", v))
        GOTO.endFollow(state.instructions, state.stack)
    }
    val DRETURN = Rule(Opcode.DRETURN) { state ->
        val v = state.stack.pop()
        state.stack.add(NValueReturn("d", v))
        GOTO.endFollow(state.instructions, state.stack)
    }
    val ATHROW = Rule(Opcode.ATHROW) { state ->
        val exception = state.stack.pop()
        state.stack.add(NAThrow(exception))
        GOTO.endFollow(state.instructions, state.stack)
    }
}