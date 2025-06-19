package dev.the_nerd2.jovialcoffee.j2c.ast

class NArrayReference(val array: Node, val index: Node): Node("NArrayReference") {
    override fun toString() = "$array[$index]"
}