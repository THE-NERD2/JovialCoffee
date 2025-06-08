package org.j2c.llvm

import org.j2c.ast.NMethodDeclaration
import org.j2c.ast.findNClassByFullName

// This class stores information about a method for use in native code
@Suppress("unused")
class MethodData(method: NMethodDeclaration) {
    val name = method.cname
    val ret =
        if(method.ret in listOf("void", "boolean", "byte", "char", "short", "int", "long", "float", "double"))
            method.ret
        else
            findNClassByFullName(method.ret).cname
    val args = method.args.map {
        if(it in listOf("boolean", "byte", "char", "short", "int", "long", "float", "double"))
            it
        else
            findNClassByFullName(it).cname
    }
    fun numArgs() = args.size
    fun getArg(index: Int) = args[index]
}