package org.j2c

import javassist.ClassPool
import javassist.CtClass
import javassist.NotFoundException
import javassist.bytecode.Mnemonic
import org.j2c.ast.NClass
import java.io.File
import java.net.URLClassLoader
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.KProperty
import kotlin.reflect.jvm.javaField

fun parse(path: String, name: String): NClass {
    val clazz: KClass<*>
    val nclass: NClass
    val ctclass: CtClass

    val classLoader = URLClassLoader(arrayOf(File(path).toURI().toURL()), ClassLoader.getSystemClassLoader())
    clazz = classLoader.loadClass(name).kotlin
    nclass = NClass(clazz.simpleName!!)
    val pool = ClassPool()
    pool.appendClassPath(path)
    ctclass = pool.get(name)

    clazz.members.forEach {
        if(it is KProperty) {
            nclass.NFieldDeclaration(it.name, (it.javaField?.type ?: it.returnType).toString())
        } else if(it is KFunction) {
            try {
                // Process method code
                val instructions = ctclass.getDeclaredMethod(it.name).methodInfo.codeAttribute.iterator()
                while (instructions.hasNext()) {
                    val pos = instructions.next()
                    val opcode = instructions.byteAt(pos)
                    println(Mnemonic.OPCODE[opcode])
                    // TODO: create AST from opcodes
                }
                nclass.NMethodDeclaration(it.name, it.returnType.toString(), it.parameters.map { it.type.toString() })
            } catch(_: NotFoundException) {}
        }
    }
    return nclass
}
fun main(args: Array<String>) = parse(args[0], args[1]).codeGen()