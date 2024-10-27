package org.j2c.assembly

import org.j2c.indentBlock
import org.j2c.isAlreadyParsed
import org.j2c.schedule

abstract class Node(val astName: String) {
    abstract override fun toString(): String
}

// Primitive types put in Java to distinguish primitives from objects
class NNull: Node("NNull") {
    override fun toString() = "null"
}

class NClass(val qualName: String, val name: String, addToClasses: Boolean = true): Node("NClass") {
    companion object {
        internal var lastId = -1
    }
    val id = ++lastId
    val fields = arrayListOf<NFieldDeclaration>()
    val methods = arrayListOf<NMethodDeclaration>()
    // These four are to be called from JNI
    fun numFields() = fields.size
    fun getField(index: Int) = fields[index]
    fun numMethods() = methods.size
    fun getMethod(index: Int) = methods[index]
    init {
        if(addToClasses) classes.add(this)
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
class NFieldDeclaration(
    val clazz: NClass,
    val name: String,
    val type: String
): Node("NFieldDeclaration") {
    init {
        clazz.fields.add(this)
    }
    override fun toString() = "$type ${clazz.cname}_$name"
}
class NMethodDeclaration(
    val clazz: NClass,
    val name: String,
    val ret: String,
    val args: ArrayList<String>,
    val body: ArrayList<Node>
): Node("NMethodDeclaration") {
    init {
        clazz.methods.add(this)
    }
    // Next four for use in JNI
    fun numArgs() = args.size
    fun getArg(index: Int) = args[index]
    fun bodySize() = body.size
    fun getBodyElement(index: Int) = body[index]
    override fun toString(): String {
        var str = ""
        body.forEach {
            str += "$it\n"
        }
        str = indentBlock(str)
        str = "$ret ${clazz.cname}_$name(${args.joinToString()})\n$str"
        return str
    }
}
class NReference(val identifier: String): Node("NReference") {
    override fun toString() = identifier
}
class NAssignment(val dest: String, val v: Node): Node("NAssignment") {
    override fun toString() = "$dest = $v"
}
class NStaticReference(val field: String): Node("NStaticReference") {
    override fun toString() = field
}
class NStaticAssignment(val field: String, val v: Node): Node("NStaticAssignment") {
    override fun toString() = "$field = $v"
}
class NBoundReference(val obj: Node, val field: String): Node("NBoundReference") {
    override fun toString() = "$obj.$field"
}
class NBoundAssignment(val obj: Node, val field: String, val v: Node): Node("NBoundAssignment") {
    override fun toString() = "$obj.$field = $v"
}
class NStaticCall(val method: String, val args: ArrayList<Node>): Node("NStaticCall") {
    // These two for JNI use
    fun numArgs() = args.size
    fun getArg(index: Int) = args[index]
    override fun toString() = "$method(" + args.map { it.toString() }.joinToString() + ")"
}
class NCall(val obj: Node, val method: String, val args: ArrayList<Node>): Node("NCall") {
    // These two for JNI use
    fun numArgs() = args.size
    fun getArg(index: Int) = args[index]
    override fun toString() = "$obj.$method(" + args.map { it.toString() }.joinToString() + ")"
}

class NNew(val clazz: String): Node("NNew") {
    override fun toString() = "new $clazz"
}

sealed class NAdd(name: String, val left: Node, val right: Node): Node(name) {
    override fun toString() = "$left + $right"
}
sealed class NSub(name: String, val left: Node, val right: Node): Node(name) {
    override fun toString() = "$left - $right"
}
sealed class NMul(name: String, val left: Node, val right: Node): Node(name) {
    override fun toString() = "$left * $right"
}
sealed class NDiv(name: String, val left: Node, val right: Node): Node(name) {
    override fun toString() = "$left / $right"
}
sealed class NCmp(name: String, val left: Node, val right: Node): Node(name) {
    override fun toString() = "$left vs $right"
}
class NIAdd(left: Node, right: Node): NAdd("NIAdd", left, right)
class NIMul(left: Node, right: Node): NMul("NIMul", left, right)
class NIDiv(left: Node, right: Node): NDiv("NIDiv", left, right)
class NLAdd(left: Node, right: Node): NAdd("NLAdd", left, right)
class NLSub(left: Node, right: Node): NSub("NLSub", left, right)
class NLMul(left: Node, right: Node): NMul("NLMul", left, right)
class NLCmp(left: Node, right: Node): NCmp("NLCmp", left, right)
class NFMul(left: Node, right: Node): NMul("NFMul", left, right)
class NFDiv(left: Node, right: Node): NDiv("NFDiv", left, right)
class NDAdd(left: Node, right: Node): NAdd("NDAdd", left, right)
class NDDiv(left: Node, right: Node): NDiv("NDDiv", left, right)

class NArrayLength(val array: Node): Node("NArrayLength") {
    override fun toString() = "#$array"
}

class NReturn: Node("NReturn") {
    override fun toString() = "return"
}
sealed class NValueReturn(name: String, val v: Node): Node(name) {
    override fun toString() = "return $v"
}
class NAReturn(v: Node): NValueReturn("NAReturn", v)
class NIReturn(v: Node): NValueReturn("NIReturn", v)
class NLReturn(v: Node): NValueReturn("NLReturn", v)
class NFReturn(v: Node): NValueReturn("NFReturn", v)
class NDReturn(v: Node): NValueReturn("NDReturn", v)

class NAThrow(val v: Node): Node("NAThrow") {
    override fun toString() = "throw $v"
}

@Deprecated("Used only for work-in-progress nodes.")
class NOther(val str: String): Node("NOther") {
    override fun toString() = str
}

private val classes = arrayListOf<NClass>()
fun findNClassByFullName(name: String): NClass {
    val v = classes.find { name == it.qualName }
    if(v == null) {
        if(!isAlreadyParsed(name)) schedule(name)
        return NClass(name, name.substring(name.lastIndexOf('.') + 1), false)
    } else return v
}
fun popNClass() = try {
    classes.removeLast()
} catch(_: NoSuchElementException) {}
fun clearNClasses() = classes.clear()