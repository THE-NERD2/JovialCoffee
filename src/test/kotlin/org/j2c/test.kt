package org.j2c

import org.j2c.ast.NClass
import kotlin.test.Test

class AST {
    @Test
    fun main() {
        var clazz: NClass
        try {
            clazz = parse("/home/payton/Coding/apis/libs/J2C/src/test/resources", "org.j2c.DummyClass")!!
        } catch(_: NullPointerException) {
            clazz = parse("/home/payton/IdeaProjects/J2C/src/test/resources", "org.j2c.DummyClass")!!
        }
        println(clazz.name + clazz.id)
        clazz.fields.forEach {
            println("\t${it.type} ${clazz.cname}_${it.name}")
        }
        clazz.methods.forEach {
            print("\t${it.ret} ${clazz.cname}_${it.name}(")
            it.args.forEach {
                print("${it},")
            }
            println(")")
            it.body.forEach {
                println("\t\t$it")
            }
        }
    }
}