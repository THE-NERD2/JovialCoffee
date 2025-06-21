package dev.the_nerd2.jovialcoffee.jastgen.ast

class NNot(val condition: Node): Node("NNot") {
    override fun toString() = "!($condition)"
}