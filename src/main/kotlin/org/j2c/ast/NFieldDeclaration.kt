package org.j2c.ast

class NFieldDeclaration(
    val clazz: NClass,
    val name: String,
    val type: String
): Node("NFieldDeclaration") {
    init {
        clazz.fields.add(this)
    }
    override fun toString() = "$type ${clazz.cname}_$name"
}