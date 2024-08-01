package org.j2c.assembly.rules

import javassist.bytecode.CodeIterator
import javassist.bytecode.ConstPool
import java.util.Stack

@Target(AnnotationTarget.CLASS)
internal annotation class RuleContainer

data class Rule(val opcode: Int, val predicate: (CodeIterator, Int, ConstPool, MutableMap<Int, String>, Stack<String>) -> Unit)