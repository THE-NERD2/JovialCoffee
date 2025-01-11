package org.j2c.parsing

import javassist.bytecode.CodeIterator
import javassist.bytecode.ConstPool
import javassist.bytecode.Mnemonic
import org.j2c.ast.NIf

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
                ret = stack.getElements().find { it is NIf && it.pos == currentPos } as NIf
            }
        }

        instructions.move(this.pos)
        return ret
    }
}