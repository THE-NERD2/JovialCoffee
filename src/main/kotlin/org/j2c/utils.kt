package org.j2c

fun getargc(desc: String): Int {
    var argc = 0
    var i = 1
    while (desc[i] != ')') {
        val c = desc[i]
        if (c == 'L') {
            while (desc[i] != ';') i++
        } else if (c == '[') {
            while (desc[i] == '[') i++
            if (desc[i] == 'L') {
                while (desc[i] != ';') i++
            }
        }
        argc++
        i++
    }
    return argc
}