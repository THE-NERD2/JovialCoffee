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
    fun DummyClass_main() {
        try {
            main(arrayOf("/home/payton/Coding/apis/libs/J2C/src/test/resources", "org.j2c.DummyClass"))
        } catch(_: NullPointerException) {
            main(arrayOf("/home/payton/IdeaProjects/J2C/src/test/resources", "org.j2c.DummyClass"))
        }
    }
    @Test
    fun EncryptedStorage() { // from Notebook repository
        init("/home/payton/IdeaProjects/Notebook/build/libs/Notebook-1.0-SNAPSHOT-all.jar")
        parseAndRunForEachClass("org.notebook.EncryptedStorage") { println(it.toString()) }
        printAll()
    }
    @Test
    fun EncryptedStorage_main() {
        main(arrayOf("/home/payton/IdeaProjects/Notebook/build/libs/Notebook-1.0-SNAPSHOT-all.jar", "org.notebook.EncryptedStorage"))
    }
    @Test
    fun WorkingFile() {
        init("/home/payton/IdeaProjects/Notebook/build/libs/Notebook-1.0-SNAPSHOT-all.jar")
        parseAndRunForEachClass("org.notebook.WorkingFile") { println(it.toString()) }
        printAll()
    }
    @Test
    fun WorkingFile_main() {
        main(arrayOf("/home/payton/IdeaProjects/Notebook/build/libs/Notebook-1.0-SNAPSHOT-all.jar", "org.notebook.WorkingFile"))
    }
}