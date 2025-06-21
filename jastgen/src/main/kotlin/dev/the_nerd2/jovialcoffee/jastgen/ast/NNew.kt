package dev.the_nerd2.jovialcoffee.jastgen.ast

class NNew(val clazz: String): Node("NNew") {
    override fun toString() = "new $clazz"
}