package dev.the_nerd2.jovialcoffee.j2c.ast

import dev.the_nerd2.jovialcoffee.j2c.indentBlock

// pos, offset, and blockLevel are used internally
class NIf(val condition: Node, val pos: Int, val offset: Int, val blockLevel: Int): ControlFlowNode("NIf") {
    val ifBranch = arrayListOf<Node>()
    val elseBranch = arrayListOf<Node>() // Will remain empty if irrelevant
    override fun toString(): String {
        var str = "if($condition) {\n"
        var ifBlock = ""
        ifBranch.forEach {
            ifBlock += "$it\n"
        }
        str += "\t" + indentBlock(ifBlock).trim() + "\n}"
        if(elseBranch.size > 0) {
            var elseBlock = ""
            elseBranch.forEach {
                elseBlock += "$it\n"
            }
            str += " else {\n\t" + indentBlock(elseBlock).trim() + "\n}"
        }
        return str
    }
}