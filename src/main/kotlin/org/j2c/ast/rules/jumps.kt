package org.j2c.ast.rules

import javassist.bytecode.Opcode
import org.j2c.ast.*
import org.j2c.ast.rules.api.NoRule
import org.j2c.ast.rules.api.Rule
import org.j2c.ast.rules.api.RuleContainer
import org.j2c.parsing.ParsingState
import org.j2c.parsing.stopParsingFunction
import java.util.*

@RuleContainer
object GOTO { // GOTO is a little weird, needs its own group
    enum class FollowType {
        GOTO, IF, ELSE
    }
    data class Follow(val type: FollowType, val pos: Int?)

    @NoRule
    val followStack = Stack<Follow>()
    fun follow(state: ParsingState, offset: Int, ifCondition: Node? = null) {
        if(offset < 0) { // Loop construct
            // This means that a GOTO was reached at the end of a loop. That loop was actually an if statement that
            // recursively calls itself. All we need to do is convert that if statement into a loop node
            if(endFollow(state)) { // No if statement can cross a loop boundary
                val ifNode = state.getNextIfStatementFrom(state.pos + offset)
                state.stack.deleteElement(ifNode)
                val loopNode = NLoop(NNot(ifNode.condition), ifNode.elseBranch)
                state.stack.add(loopNode)
                ifNode.ifBranch.forEach(state.stack::add)
                stopParsingFunction() // The if body would have been parsed to completion already
            }
        } else {
            state.instructions.move(state.pos + offset)
            if (ifCondition != null) {
                followStack.add(Follow(FollowType.IF, state.pos))
                val node = NIf(ifCondition, state.pos)
                state.stack.add(node)
                state.stack.enterBlock(node.ifBranch)
            } else {
                followStack.add(Follow(FollowType.GOTO, state.pos))
            }
        }
    }
    fun endFollow(state: ParsingState): Boolean {
        // Do not go back to GOTO, but go back to else branch
        var type = FollowType.GOTO
        while(type != FollowType.IF && followStack.size > 0) {
            val follow = followStack.pop()
            type = follow.type
            when(type) {
                FollowType.IF -> {
                    // Don't re-follow the jump! (3 because jump takes up more than 1)
                    state.instructions.move(follow.pos!! + 3)
                    state.stack.leaveBlock()
                    state.stack.enterBlock(state.stack.getIfNodeInLastBlock().elseBranch)
                    followStack.add(Follow(FollowType.ELSE, null))
                    return false // Don't immediately remove the else block
                }
                FollowType.ELSE -> {
                    state.stack.leaveBlock()
                }
                FollowType.GOTO -> {}
            }
        }
        return true
    }

    val GOTO = Rule(Opcode.GOTO) { state ->
        val i = state.instructions.s16bitAt(state.pos + 1)
        follow(state, i)
    }
}

@RuleContainer
object JUMPS {
    val RETURN = Rule(Opcode.RETURN) { state ->
        state.stack.add(NReturn())
        GOTO.endFollow(state)
    }
    val ARETURN = Rule(Opcode.ARETURN) { state ->
        val v = state.stack.pop()
        state.stack.add(NValueReturn("a", v))
        GOTO.endFollow(state)
    }
    val IRETURN = Rule(Opcode.IRETURN) { state ->
        val v = state.stack.pop()
        state.stack.add(NValueReturn("i", v))
        GOTO.endFollow(state)
    }
    val LRETURN = Rule(Opcode.LRETURN) { state ->
        val v = state.stack.pop()
        state.stack.add(NValueReturn("l", v))
        GOTO.endFollow(state)
    }
    val FRETURN = Rule(Opcode.FRETURN) { state ->
        val v = state.stack.pop()
        state.stack.add(NValueReturn("f", v))
        GOTO.endFollow(state)
    }
    val DRETURN = Rule(Opcode.DRETURN) { state ->
        val v = state.stack.pop()
        state.stack.add(NValueReturn("d", v))
        GOTO.endFollow(state)
    }
    val ATHROW = Rule(Opcode.ATHROW) { state ->
        val exception = state.stack.pop()
        state.stack.add(NAThrow(exception))
        GOTO.endFollow(state)
    }
}