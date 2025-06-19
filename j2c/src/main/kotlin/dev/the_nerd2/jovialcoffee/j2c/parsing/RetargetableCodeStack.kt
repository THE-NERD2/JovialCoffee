package dev.the_nerd2.jovialcoffee.j2c.parsing

import dev.the_nerd2.jovialcoffee.j2c.ast.ControlFlowNode
import dev.the_nerd2.jovialcoffee.j2c.ast.Node
import dev.the_nerd2.jovialcoffee.j2c.ast.NIf
import java.util.*

class RetargetableCodeStack {
    private val blocks = arrayListOf(arrayListOf<Node>())
    fun getElements() = blocks.flatten() as ArrayList<Node>
    fun numBlocks() = blocks.size
    fun enterBlock(block: ArrayList<Node>) = blocks.add(block)
    fun leaveBlock() = blocks.removeLast()
    fun getIfNodeInLastBlock() = blocks.last().last() as NIf
    fun getTopBlock() = blocks[0]
    fun add(element: Node) = blocks.last().add(element)
    fun pop(): Node { // This will try to get the last non-control-flow node in each block until it finds one
        @Suppress("UNCHECKED_CAST")
        val tempBlocks = blocks.map { it.clone() as ArrayList<Node> } as ArrayList<ArrayList<Node>>

        var blockIndex = blocks.size
        var nodeIndex: Int? = null
        var ret: Node? = null
        while(ret == null) {
            val block = tempBlocks.removeLast()
            nodeIndex = block.size
            do {
                ret = block.removeLastOrNull()
                nodeIndex--
            } while(ret is ControlFlowNode)
            blockIndex--
        }
        blocks[blockIndex].removeAt(nodeIndex!!)
        return ret
    }
    fun peek(): Node { // Does the same as pop()
        @Suppress("UNCHECKED_CAST")
        val tempBlocks = blocks.map { it.clone() as ArrayList<Node> } as ArrayList<ArrayList<Node>>

        var ret: Node? = null
        while(ret == null) {
            val block = tempBlocks.removeLast()
            do {
                ret = block.removeLastOrNull()
            } while(ret is ControlFlowNode)
        }
        return ret
    }
    fun deleteElement(element: Node) {
        try {
            var i = 0
            while (!blocks[i].remove(element)) {
                i++
            }
        } catch(_: IndexOutOfBoundsException) {
            // The element in question must be inside another if statement.
            // I believe that it has to be in the else block of an if statement. If not, fix this later.
            getElements().forEach {
                if (it is NIf && it.elseBranch.remove(element)) return
            }
        }
    }
}