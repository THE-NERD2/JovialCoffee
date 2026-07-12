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
}