package org.j2c.assembly

import org.j2c.parse

abstract class Node {
    companion object {
        internal var lastId = -1
    }

    val id = ++lastId
}
// Primitive types put in Java to distinguish primitives from objects
class NClass(val qualName: String, val name: String): Node() {
    val fields = arrayListOf<NFieldDeclaration>()
    val methods = arrayListOf<NMethodDeclaration>()
    inner class NFieldDeclaration(
        val name: String,
        val type: String
    ): Node() {
        init {
            this@NClass.fields.add(this)
        }
    }
    inner class NMethodDeclaration(
        val name: String,
        val ret: String,
        val args: Collection<String>,
        val body: Collection</*Node*/ String> // Change when ready for AST
    ): Node() {
        init {
            this@NClass.methods.add(this)
        }
    }
    init {
        classes.add(this)
    }
    val cname get() = "$name$id"
}
class NReference(val identifier: String): Node()
class NAssignment(val dest: NReference, val v: Node): Node()

private val classes = arrayListOf<NClass>()
fun findNClassByFullName(name: String): NClass? {
    val v = classes.find { name == it.qualName }
    return if(v == null) parse(name) else v
}
fun popNClass() {
    classes.removeLast()
    Node.lastId--
}
fun getClasses() = classes.clone() as ArrayList<NClass>