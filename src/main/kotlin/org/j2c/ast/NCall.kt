package org.j2c.ast

class NCall(val obj: Node, val method: String, val args: ArrayList<Node>): Node("NCall") {
    // These two for JNI use
    fun numArgs() = args.size
    fun getArg(index: Int) = args[index]
    override fun toString() = "$obj.$method(" + args.map { it.toString() }.joinToString() + ")"
}