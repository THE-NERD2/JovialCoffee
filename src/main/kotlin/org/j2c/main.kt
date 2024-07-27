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

lateinit var classLoader: URLClassLoader
val pool = ClassPool()

@OptIn(ExperimentalStdlibApi::class)
fun parse(name: String): NClass? {
    try {
        val kclass: KClass<*>
        val nclass: NClass
        val ctclass: CtClass

        kclass = classLoader.loadClass(name).kotlin
        nclass = NClass(kclass.qualifiedName!!, kclass.simpleName!!)
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
                            Opcode.ALOAD_1 -> {
                                stack.add(localVariables[1])
                            }
                            Opcode.ALOAD_2 -> {
                                stack.add(localVariables[2])
                            }
                            Opcode.ASTORE_2 -> {
                                val newV = stack.pop()
                                localVariables[2] = "avar2"
                                stack.add("avar2 = $newV")
                            }
                            Opcode.ACONST_NULL -> {
                                stack.add("null")
                            }
                            Opcode.ILOAD_1 -> {
                                stack.add(localVariables[1])
                            }
                            Opcode.ISTORE_1 -> {
                                val newV = stack.pop()
                                localVariables[1] = "ivar1"
                                stack.add("ivar1 = $newV")
                            }
                            Opcode.ICONST_0 -> {
                                stack.add("0")
                            }
                            Opcode.ICONST_1 -> {
                                stack.add("1")
                            }
                            Opcode.BIPUSH -> {
                                val v = instructions.byteAt(pos + 1)
                                stack.add("$v")
                            }
                            Opcode.LDC -> {
                                val i = instructions.byteAt(pos + 1)
                                val v = const.getLdcValue(i)
                                stack.push(v.toString())
                            }
                            Opcode.NEW -> {
                                val i = instructions.u16bitAt(pos + 1)
                                val c = const.getClassInfo(i)
                                stack.add("new ${findNClassByFullName(c)?.cname ?: "???"}")
                            }

                            Opcode.IMUL -> {
                                val v2 = stack.pop()
                                val v1 = stack.pop()
                                stack.add("$v1 * $v2")
                            }
                            Opcode.IADD -> {
                                val v2 = stack.pop()
                                val v1 = stack.pop()
                                stack.add("$v1 + $v2")
                            }

                            Opcode.GETSTATIC -> {
                                val i = instructions.u16bitAt(pos + 1)
                                val fld = const.getFieldrefName(i)
                                stack.add(fld)
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
                            Opcode.INVOKESTATIC -> {
                                val i = instructions.u16bitAt(pos + 1)
                                val method = (findNClassByFullName(const.getMethodrefClassName(i))?.cname ?: "???"
                                    ) + "_" + const.getMethodrefName(i)
                                val desc = const.getMethodrefType(i)

                                val argc = getargc(desc)
                                val args = arrayOfNulls<String>(argc)
                                for (i in argc - 1 downTo 0) {
                                    args[i] = stack.pop()
                                }

                                val callStr = StringBuilder("$method(")
                                for (i in 0 ..< argc) {
                                    if (i > 0) {
                                        callStr.append(",")
                                    }
                                    callStr.append(args[i])
                                }
                                callStr.append(")")
                                stack.add(callStr.toString())
                            }
                            Opcode.INVOKEINTERFACE -> {
                                val i = instructions.u16bitAt(pos + 1)
                                val method = (findNClassByFullName(const.getInterfaceMethodrefClassName(i))?.cname ?: "???"
                                    ) + "_" + const.getInterfaceMethodrefName(i)
                                val desc = const.getInterfaceMethodrefType(i)

                                val argc = getargc(desc)
                                val args = arrayOfNulls<String>(argc)
                                for (i in argc - 1 downTo 0) {
                                    args[i] = stack.pop()
                                }
                                val obj = stack.pop()

                                val callStr = StringBuilder("$obj.$method(")
                                for (i in 0 ..< argc) {
                                    if (i > 0) {
                                        callStr.append(",")
                                    }
                                    callStr.append(args[i])
                                }
                                callStr.append(")")
                                stack.add(callStr.toString())
                            }
                            Opcode.INVOKESPECIAL -> {
                                val i = instructions.u16bitAt(pos + 1)
                                val method = (findNClassByFullName(const.getMethodrefClassName(i))?.cname ?: "???"
                                        ) + "_" + const.getMethodrefName(i)
                                val desc = const.getInterfaceMethodrefType(i)

                                val argc = getargc(desc)
                                val args = arrayOfNulls<String>(argc)
                                for (i in argc - 1 downTo 0) {
                                    args[i] = stack.pop()
                                }
                                val obj = stack.pop()

                                val callStr = StringBuilder("$obj.$method(")
                                for (i in 0 ..< argc) {
                                    if (i > 0) {
                                        callStr.append(",")
                                    }
                                    callStr.append(args[i])
                                }
                                callStr.append(")")
                                stack.add(callStr.toString())
                            }
                            Opcode.INVOKEVIRTUAL -> {
                                val i = instructions.u16bitAt(pos + 1)
                                val method = (findNClassByFullName(const.getMethodrefClassName(i))?.cname ?: "???"
                                        ) + "_" + const.getMethodrefName(i)
                                val desc = const.getInterfaceMethodrefType(i)

                                val argc = getargc(desc)
                                val args = arrayOfNulls<String>(argc)
                                for (i in argc - 1 downTo 0) {
                                    args[i] = stack.pop()
                                }
                                val obj = stack.pop()

                                val callStr = StringBuilder("$obj.$method(")
                                for (i in 0 ..< argc) {
                                    if (i > 0) {
                                        callStr.append(",")
                                    }
                                    callStr.append(args[i])
                                }
                                callStr.append(")")
                                stack.add(callStr.toString())
                            }
                            //Opcode.INVOKEDYNAMIC -> {... TODO

                            Opcode.DUP -> {
                                stack.add(stack.peek())
                            }
                            Opcode.IFEQ -> {
                                val v = stack.pop()
                                val i = instructions.s16bitAt(pos + 1)
                                stack.add("if($v == 0) goto ${pos + i}")
                            }
                            Opcode.IFNE -> {
                                val v = stack.pop()
                                val i = instructions.s16bitAt(pos + 1)
                                stack.add("if($v != 0) goto ${pos + i}")
                            }
                            Opcode.IF_ACMPEQ -> {
                                val i = instructions.s16bitAt(pos + 1)
                                val v2 = stack.pop()
                                val v1 = stack.pop()
                                stack.add("if($v1 == $v2) goto ${pos + i}")
                            }
                            Opcode.IF_ACMPNE -> {
                                val i = instructions.s16bitAt(pos + 1)
                                val v2 = stack.pop()
                                val v1 = stack.pop()
                                stack.add("if($v1 != $v2) goto ${pos + i}")
                            }
                            Opcode.RETURN -> {
                                stack.add("return")
                            }
                            Opcode.ARETURN -> {
                                val v = stack.pop()
                                stack.add("return $v")
                            }
                            Opcode.IRETURN -> {
                                val v = stack.pop()
                                stack.add("return $v")
                            }

                            Opcode.CHECKCAST -> {
                                // I think that this won't be necessary to implement.
                            } // TODO
                            Opcode.INSTANCEOF -> {
                                // I'm not sure how to do this. Reflection maybe?
                            } // TODO

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
fun setPath(path: String) {
    classLoader = URLClassLoader(arrayOf(File(path).toURI().toURL()), ClassLoader.getSystemClassLoader())
    pool.appendClassPath(path)
}
fun main(args: Array<String>) {
    setPath(args[0])
    val astRoot = parse(args[1])!!
    LLVM.createAST(astRoot)
    LLVM.codeGen()
}