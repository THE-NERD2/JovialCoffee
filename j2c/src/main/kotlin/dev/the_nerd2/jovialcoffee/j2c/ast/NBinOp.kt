package dev.the_nerd2.jovialcoffee.j2c.ast

class NBinOp(
    @Suppress("unused") val type: String, // Used in native code
    val op: String,
    val left: Node,
    val right: Node
): Node("NBinOp") {
    override fun toString() = "$left $op $right"
}