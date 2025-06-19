package dev.the_nerd2.jovialcoffee.j2c.ast

@Deprecated("Used only for work-in-progress nodes.")
class NOther(val str: String): Node("NOther") {
    override fun toString() = str
}