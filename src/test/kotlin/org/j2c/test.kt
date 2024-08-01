package org.j2c

import org.j2c.assembly.NClass
import org.j2c.assembly.getClasses
import org.j2c.development.printAll
import kotlin.test.Test

class Tests {
    @Test
    fun DummyClass() {
        try {
            init("/home/payton/Coding/apis/libs/J2C/src/test/resources")
            parse("org.j2c.DummyClass")!!
        } catch(_: NullPointerException) {
            init("/home/payton/IdeaProjects/J2C/src/test/resources")
            parse("org.j2c.DummyClass")!!
        }
        getClasses().forEach(::printClass)
        printAll()
    }
    @Test
    fun EncryptedStorage() { // from Notebook repository
        init("/home/payton/IdeaProjects/Notebook/build/libs/Notebook-1.0-SNAPSHOT-all.jar")
        parse("org.notebook.EncryptedStorage")!!
        getClasses().forEach(::printClass)
        printAll()
    }
    @Test
    fun WorkingFile() {
        init("/home/payton/IdeaProjects/Notebook/build/libs/Notebook-1.0-SNAPSHOT-all.jar")
        parse("org.notebook.WorkingFile")!!
        getClasses().forEach(::printClass)
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