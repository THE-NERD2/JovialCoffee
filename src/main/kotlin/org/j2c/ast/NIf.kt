package org.j2c.ast

import org.j2c.indentBlock

class NIf(val condition: Node): Node("NIf") {
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