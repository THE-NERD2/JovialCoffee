package org.j2c.ast

class NBoundAssignment(val obj: Node, val field: String, val v: Node): Node("NBoundAssignment") {
    override fun toString() = "$obj.$field = $v"
}