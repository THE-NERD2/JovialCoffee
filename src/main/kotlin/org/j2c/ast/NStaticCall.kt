package org.j2c.ast

class NStaticCall(val method: String, val args: ArrayList<Node>): Node("NStaticCall") {
    // These two for JNI use
    fun numArgs() = args.size
    fun getArg(index: Int) = args[index]
    override fun toString() = "$method(" + args.map { it.toString() }.joinToString() + ")"
}