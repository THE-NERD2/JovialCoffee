package dev.the_nerd2.jovialcoffee.kna

import org.junit.jupiter.api.Test

class Tests {
    @Test
    fun test() {
        val lib = object {
            external fun add(a: Int, b: Int): Int
        }
        lib.initLibrary("test")
    }
}