package org.j2c.ast

import org.j2c.indentBlock

class NClass(val qualName: String, val name: String, addToClasses: Boolean = true): Node("NClass") {
    companion object {
        internal var lastId = -1
    }
    val id = ++lastId
    val fields = arrayListOf<NFieldDeclaration>()
    val methods = arrayListOf<NMethodDeclaration>()
    // These four are to be called from JNI
    fun numFields() = fields.size
    fun getField(index: Int) = fields[index]
    fun numMethods() = methods.size
    fun getMethod(index: Int) = methods[index]
    init {
        if(addToClasses) classes.add(this)
    }
    val cname get() = "$name$id"
    override fun toString(): String {
        var str = ""
        (fields + methods).forEach {
            str += "$it\n"
        }
        str = indentBlock(str)
        str = "$cname\n$str"
        return str
    }
}