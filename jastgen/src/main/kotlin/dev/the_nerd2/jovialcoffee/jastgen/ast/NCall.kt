package dev.the_nerd2.jovialcoffee.jastgen.ast

class NCall(val obj: Node, val method: String, val args: ArrayList<Node>): Node("NCall") {
    override fun toString() = "$obj.$method(" + args.joinToString() + ")"
}