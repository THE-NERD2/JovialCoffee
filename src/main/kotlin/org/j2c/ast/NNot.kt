package org.j2c.ast

class NNot(val condition: Node): Node("NNot") {
    override fun toString() = "!$condition"
}