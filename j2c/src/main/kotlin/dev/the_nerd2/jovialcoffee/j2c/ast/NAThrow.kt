package dev.the_nerd2.jovialcoffee.j2c.ast

class NAThrow(val v: Node): Node("NAThrow") {
    override fun toString() = "throw $v"
}