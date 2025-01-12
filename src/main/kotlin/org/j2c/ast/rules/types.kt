package org.j2c.ast.rules

import javassist.bytecode.Opcode
import org.j2c.ast.rules.api.Rule
import org.j2c.ast.rules.api.RuleContainer

@RuleContainer
object CHECKCAST {
    val CHECKCAST = Rule(Opcode.CHECKCAST) { state ->
        // I think that this won't be necessary to implement.
    } // TODO
}

@RuleContainer
object INSTANCEOF {
    val INSTANCEOF = Rule(Opcode.INSTANCEOF) { state ->
        // I'm not sure how to do this. Reflection maybe?
    } // TODO
}