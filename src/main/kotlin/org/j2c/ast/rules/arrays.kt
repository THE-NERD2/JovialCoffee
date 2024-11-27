package org.j2c.ast.rules

import javassist.bytecode.Opcode
import org.j2c.ast.NArrayLength
import org.j2c.ast.rules.api.Rule
import org.j2c.ast.rules.api.RuleContainer

@RuleContainer
object ARRAYLENGTH {
    val ARRAYLENGTH = Rule(Opcode.ARRAYLENGTH) { state ->
        state.stack.add(NArrayLength(state.stack.pop()))
    }
}