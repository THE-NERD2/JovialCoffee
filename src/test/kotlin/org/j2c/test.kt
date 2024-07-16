package org.j2c

import kotlin.test.Test

class AST {
    @Test
    fun main() {
        val clazz = parse("/home/payton/Coding/apis/libs/J2C/src/test/resources", "org.j2c.DummyClass")
        println(clazz.name + clazz.id)
        clazz.fields.forEach {
            println("${it.type} ${clazz.name}${clazz.id}_${it.name}")
        }
        clazz.methods.forEach {
            print("${it.ret} ${clazz.name}${clazz.id}_${it.name}(")
            it.args.forEach {
                print("${it},")
            }
            println(")")
        }
    }
}