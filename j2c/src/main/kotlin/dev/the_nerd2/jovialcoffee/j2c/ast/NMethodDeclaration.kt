package dev.the_nerd2.jovialcoffee.j2c.ast

import dev.the_nerd2.jovialcoffee.j2c.indentBlock

class NMethodDeclaration(
    clazz: NClass,
    name: String,
    val ret: String,
    val args: ArrayList<String>,
    val body: ArrayList<Node>
): Node("NMethodDeclaration") {
    init {
        clazz.methods.add(this)
    }
    val cname = "${clazz.cname}_$name"
    override fun toString(): String {
        var str = ""
        body.forEach {
            str += "$it\n"
        }
        str = indentBlock(str)
        str = "$ret $cname(${args.joinToString()})\n$str"
        return str
    }
}