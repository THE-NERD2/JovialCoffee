package dev.the_nerd2.jovialcoffee.jastgen.ast

class NAThrow(val v: Node): Node("NAThrow") {
    override fun toString() = "throw $v"
}