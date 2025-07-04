package dev.the_nerd2.jovialcoffee.kna

import kotlin.reflect.KFunction

fun <T: Any> T.initLibrary(name: String) {
    Native.addLibrary(name)
    this::class.members.forEach {
        if(it is KFunction<*>) {
            if(it.isExternal) {
                Native.addFunction(this::class.qualifiedName, it.name)
            }
        }
    }
}