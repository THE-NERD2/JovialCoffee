package org.j2c.ast

class NBoundReference(val className: String, val obj: Node, val field: String): Node("NBoundReference") {
    override fun toString() = "$obj.$field"
}