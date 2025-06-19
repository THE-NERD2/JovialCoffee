package dev.the_nerd2.jovialcoffee.j2c

import dev.the_nerd2.jovialcoffee.j2c.ast.classes
import dev.the_nerd2.jovialcoffee.j2c.ast.rules.api.NoRule
import dev.the_nerd2.jovialcoffee.j2c.ast.rules.api.Rule
import dev.the_nerd2.jovialcoffee.j2c.ast.rules.api.RuleContainer
import dev.the_nerd2.jovialcoffee.j2c.codegen.Codegen
import dev.the_nerd2.jovialcoffee.j2c.llvm.ClassData
import javassist.ClassPool
import dev.the_nerd2.jovialcoffee.j2c.llvm.LLVM
import dev.the_nerd2.jovialcoffee.j2c.llvm.MethodData
import dev.the_nerd2.jovialcoffee.j2c.parsing.parseAllRecursively
import org.reflections.Reflections
import org.reflections.scanners.Scanners
import org.reflections.util.ConfigurationBuilder
import org.reflections.util.FilterBuilder
import java.io.File
import java.net.URLClassLoader
import kotlin.collections.forEach
import kotlin.reflect.KProperty1
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.hasAnnotation

val rules = arrayListOf<Rule>()
lateinit var classLoader: URLClassLoader
val pool = ClassPool(ClassPool.getDefault())

fun init(path: String) {
    val reflections = Reflections(
        ConfigurationBuilder()
            .forPackages("dev.the_nerd2.jovialcoffee.j2c.ast.rules")
            .filterInputsBy(FilterBuilder().includePackage("dev.the_nerd2.jovialcoffee.j2c.ast.rules"))
            .setScanners(Scanners.SubTypes.filterResultsBy { true }) // Include object classes
    )
    val classes = reflections.getSubTypesOf(Any::class.java)
    classes.map { it.kotlin }.filter { it.hasAnnotation<RuleContainer>() }.forEach { clazz ->
        val properties = clazz.declaredMemberProperties
        properties.filter { !it.hasAnnotation<NoRule>() }.forEach { prop ->
            @Suppress("UNCHECKED_CAST")
            rules.add((prop as KProperty1<Any?, Rule>).get(clazz.objectInstance))
        }
    }

    classLoader = URLClassLoader(arrayOf(File(path).toURI().toURL()), ClassLoader.getSystemClassLoader())
    pool.appendClassPath(path)
}

fun main(args: Array<String>) {
    init(args[0])
    parseAllRecursively(args[1])
    LLVM.initCodegen()
    classes.forEach {
        LLVM.addClass(ClassData(it))
    }
    LLVM.createClasses()
    classes.forEach { clazz ->
        clazz.methods.forEach { method ->
            Codegen.Companion.useNew {
                LLVM.createMethod(MethodData(method))
                method.body.forEach(::codegen)
            }
        }
    }
    LLVM.emit()
}