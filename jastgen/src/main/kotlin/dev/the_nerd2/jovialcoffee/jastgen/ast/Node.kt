package dev.the_nerd2.jovialcoffee.jastgen.ast

abstract class Node(val astName: String) {
    abstract override fun toString(): String
}