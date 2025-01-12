package org.j2c.ast

class NAssignment(val dest: String, val v: Node): Node("NAssignment") {
    override fun toString() = "$dest = $v"
}