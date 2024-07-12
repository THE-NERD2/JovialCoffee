package org.j2c.ast

import org.j2c.llvm.LLVM

abstract class Node {
    companion object {
        internal var lastId = -1
    }

    val id = ++lastId
    abstract fun codeGen()
}
// Primitive types put in Java to distinguish primitives from objects
class NClass(
    private val name: String,
    private val fields: ArrayList<NFieldDeclaration>,
    private val methods: ArrayList<NMethodDeclaration>
): Node() {
    inner class NFieldDeclaration(
        private val name: String,
        private val type: String
    ): Node() {
        override fun codeGen() = LLVM.declareVariable("${this@NClass.name + id}_$name", type)
    }
    inner class NMethodDeclaration(
        private val name: String,
        private val ret: String,
        private val args: Array<String>,
        private val body: Array<Node>
    ): Node() {
        override fun codeGen() = LLVM.declareMethod("${this@NClass.name + id}_$name", ret, args, body)
    }
    override fun codeGen() {
        fields.forEach(NFieldDeclaration::codeGen)
        methods.forEach(NMethodDeclaration::codeGen)
    }
}