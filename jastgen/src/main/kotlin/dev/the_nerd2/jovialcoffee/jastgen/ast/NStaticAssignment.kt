package dev.the_nerd2.jovialcoffee.jastgen.ast

class NStaticAssignment(val field: String, val v: Node): Node("NStaticAssignment") {
    override fun toString() = "$field = $v"
}