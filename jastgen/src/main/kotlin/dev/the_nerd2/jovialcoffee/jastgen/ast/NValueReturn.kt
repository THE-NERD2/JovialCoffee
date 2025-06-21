package dev.the_nerd2.jovialcoffee.jastgen.ast

class NValueReturn(
    @Suppress("unused") val type: String, // Used in native code
    val v: Node
): Node("NValueReturn") {
    override fun toString() = "return $v"
}