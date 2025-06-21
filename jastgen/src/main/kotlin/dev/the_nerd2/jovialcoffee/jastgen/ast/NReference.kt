package dev.the_nerd2.jovialcoffee.jastgen.ast

class NReference(val identifier: String): Node("NReference") {
    override fun toString() = identifier
}