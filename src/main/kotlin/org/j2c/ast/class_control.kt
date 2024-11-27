package org.j2c.ast

import org.j2c.parsing.isAlreadyParsed
import org.j2c.parsing.schedule

internal val classes = arrayListOf<NClass>()
fun findNClassByFullName(name: String): NClass {
    val v = classes.find { name == it.qualName }
    if(v == null) {
        if(!isAlreadyParsed(name)) schedule(name)
        return NClass(name, name.substring(name.lastIndexOf('.') + 1), false)
    } else return v
}
fun popNClass() = try {
    classes.removeLast()
} catch(_: NoSuchElementException) {}
fun clearNClasses() = classes.clear()