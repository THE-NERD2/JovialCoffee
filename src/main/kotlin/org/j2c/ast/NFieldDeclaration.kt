package org.j2c.ast

class NFieldDeclaration(
    clazz: NClass,
    val name: String,
    val type: String
): Node("NFieldDeclaration") {
    // Used in JNI
    @Suppress("unused")
    val cname = "${clazz.cname}_$name"
    init {
        if(type !in listOf("boolean", "byte", "char", "short", "int", "long", "float", "double"))
            findNClassByFullName(type) // Force parsing to ensure this type exists
        clazz.fields.add(this)
    }
    override fun toString() = "$type $cname"
}