package org.j2c.parsing

import org.j2c.ast.Node
import org.j2c.ast.NIf
import java.util.*

class RetargetableCodeStack: Stack<Node>() {
    private val blocks = Stack<ArrayList<Node>>()
    fun enterBlock(block: ArrayList<Node>) = blocks.add(block)
    fun leaveBlock() = blocks.pop()
    fun getCurrentIfNode(): NIf {
        try {
            val blocksArray = blocks.toList()
            return blocksArray[blocksArray.size - 2].last() as NIf
        } catch(_: IndexOutOfBoundsException) {
            return super.peek() as NIf
        }
    }
    override fun add(element: Node) = blocks.lastOrNull()?.add(element) ?: super.add(element)
    override fun pop() = blocks.lastOrNull()?.removeLast() ?: super.pop()
    override fun peek() = blocks.lastOrNull()?.last() ?: super.peek()
}