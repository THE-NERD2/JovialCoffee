package org.j2c

import org.j2c.ast.rules.api.NoRule
import org.j2c.ast.rules.api.RuleContainer
import org.junit.jupiter.api.Test
import org.reflections.Reflections
import org.reflections.scanners.SubTypesScanner
import org.reflections.util.ConfigurationBuilder
import org.reflections.util.FilterBuilder
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.hasAnnotation

class LanguageTests {
    @Test
    fun getRules() {
        val reflections = Reflections(
            ConfigurationBuilder()
                .forPackages("org.j2c.ast.rules")
                .filterInputsBy(FilterBuilder().includePackage("org.j2c.ast.rules"))
                .setScanners(SubTypesScanner(false))
        )
        val classes = reflections.getSubTypesOf(Any::class.java)
        classes.map { it.kotlin }.filter { it.hasAnnotation<RuleContainer>() }.forEach {
            val properties = it.declaredMemberProperties
            properties.filter { !it.hasAnnotation<NoRule>() }.forEach {
                println(it.name)
            }
        }
    }
}