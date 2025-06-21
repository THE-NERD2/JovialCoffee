package dev.the_nerd2.jovialcoffee.jastgen

import dev.the_nerd2.jovialcoffee.jastgen.development.printAll
import kotlin.test.Test

class Tests {
    @Test
    fun DummyClass() {
        init("/home/payton/IdeaProjects/JovialCoffee/jastgen/src/test/resources")
        parseAndRunForEachClass("dev.the_nerd2.jovialcoffee.jastgen.DummyClass") { println(it.toString()) }
        printAll()
    }
    @Test
    fun DummyLoopClass() {
        init("/home/payton/IdeaProjects/JovialCoffee/jastgen/src/test/resources")
        parseAndRunForEachClass("dev.the_nerd2.jovialcoffee.jastgen.DummyLoopClass") { println(it.toString()) }
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