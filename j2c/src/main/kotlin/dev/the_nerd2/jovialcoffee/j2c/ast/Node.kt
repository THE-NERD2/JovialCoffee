package dev.the_nerd2.jovialcoffee.j2c.ast

abstract class Node(val astName: String) {
    abstract override fun toString(): String
}