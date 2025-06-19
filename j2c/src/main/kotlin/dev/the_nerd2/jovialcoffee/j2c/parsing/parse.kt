package dev.the_nerd2.jovialcoffee.j2c.parsing

import javassist.NotFoundException
import javassist.bytecode.Mnemonic
import dev.the_nerd2.jovialcoffee.j2c.ast.NClass
import dev.the_nerd2.jovialcoffee.j2c.ast.NFieldDeclaration
import dev.the_nerd2.jovialcoffee.j2c.ast.NMethodDeclaration
import dev.the_nerd2.jovialcoffee.j2c.ast.popNClass
import dev.the_nerd2.jovialcoffee.j2c.classLoader
import dev.the_nerd2.jovialcoffee.j2c.development.registerUnknownOpcode
import dev.the_nerd2.jovialcoffee.j2c.pool
import dev.the_nerd2.jovialcoffee.j2c.rules
import kotlin.reflect.KFunction
import kotlin.reflect.KParameter
import kotlin.reflect.KProperty
import kotlin.reflect.javaType
import kotlin.reflect.jvm.internal.KotlinReflectionInternalError
import kotlin.reflect.jvm.javaField

private var keepParsingFunction = true
@OptIn(ExperimentalStdlibApi::class)
fun parse(name: String): NClass? {
    beginProgress(name)

    var nclass: NClass? = null
    try {
        val kclass = classLoader.loadClass(name).kotlin
        val ctclass = pool.get(name)
        nclass = NClass(kclass.qualifiedName!!, kclass.simpleName!!)

        kclass.members.forEach {
            if (it is KProperty) {
                NFieldDeclaration(nclass, it.name, (it.javaField?.type ?: it.returnType.javaType).typeName)
            } else if (it is KFunction) {
                try {
                    // Process method code
                    val methodInfo = ctclass.getDeclaredMethod(it.name).methodInfo
                    val instructions = methodInfo.codeAttribute.iterator()
                    val const = methodInfo.constPool
                    val vars = mutableMapOf<Int, String>()
                    it.parameters.forEachIndexed { i: Int, v: KParameter -> vars[i] = "param$i" }

                    val stack = RetargetableCodeStack()
                    state = ParsingState(instructions, const, vars, stack)
                    keepParsingFunction = true
                    while (instructions.hasNext() && keepParsingFunction) {
                        val pos = instructions.next()
                        val opcode = instructions.byteAt(pos)
                        state.pos = pos

                        try {
                            rules.find { it.opcode == opcode }?.predicate?.invoke(state)
                                ?: run {
                                    UnknownOpcodeException(Mnemonic.OPCODE[opcode]).printStackTrace()
                                    registerUnknownOpcode(Mnemonic.OPCODE[opcode])
                                }
                        } catch(e: InfiniteLoopException) {
                            e.printStackTrace()
                            break
                        }
                    }
                    NMethodDeclaration(
                        nclass,
                        it.name,
                        it.returnType.javaType.typeName,
                        ArrayList(it.parameters.map { it.type.javaType.typeName }),
                        stack.getTopBlock()
                    )
                } catch (_: NotFoundException) {
                }
            }
        }
    } catch(_: KotlinReflectionInternalError) {
        popNClass()
    } catch(_: Exception) {
        popNClass()
    } finally {
        finishedProgress(name)
    }
    return nclass
}