package org.j2c.ast

import org.j2c.indentBlock

class NMethodDeclaration(
    val clazz: NClass,
    val name: String,
    val ret: String,
    val args: ArrayList<String>,
    val body: ArrayList<Node>
): Node("NMethodDeclaration") {
    init {
        clazz.methods.add(this)
    }
    // Next four for use in JNI
    fun numArgs() = args.size
    fun getArg(index: Int) = args[index]
    fun bodySize() = body.size
    fun getBodyElement(index: Int) = body[index]
    override fun toString(): String {
        var str = ""
        body.forEach {
            str += "$it\n"
        }
        str = indentBlock(str)
        str = "$ret ${clazz.cname}_$name(${args.joinToString()})\n$str"
        return str
    }
}