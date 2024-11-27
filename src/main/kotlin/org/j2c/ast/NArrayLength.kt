package org.j2c.ast

class NArrayLength(val array: Node): Node("NArrayLength") {
    override fun toString() = "#$array"
}