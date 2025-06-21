package dev.the_nerd2.jovialcoffee.jastgen.ast.rules.api

import dev.the_nerd2.jovialcoffee.jastgen.ParsingState

@Target(AnnotationTarget.CLASS)
internal annotation class RuleContainer

@Target(AnnotationTarget.PROPERTY)
internal annotation class NoRule

data class Rule(val opcode: Int, val predicate: (ParsingState) -> Unit)