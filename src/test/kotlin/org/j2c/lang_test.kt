package org.j2c

import org.j2c.assembly.rules.RuleContainer
import org.junit.jupiter.api.Test
import org.reflections.Reflections
import org.reflections.scanners.SubTypesScanner
import org.reflections.util.ConfigurationBuilder
import org.reflections.util.FilterBuilder
import kotlin.reflect.full.declaredMemberProperties

class LanguageTests {
    @Test
    fun getRules() {
        val reflections = Reflections(
            ConfigurationBuilder()
                .forPackages("org.j2c.assembly.rules")
                .filterInputsBy(FilterBuilder().includePackage("org.j2c.assembly.rules"))
                .setScanners(SubTypesScanner(false))
        )
        val classes = reflections.getSubTypesOf(Any::class.java)
        classes.filter { it.isAnnotationPresent(RuleContainer::class.java) }.forEach {
            val properties = it.kotlin.declaredMemberProperties
            properties.forEach {
                println(it.name)
            }
        }
    }
}