package dev.the_nerd2.jovialcoffee.j2c

import dev.the_nerd2.jovialcoffee.j2c.codegen.Codegen
import dev.the_nerd2.jovialcoffee.j2c.llvm.ClassData
import dev.the_nerd2.jovialcoffee.j2c.llvm.LLVM
import dev.the_nerd2.jovialcoffee.j2c.llvm.MethodData
import dev.the_nerd2.jovialcoffee.jastgen.forEachClass
import dev.the_nerd2.jovialcoffee.jastgen.init
import dev.the_nerd2.jovialcoffee.jastgen.parseAllRecursively

fun main(args: Array<String>) {
    init(args[0])
    parseAllRecursively(args[1])
    LLVM.initCodegen()
    forEachClass {
        LLVM.addClass(ClassData(it))
    }
    LLVM.createClasses()
    forEachClass { clazz ->
        clazz.methods.forEach { method ->
            Codegen.Companion.useNew {
                LLVM.createMethod(MethodData(method))
                method.body.forEach(::codegen)
            }
        }
    }
    LLVM.emit()
}