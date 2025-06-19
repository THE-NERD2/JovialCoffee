package dev.the_nerd2.jovialcoffee.j2c.ast

class NNew(val clazz: String): Node("NNew") {
    override fun toString() = "new $clazz"
}