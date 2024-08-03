package org.j2c.assembly.rules

import javassist.bytecode.CodeIterator
import javassist.bytecode.ConstPool
import org.j2c.assembly.Node
import java.util.Stack

@Target(AnnotationTarget.CLASS)
internal annotation class RuleContainer

@Target(AnnotationTarget.PROPERTY)
internal annotation class NoRule

data class Rule(val opcode: Int, val predicate: (CodeIterator, Int, ConstPool, MutableMap<Int, String>, Stack<Node>) -> Unit)