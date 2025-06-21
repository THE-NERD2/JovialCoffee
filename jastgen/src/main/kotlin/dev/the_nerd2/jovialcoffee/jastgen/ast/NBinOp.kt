package dev.the_nerd2.jovialcoffee.jastgen.ast

class NBinOp(
    @Suppress("unused") val type: String, // Used in native code
    val op: String,
    val left: Node,
    val right: Node
): Node("NBinOp") {
    override fun toString() = "$left $op $right"
}