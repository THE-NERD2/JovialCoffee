package org.j2c

import javassist.ClassPool
import javassist.CtClass
import javassist.NotFoundException
import javassist.bytecode.Mnemonic
import javassist.bytecode.Opcode
import org.j2c.ast.NClass
import org.j2c.ast.findNClassByFullName
import org.j2c.ast.popNClass
import org.j2c.development.registerUnknownOpcode
import org.j2c.exceptions.UnknownOpcodeException
import org.j2c.llvm.LLVM
import java.io.File
import java.net.URLClassLoader
import java.util.*
import kotlin.reflect.*
import kotlin.reflect.jvm.javaField

@OptIn(ExperimentalStdlibApi::class)
fun parse(path: String, name: String): NClass? {
    try {
        val kclass: KClass<*>
        val nclass: NClass
        val ctclass: CtClass

        val classLoader = URLClassLoader(arrayOf(File(path).toURI().toURL()), ClassLoader.getSystemClassLoader())
        kclass = classLoader.loadClass(name).kotlin
        nclass = NClass(kclass.qualifiedName!!, kclass.simpleName!!)
        val pool = ClassPool()
        pool.appendClassPath(path)
        ctclass = pool.get(name)

        kclass.members.forEach {
            if(it is KProperty) {
                nclass.NFieldDeclaration(it.name, (it.javaField?.type ?: it.returnType.javaType).typeName)
            } else if(it is KFunction) {
                try {
                    // Process method code
                    val methodInfo = ctclass.getDeclaredMethod(it.name).methodInfo
                    val instructions = methodInfo.codeAttribute.iterator()
                    val const = methodInfo.constPool
                    val localVariables = mutableMapOf<Int, String>()
                    it.parameters.forEachIndexed { i: Int, v: KParameter -> localVariables.put(i, "param$i") }

                    val stack = Stack<String>()
                    while (instructions.hasNext()) {
                        val pos = instructions.next()
                        val opcode = instructions.byteAt(pos)
                        when(opcode) {
                            Opcode.ALOAD_0 -> {
                                stack.add(localVariables[0])
                            }

                            Opcode.GETFIELD -> {
                                val i = instructions.u16bitAt(pos + 1)
                                val obj = stack.pop()
                                // Non-null assertion is temporary. TODO: null results in search for class in class pool
                                val fld = findNClassByFullName(const.getFieldrefClassName(i))!!.cname + "_" + const.getFieldrefName(i)
                                stack.add("$obj.$fld")
                            }

                            Opcode.PUTFIELD -> {
                                val i = instructions.u16bitAt(pos + 1)
                                val newV = stack.pop()
                                val obj = stack.pop()
                                // Non-null assertion is temporary. TODO: null results in search for class in class pool
                                val fld = findNClassByFullName(const.getFieldrefClassName(i))!!.cname + "_" + const.getFieldrefName(i)
                                stack.add("$obj.$fld = $newV")
                            }

                            Opcode.RETURN -> {
                                stack.add("return")
                            }

                            else -> {
                                UnknownOpcodeException(Mnemonic.OPCODE[opcode]).printStackTrace()
                                registerUnknownOpcode(Mnemonic.OPCODE[opcode])
                            }
                        }
                    }
                    nclass.NMethodDeclaration(it.name, it.returnType.javaType.typeName, it.parameters.map { it.type.javaType.typeName }, stack.toList())
                } catch(_: NotFoundException) {}
            }
        }
        return nclass
    } catch(_: Exception) {
        popNClass()
        return null
    }
}
fun main(args: Array<String>) {
    val astRoot = parse(args[0], args[1])!!
    LLVM.createAST(astRoot)
    LLVM.codeGen()
}