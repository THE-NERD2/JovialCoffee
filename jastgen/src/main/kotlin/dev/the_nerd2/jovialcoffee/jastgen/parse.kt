package dev.the_nerd2.jovialcoffee.jastgen

import dev.the_nerd2.jovialcoffee.jastgen.development.registerUnknownOpcode
import dev.the_nerd2.jovialcoffee.jastgen.ast.NClass
import dev.the_nerd2.jovialcoffee.jastgen.ast.NFieldDeclaration
import dev.the_nerd2.jovialcoffee.jastgen.ast.NMethodDeclaration
import dev.the_nerd2.jovialcoffee.jastgen.ast.popNClass
import dev.the_nerd2.jovialcoffee.jastgen.ast.rules.api.NoRule
import dev.the_nerd2.jovialcoffee.jastgen.ast.rules.api.Rule
import dev.the_nerd2.jovialcoffee.jastgen.ast.rules.api.RuleContainer
import io.github.classgraph.ClassGraph
import javassist.ClassPool
import javassist.NotFoundException
import javassist.bytecode.Mnemonic
import java.io.File
import java.net.URLClassLoader
import kotlin.reflect.*
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.jvm.internal.KotlinReflectionInternalError
import kotlin.reflect.jvm.javaField

val rules = arrayListOf<Rule>()
lateinit var classLoader: URLClassLoader
val pool = ClassPool(ClassPool.getDefault())

fun init(path: String) {
    // Gets all rules for opcode parsing and stores them
    ClassGraph()
        .enableClassInfo()
        .acceptPackages("dev.the_nerd2.jovialcoffee.jastgen.ast.rules")
        .scan()
        .use {
            it.allClasses
                .map { Class.forName(it.name).kotlin }
                .filter { it.hasAnnotation<RuleContainer>() }
        }
        .forEach { clazz ->
            val properties = clazz.declaredMemberProperties
            properties.filter { !it.hasAnnotation<NoRule>() }.forEach { prop ->
                @Suppress("UNCHECKED_CAST")
                rules.add((prop as KProperty1<Any?, Rule>).get(clazz.objectInstance))
            }
        }
    // Prepares class loader and class pool
    classLoader = URLClassLoader(arrayOf(File(path).toURI().toURL()), ClassLoader.getSystemClassLoader())
    pool.appendClassPath(path)
}

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
                    it.parameters.forEachIndexed { i, _ -> vars[i] = "param$i" }

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