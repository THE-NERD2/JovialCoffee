package dev.the_nerd2.jovialcoffee.j2c

import dev.the_nerd2.jovialcoffee.j2c.development.printAll
import dev.the_nerd2.jovialcoffee.j2c.parsing.parseAndRunForEachClass
import kotlin.test.Test

class Tests {
    @Test
    fun DummyClass() {
        try {
            init("/home/payton/Coding/apis/libs/J2C/src/test/resources")
            parseAndRunForEachClass("dev.the_nerd2.jovialcoffee.j2c.DummyClass") { println(it.toString()) }
        } catch(_: NullPointerException) {
            init("/home/payton/IdeaProjects/J2C/src/test/resources")
            parseAndRunForEachClass("dev.the_nerd2.jovialcoffee.j2c.DummyClass") { println(it.toString()) }
        }
        printAll()
    }
    @Test
    fun DummyLoopClass() {
        init("/home/payton/IdeaProjects/J2C/src/test/resources")
        parseAndRunForEachClass("dev.the_nerd2.jovialcoffee.j2c.DummyLoopClass") { println(it.toString()) }
        printAll()
    }
    @Test
    fun EncryptedStorage() { // from Notebook repository
        init("/home/payton/IdeaProjects/Notebook/build/libs/Notebook-1.0-SNAPSHOT-all.jar")
        parseAndRunForEachClass("org.notebook.EncryptedStorage") { println(it.toString()) }
        printAll()
    }
    @Test
    fun WorkingFile() {
        init("/home/payton/IdeaProjects/Notebook/build/libs/Notebook-1.0-SNAPSHOT-all.jar")
        parseAndRunForEachClass("org.notebook.WorkingFile") { println(it.toString()) }
        printAll()
    }
}