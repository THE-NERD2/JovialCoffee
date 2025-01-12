package org.j2c.ast.rules.api

import org.j2c.parsing.ParsingState

@Target(AnnotationTarget.CLASS)
internal annotation class RuleContainer

@Target(AnnotationTarget.PROPERTY)
internal annotation class NoRule

data class Rule(val opcode: Int, val predicate: (ParsingState) -> Unit)