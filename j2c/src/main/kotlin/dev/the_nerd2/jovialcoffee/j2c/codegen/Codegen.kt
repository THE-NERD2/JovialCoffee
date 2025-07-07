package dev.the_nerd2.jovialcoffee.j2c.codegen

import dev.the_nerd2.jovialcoffee.j2c.llvm.LLVM
import dev.the_nerd2.jovialcoffee.jastgen.ast.*
import kotlin.reflect.full.declaredMemberExtensionFunctions

class Codegen private constructor(): AutoCloseable {
    companion object {
        private var lastId = -1
        fun useNew(block: Codegen.() -> Unit) = Codegen().use(block)
    }
    private val ids = mutableListOf<Int>()
    fun codegen(node: Node): Int {
        val id = ++lastId
        val addId = this::class.declaredMemberExtensionFunctions
            .find { it.name == "codegen${node.astName}" }?.call(this, node, id) as Boolean?
            ?: throw UnsupportedNodeException(node.astName)
        if(addId) ids.add(id)
        return id
    }
    override fun close() = ids.forEach(LLVM::free)
    // Codegen behavior for individual nodes
    @Suppress("unused")
    fun NReference.codegenNReference(id: Int): Boolean {
        LLVM.createReference(id, identifier)
        return true
    }
    @Suppress("unused")
    fun NBoundReference.codegenNBoundReference(id: Int): Boolean {
        useNew {
            val objId = codegen(obj)
            LLVM.createBoundReference(id, objId, className, field)
        }
        return true
    }
    @Suppress("unused")
    fun NBoundAssignment.codegenNBoundAssignment(id: Int): Boolean {
        useNew {
            val objId = codegen(obj)
            val valueId = codegen(v)
            LLVM.createBoundAssignment(objId, className, field, valueId)
        }
        return false
    }
    @Suppress("unused")
    fun NReturn.codegenNReturn(id: Int): Boolean {
        LLVM.createVoidReturn()
        return false
    }
}