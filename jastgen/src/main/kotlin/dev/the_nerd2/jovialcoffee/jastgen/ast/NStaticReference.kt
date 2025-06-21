package dev.the_nerd2.jovialcoffee.jastgen.ast

class NStaticReference(val field: String): Node("NStaticReference") {
    override fun toString() = field
}