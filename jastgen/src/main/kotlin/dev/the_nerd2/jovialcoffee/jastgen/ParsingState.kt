package dev.the_nerd2.jovialcoffee.jastgen

import javassist.bytecode.CodeIterator
import javassist.bytecode.ConstPool
import javassist.bytecode.Mnemonic
import dev.the_nerd2.jovialcoffee.jastgen.ast.NIf

class ParsingState(
    val instructions: CodeIterator,
    val const: ConstPool,
    val vars: MutableMap<Int, String>,
    val stack: RetargetableCodeStack
) {
    var pos = 0
    fun getNextIfStatementFrom(pos: Int): NIf {
        var ret: NIf? = null
        instructions.move(pos)

        while(ret == null) {
            val currentPos = instructions.next()
            val opcode = instructions.byteAt(currentPos)
            val mnemonic = Mnemonic.OPCODE[opcode]
            if(mnemonic.startsWith("if")) {
                try {
                    ret = stack.getElements().find { it is NIf && it.pos == currentPos } as NIf
                } catch(_: NullPointerException) {
                    // The element in question must be inside another if statement.
                    // I believe that it has to be in the else block of an if statement. If not, fix this later.
                    stack.getElements().forEach {
                        if(it is NIf) {
                            ret = it.elseBranch.find { it is NIf && it.pos == currentPos } as NIf
                        }
                    }
                }
            }
        }

        instructions.move(this.pos)
        return ret
    }
}