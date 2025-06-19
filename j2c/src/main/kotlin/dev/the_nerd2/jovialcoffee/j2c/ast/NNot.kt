package dev.the_nerd2.jovialcoffee.j2c.ast

class NNot(val condition: Node): Node("NNot") {
    override fun toString() = "!($condition)"
}