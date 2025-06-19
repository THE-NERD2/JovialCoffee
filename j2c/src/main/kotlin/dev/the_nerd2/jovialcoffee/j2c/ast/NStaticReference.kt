package dev.the_nerd2.jovialcoffee.j2c.ast

class NStaticReference(val field: String): Node("NStaticReference") {
    override fun toString() = field
}