package org.j2c.ast

class NValueReturn(val type: String, val v: Node): Node("NValueReturn") {
    override fun toString() = "return $v"
}