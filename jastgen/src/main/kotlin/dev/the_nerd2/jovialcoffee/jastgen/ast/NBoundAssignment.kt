package dev.the_nerd2.jovialcoffee.jastgen.ast

class NBoundAssignment(val className: String, val obj: Node, val field: String, val v: Node): Node("NBoundAssignment") {
    override fun toString() = "$obj.$field = $v"
}