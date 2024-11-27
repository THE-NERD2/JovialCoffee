package org.j2c.ast

class NBinOp(val type: String, val op: String, val left: Node, val right: Node): Node("NBinOp") {
    override fun toString() = "$left $op $right"
}