package org.j2c.ast

class NArrayAssignment(val array: Node, val index: Node, val v: Node): Node("NArrayAssignment") {
    override fun toString() = "$array[$index] = $v"
}