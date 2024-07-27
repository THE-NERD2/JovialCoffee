package org.j2c

import org.j2c.ast.NClass
import org.j2c.development.printAll
import kotlin.test.Test

class Tests {
    @Test
    fun DummyClass() {
        var clazz: NClass
        try {
            clazz = parse("/home/payton/Coding/apis/libs/J2C/src/test/resources", "org.j2c.DummyClass")!!
        } catch(_: NullPointerException) {
            clazz = parse("/home/payton/IdeaProjects/J2C/src/test/resources", "org.j2c.DummyClass")!!
        }
        printClass(clazz)
        printAll()
    }
    @Test
    fun EncryptedStorage() { // from Notebook repository
        val clazz = parse("/home/payton/IdeaProjects/Notebook/build/libs/Notebook-1.0-SNAPSHOT-all.jar", "org.notebook.EncryptedStorage")!!
        printClass(clazz)
        printAll()
    }
    @Test
    fun WorkingFile() {
        val clazz = parse("/home/payton/IdeaProjects/Notebook/build/libs/Notebook-1.0-SNAPSHOT-all.jar", "org.notebook.WorkingFile")!!
        printClass(clazz)
        printAll()
    }
    fun printClass(clazz: NClass) {
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