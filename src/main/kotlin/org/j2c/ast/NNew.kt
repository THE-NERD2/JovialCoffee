package org.j2c.ast

class NNew(val clazz: String): Node("NNew") {
    override fun toString() = "new $clazz"
}