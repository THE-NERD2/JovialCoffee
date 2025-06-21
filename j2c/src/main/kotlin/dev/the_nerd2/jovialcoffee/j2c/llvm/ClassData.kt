package dev.the_nerd2.jovialcoffee.j2c.llvm

import dev.the_nerd2.jovialcoffee.jastgen.ast.NClass
import dev.the_nerd2.jovialcoffee.jastgen.ast.NFieldDeclaration
import dev.the_nerd2.jovialcoffee.jastgen.ast.findNClassByFullName

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