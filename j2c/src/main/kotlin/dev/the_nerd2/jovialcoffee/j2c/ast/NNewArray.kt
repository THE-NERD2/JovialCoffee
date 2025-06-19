package dev.the_nerd2.jovialcoffee.j2c.ast

class NNewArray(val type: String, val length: Node): Node("NNewArray") {
    override fun toString() = "new $type[$length]"
}