package dev.the_nerd2.jovialcoffee.j2c.codegen

import dev.the_nerd2.jovialcoffee.jastgen.ast.NBoundAssignment
import dev.the_nerd2.jovialcoffee.jastgen.ast.NBoundReference
import dev.the_nerd2.jovialcoffee.jastgen.ast.NReference
import dev.the_nerd2.jovialcoffee.jastgen.ast.NReturn
import dev.the_nerd2.jovialcoffee.jastgen.ast.Node
import dev.the_nerd2.jovialcoffee.j2c.llvm.LLVM

class Codegen private constructor(): AutoCloseable {
    companion object {
        private var lastId = -1
        fun <R> useNew(block: Codegen.() -> R) = Codegen().use(block)
    }
    private val ids = mutableListOf<Int>()
    fun codegen(node: Node): Int {
        val id = ++lastId
        var addId = true
        when (node) {
            is NReference -> {
                LLVM.createReference(id, node.identifier)
            }
            is NBoundReference -> {
                useNew {
                    val objId = codegen(node.obj)
                    LLVM.createBoundReference(id, objId, node.className, node.field)
                }
            }
            is NBoundAssignment -> {
                useNew {
                    val objId = codegen(node.obj)
                    val valueId = codegen(node.v)
                    LLVM.createBoundAssignment(objId, node.className, node.field, valueId)
                }
                addId = false
            }
            is NReturn -> {
                LLVM.createVoidReturn()
                addId = false
            }
            else -> throw UnsupportedNodeException(node.astName)
        }
        if(addId) ids.add(id)
        return id
    }
    override fun close() = ids.forEach(LLVM::free)
}