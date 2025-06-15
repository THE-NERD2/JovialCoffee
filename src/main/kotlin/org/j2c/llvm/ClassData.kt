package org.j2c.llvm

import org.j2c.ast.NClass
import org.j2c.ast.NFieldDeclaration
import org.j2c.ast.findNClassByFullName

// This class stores information about a class for use in native code
@Suppress("unused")
class ClassData(clazz: NClass) {
    val name = clazz.cname
    val fields = clazz.fields.map {
        if(it.type in listOf("boolean", "byte", "char", "short", "int", "long", "float", "double"))
            it
        else
            NFieldDeclaration(
                NClass("", "", false), // Dummy NClass (we don't need this)
                it.name,
                findNClassByFullName(it.type).cname // cname instead of qualified name
            )
    }
    fun numFields() = fields.size
    fun getField(index: Int) = fields[index]
}