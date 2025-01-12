package org.j2c.ast

import org.j2c.indentBlock

class NClass(val qualName: String, val name: String, addToClasses: Boolean = true): Node("NClass") {
    companion object {
        internal var lastId = 0
        internal val idDictionary = mutableMapOf<String, Int>()
    }
    val id: Int
    val fields = arrayListOf<NFieldDeclaration>()
    val methods = arrayListOf<NMethodDeclaration>()
    // These four are to be called from JNI
    fun numFields() = fields.size
    fun getField(index: Int) = fields[index]
    fun numMethods() = methods.size
    fun getMethod(index: Int) = methods[index]
    init {
        id = idDictionary[qualName] ?: lastId++
        if(addToClasses) classes.add(this)
        // Register the name for future NClasses
        idDictionary[qualName] = id
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