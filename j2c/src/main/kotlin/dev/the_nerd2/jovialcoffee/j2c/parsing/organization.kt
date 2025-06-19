package dev.the_nerd2.jovialcoffee.j2c.parsing

import dev.the_nerd2.jovialcoffee.j2c.ast.NClass
import dev.the_nerd2.jovialcoffee.j2c.ast.clearNClasses

private val scheduled = mutableSetOf<String>()
fun schedule(name: String) = scheduled.add(name)

private val alreadyParsed = mutableSetOf<String>()
fun isAlreadyParsed(name: String) = alreadyParsed.contains(name)

fun beginProgress(name: String) = scheduled.remove(name)
fun finishedProgress(name: String) = alreadyParsed.add(name)


internal lateinit var state: ParsingState


fun parseAndRunForEachClass(firstClassName: String, doClear: Boolean = true, predicate: (NClass) -> Unit) {
    schedule(firstClassName)
    while(scheduled.size > 0) {
        val currentScheduled = scheduled.toMutableSet()
        scheduled.clear()
        currentScheduled.forEach {
            val v = parse(it)
            if(v != null) predicate.invoke(v)
        }
        if(doClear) clearNClasses()
    }
}
fun parseAllRecursively(firstClassName: String) = parseAndRunForEachClass(firstClassName, false) {}