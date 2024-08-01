package org.j2c.assembly.rules

import javassist.bytecode.Opcode

@RuleContainer
object CHECKCAST {
    val CHECKCAST = Rule(Opcode.CHECKCAST) { _, _, _, _, _ ->
        // I think that this won't be necessary to implement.
    } // TODO
}

@RuleContainer
object INSTANCEOF {
    val INSTANCEOF = Rule(Opcode.INSTANCEOF) { _, _, _, _, _ ->
        // I'm not sure how to do this. Reflection maybe?
    } // TODO
}