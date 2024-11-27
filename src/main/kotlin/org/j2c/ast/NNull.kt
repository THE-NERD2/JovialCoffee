package org.j2c.ast

// Primitive types put in Java to distinguish primitives from objects
class NNull: Node("NNull") {
    override fun toString() = "null"
}