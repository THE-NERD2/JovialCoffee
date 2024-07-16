package org.j2c

import javassist.ClassPool
import javassist.CtClass
import javassist.NotFoundException
import javassist.bytecode.Mnemonic
import javassist.bytecode.Opcode
import org.j2c.ast.NClass
import org.j2c.exceptions.UnknownOpcodeException
import org.j2c.llvm.LLVM
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
                val methodInfo = ctclass.getDeclaredMethod(it.name).methodInfo
                val instructions = methodInfo.codeAttribute.iterator()
                val const = methodInfo.constPool

                val localVariables = mutableMapOf<Int, String>()
                // TODO

                while (instructions.hasNext()) {
                    val pos = instructions.next()
                    val opcode = instructions.byteAt(pos)
                    when(opcode) {
                        Opcode.ALOAD_0 -> {
                            // TODO
                        }
                        Opcode.GETFIELD -> {
                            // TODO
                        }
                        Opcode.PUTFIELD -> {
                            // TODO
                        }
                        Opcode.RETURN -> {
                            // TODO
                        }
                        else -> throw UnknownOpcodeException(Mnemonic.OPCODE[opcode])
                    }
                }
                nclass.NMethodDeclaration(it.name, it.returnType.toString(), it.parameters.map { it.type.toString() })
            } catch(_: NotFoundException) {}
        }
    }
    return nclass
}
fun main(args: Array<String>) {
    val astRoot = parse(args[0], args[1])
    LLVM.createAST(astRoot)
    LLVM.codeGen()
}