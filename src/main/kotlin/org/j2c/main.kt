package org.j2c

import org.j2c.ast.NClass
import java.io.File
import java.net.URLClassLoader
import kotlin.reflect.KFunction
import kotlin.reflect.KProperty
import kotlin.reflect.jvm.javaField

fun parse(path: String, name: String): NClass {
    val classLoader = URLClassLoader(arrayOf(File(path).toURI().toURL()), ClassLoader.getSystemClassLoader())
    val clazz = classLoader.loadClass(name).kotlin

    val nclass = NClass(clazz.simpleName!!)
    clazz.members.forEach {
        if(it is KProperty) {
            nclass.NFieldDeclaration(it.name, (it.javaField?.type ?: it.returnType).toString())
        } else if(it is KFunction) {
            nclass.NMethodDeclaration(it.name, it.returnType.toString(), it.parameters.map { it.type.toString() })
        }
    }
    return nclass
}
fun main(args: Array<String>) = parse(args[0], args[1]).codeGen()