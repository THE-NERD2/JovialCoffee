package dev.the_nerd2.jovialcoffee.jastgen.ast

class NStaticCall(val method: String, val args: ArrayList<Node>): Node("NStaticCall") {
    override fun toString() = "$method(" + args.joinToString() + ")"
}