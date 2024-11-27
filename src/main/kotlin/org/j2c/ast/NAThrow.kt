package org.j2c.ast

class NAThrow(val v: Node): Node("NAThrow") {
    override fun toString() = "throw $v"
}