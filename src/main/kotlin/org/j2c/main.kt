package org.j2c

import javassist.ClassPool
import org.j2c.ast.classes
import org.j2c.ast.rules.api.NoRule
import org.j2c.ast.rules.api.Rule
import org.j2c.ast.rules.api.RuleContainer
import org.j2c.llvm.ClassData
import org.j2c.llvm.LLVM
import org.j2c.parsing.parseAllRecursively
import org.reflections.Reflections
import org.reflections.scanners.Scanners
import org.reflections.util.ConfigurationBuilder
import org.reflections.util.FilterBuilder
import java.io.File
import java.net.URLClassLoader
import kotlin.reflect.KProperty1
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.hasAnnotation

val rules = arrayListOf<Rule>()
lateinit var classLoader: URLClassLoader
val pool = ClassPool(ClassPool.getDefault())

fun init(path: String) {
    val reflections = Reflections(
        ConfigurationBuilder()
            .forPackages("org.j2c.ast.rules")
            .filterInputsBy(FilterBuilder().includePackage("org.j2c.ast.rules"))
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
    classes.forEach {
        // TODO: handle methods
    }
    LLVM.emit()
}