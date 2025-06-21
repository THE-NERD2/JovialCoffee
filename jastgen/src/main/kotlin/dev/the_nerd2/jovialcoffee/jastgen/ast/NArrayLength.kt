package dev.the_nerd2.jovialcoffee.jastgen.ast

class NArrayLength(val array: Node): Node("NArrayLength") {
    override fun toString() = "#$array"
}