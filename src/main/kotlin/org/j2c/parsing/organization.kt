package org.j2c.parsing

import org.j2c.ast.NClass
import org.j2c.ast.clearNClasses

private val scheduled = mutableSetOf<String>()
fun schedule(name: String) = scheduled.add(name)

private val alreadyParsed = mutableSetOf<String>()
fun isAlreadyParsed(name: String) = alreadyParsed.contains(name)

fun beginProgress(name: String) = scheduled.remove(name)
fun finishedProgress(name: String) = alreadyParsed.add(name)


internal lateinit var state: ParsingState


fun parseAndRunForEachClass(firstClassName: String, predicate: (NClass) -> Unit) {
    schedule(firstClassName)
    while(scheduled.size > 0) {
        val currentScheduled = scheduled.toMutableSet()
        scheduled.clear()
        currentScheduled.forEach {
            val v = parse(it)
            if(v != null) predicate.invoke(v)
        }
        clearNClasses()
    }
}