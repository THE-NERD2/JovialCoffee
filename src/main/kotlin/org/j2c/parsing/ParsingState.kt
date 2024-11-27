package org.j2c.parsing

import javassist.bytecode.CodeIterator
import javassist.bytecode.ConstPool
import org.j2c.ast.Node
import java.util.*

data class ParsingState(
    val instructions: CodeIterator,
    val const: ConstPool,
    val vars: MutableMap<Int, String>,
    val stack: Stack<Node>
) {
    var pos = 0
}