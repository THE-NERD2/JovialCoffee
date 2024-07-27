package org.j2c

import org.j2c.ast.NClass
import org.j2c.ast.getClasses
import org.j2c.development.printAll
import kotlin.test.Test

class Tests {
    @Test
    fun DummyClass() {
        try {
            setPath("/home/payton/Coding/apis/libs/J2C/src/test/resources")
            parse("org.j2c.DummyClass")!!
        } catch(_: NullPointerException) {
            setPath("/home/payton/IdeaProjects/J2C/src/test/resources")
            parse("org.j2c.DummyClass")!!
        }
        getClasses().forEach(::printClass)
        printAll()
    }
    @Test
    fun EncryptedStorage() { // from Notebook repository
        setPath("/home/payton/IdeaProjects/Notebook/build/libs/Notebook-1.0-SNAPSHOT-all.jar")
        parse("org.notebook.EncryptedStorage")!!
        getClasses().forEach(::printClass)
        printAll()
    }
    @Test
    fun WorkingFile() {
        setPath("/home/payton/IdeaProjects/Notebook/build/libs/Notebook-1.0-SNAPSHOT-all.jar")
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