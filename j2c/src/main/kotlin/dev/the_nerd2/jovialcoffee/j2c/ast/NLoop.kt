package dev.the_nerd2.jovialcoffee.j2c.ast

import dev.the_nerd2.jovialcoffee.j2c.indentBlock

class NLoop(val condition: Node, val body: ArrayList<Node>): ControlFlowNode("NLoop") {
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