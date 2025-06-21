package dev.the_nerd2.jovialcoffee.jastgen.ast

class NAssignment(val dest: String, val v: Node): Node("NAssignment") {
    override fun toString() = "$dest = $v"
}