package org.j2c.ast

import org.j2c.indentBlock

class NLoop(val condition: Node, val body: ArrayList<Node>): Node("NLoop") {
    // Used in JNI
    fun bodySize() = body.size
    fun getBodyElement(index: Int) = body[index]
    override fun toString(): String {
        var str = "while($condition) {\n"
        var block = ""
        body.forEach {
            block += "$it\n"
        }
        str += "\t" + indentBlock(block).trim() + "\n}"
        return str
    }
}