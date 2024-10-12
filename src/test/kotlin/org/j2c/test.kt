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
        getClasses().forEach { println(it.toString()) }
        printAll()
    }
    @Test
    fun DummyClass_main() {
        main(arrayOf("/home/payton/Coding/apis/libs/J2C/src/test/resources", "org.j2c.DummyClass"))
        if(getClasses().size == 0) {
            main(arrayOf("/home/payton/IdeaProjects/J2C/src/test/resources", "org.j2c.DummyClass"))
        }
    }
    @Test
    fun EncryptedStorage() { // from Notebook repository
        init("/home/payton/IdeaProjects/Notebook/build/libs/Notebook-1.0-SNAPSHOT-all.jar")
        parse("org.notebook.EncryptedStorage")!!
        getClasses().forEach { println(it.toString()) }
        printAll()
    }
    @Test
    fun EncryptedStorage_main() {
        main(arrayOf("/home/payton/IdeaProjects/Notebook/build/libs/Notebook-1.0-SNAPSHOT-all.jar", "org.notebook.EncryptedStorage"))
    }
    @Test
    fun WorkingFile() {
        init("/home/payton/IdeaProjects/Notebook/build/libs/Notebook-1.0-SNAPSHOT-all.jar")
        parse("org.notebook.WorkingFile")!!
        getClasses().forEach { println(it.toString()) }
        printAll()
    }
}