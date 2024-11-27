package org.j2c.ast

class NStaticReference(val field: String): Node("NStaticReference") {
    override fun toString() = field
}