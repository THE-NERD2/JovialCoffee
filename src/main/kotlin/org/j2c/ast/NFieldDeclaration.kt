package org.j2c.ast

class NFieldDeclaration(
    val clazz: NClass,
    val name: String,
    val type: String
): Node("NFieldDeclaration") {
    init {
        if(type !in listOf("boolean", "byte", "char", "short", "int", "long", "float", "double"))
            findNClassByFullName(type) // Force parsing to ensure this type exists
        clazz.fields.add(this)
    }
    override fun toString() = "$type ${clazz.cname}_$name"
}