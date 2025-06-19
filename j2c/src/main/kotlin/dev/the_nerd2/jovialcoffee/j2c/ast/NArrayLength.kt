package dev.the_nerd2.jovialcoffee.j2c.ast

class NArrayLength(val array: Node): Node("NArrayLength") {
    override fun toString() = "#$array"
}