package org.j2c.ast

class NReference(val identifier: String): Node("NReference") {
    override fun toString() = identifier
}