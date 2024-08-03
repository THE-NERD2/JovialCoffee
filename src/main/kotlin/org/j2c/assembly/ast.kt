package org.j2c.assembly

import org.j2c.indentBlock
import org.j2c.parse

abstract class Node {
    companion object {
        internal var lastId = -1
    }

    val id = ++lastId
    abstract override fun toString(): String
}

// Primitive types put in Java to distinguish primitives from objects
class NNull: Node() {
    override fun toString() = "null"
}

class NClass(val qualName: String, val name: String): Node() {
    val fields = arrayListOf<NFieldDeclaration>()
    val methods = arrayListOf<NMethodDeclaration>()
    inner class NFieldDeclaration(
        val name: String,
        val type: String
    ): Node() {
        init {
            fields.add(this)
        }
        val clazz get() = this@NClass
        override fun toString() = "$type ${cname}_$name"
    }
    inner class NMethodDeclaration(
        val name: String,
        val ret: String,
        val args: Collection<String>,
        val body: Collection<Node>
    ): Node() {
        init {
            methods.add(this)
        }
        val clazz get() = this@NClass
        override fun toString(): String {
            var str = ""
            body.forEach {
                str += "$it\n"
            }
            str = indentBlock(str)
            str = "$ret ${cname}_$name\n$str"
            return str
        }
    }
    init {
        classes.add(this)
    }
    val cname get() = "$name$id"
    override fun toString(): String {
        var str = ""
        (fields + methods).forEach {
            str += "$it\n"
        }
        str = indentBlock(str)
        str = "$cname\n$str"
        return str
    }
}
class NReference(val identifier: String): Node() {
    override fun toString() = identifier
}
class NAssignment(val dest: String, val v: Node): Node() {
    override fun toString() = "$dest = $v"
}
class NStaticReference(val field: String): Node() {
    override fun toString() = field
}
class NBoundReference(val obj: Node, val field: String): Node() {
    override fun toString() = "$obj.$field"
}
class NBoundAssignment(val obj: Node, val field: String, val v: Node): Node() {
    override fun toString() = "$obj.$field = $v"
}
class NStaticCall(val method: String, val args: Collection<Node>): Node() {
    override fun toString() = "$method(" + args.map { it.toString() }.joinToString() + ")"
}
class NCall(val obj: Node, val method: String, val args: Collection<Node>): Node() {
    override fun toString() = "$obj.$method(" + args.map { it.toString() }.joinToString() + ")"
}

class NNew(val clazz: String): Node() {
    override fun toString() = "new $clazz"
}

sealed class NAdd(val left: Node, val right: Node): Node() {
    override fun toString() = "$left + $right"
}
sealed class NMul(val left: Node, val right: Node): Node() {
    override fun toString() = "$left * $right"
}
sealed class NCmp(val left: Node, val right: Node): Node() {
    override fun toString() = "$left vs $right"
}
class NIAdd(left: Node, right: Node): NAdd(left, right)
class NIMul(left: Node, right: Node): NMul(left, right)
class NLCmp(left: Node, right: Node): NCmp(left, right)

class NReturn: Node() {
    override fun toString() = "return"
}
sealed class NValueReturn(val v: Node): Node() {
    override fun toString() = "return $v"
}
class NAReturn(v: Node): NValueReturn(v)
class NIReturn(v: Node): NValueReturn(v)

class NAThrow(val v: Node): Node() {
    override fun toString() = "throw $v"
}

@Deprecated("Used only for work-in-progress nodes.")
class NOther(val str: String): Node() {
    override fun toString() = str
}

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