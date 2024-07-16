package org.j2c.ast

import org.j2c.llvm.LLVM
import kotlin.properties.Delegates

abstract class Node {
    companion object {
        internal var lastId = -1
    }

    val id = ++lastId
    abstract fun codeGen()
}
// Primitive types put in Java to distinguish primitives from objects
class NClass(val name: String): Node() {
    val fields = arrayListOf<NFieldDeclaration>()
    val methods = arrayListOf<NMethodDeclaration>()
    inner class NFieldDeclaration(
        val name: String,
        val type: String
    ): Node() {
        init {
            this@NClass.fields.add(this)
        }
        var bytecodeId by Delegates.notNull<Int>()
        override fun codeGen() = LLVM.declareVariable("${this@NClass.name + id}_$name", type)
    }
    inner class NMethodDeclaration(
        val name: String,
        val ret: String,
        val args: Collection<String>
    ): Node() {
        val body = arrayListOf<Node>()
        init {
            this@NClass.methods.add(this)
        }
        var bytecodeId by Delegates.notNull<Int>()
        override fun codeGen() = LLVM.declareMethod("${this@NClass.name + id}_$name", ret, args.toTypedArray(), body.toTypedArray())
    }
    override fun codeGen() {
        fields.forEach(NFieldDeclaration::codeGen)
        methods.forEach(NMethodDeclaration::codeGen)
    }
}