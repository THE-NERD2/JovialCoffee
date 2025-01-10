package org.j2c

import org.j2c.development.printAll
import kotlin.test.Test

class Tests {
    @Test
    fun DummyClass() {
        try {
            init("/home/payton/Coding/apis/libs/J2C/src/test/resources")
            parseAndRunForEachClass("org.j2c.DummyClass") { println(it.toString()) }
        } catch(_: NullPointerException) {
            init("/home/payton/IdeaProjects/J2C/src/test/resources")
            parseAndRunForEachClass("org.j2c.DummyClass") { println(it.toString()) }
        }
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