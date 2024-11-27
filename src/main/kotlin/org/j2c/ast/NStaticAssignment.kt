package org.j2c.ast

class NStaticAssignment(val field: String, val v: Node): Node("NStaticAssignment") {
    override fun toString() = "$field = $v"
}