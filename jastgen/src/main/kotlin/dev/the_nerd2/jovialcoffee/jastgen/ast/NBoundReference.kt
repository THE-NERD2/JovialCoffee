package dev.the_nerd2.jovialcoffee.jastgen.ast

class NBoundReference(val className: String, val obj: Node, val field: String): Node("NBoundReference") {
    override fun toString() = "$obj.$field"
}