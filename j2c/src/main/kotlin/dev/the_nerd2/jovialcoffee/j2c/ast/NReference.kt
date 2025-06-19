package dev.the_nerd2.jovialcoffee.j2c.ast

class NReference(val identifier: String): Node("NReference") {
    override fun toString() = identifier
}