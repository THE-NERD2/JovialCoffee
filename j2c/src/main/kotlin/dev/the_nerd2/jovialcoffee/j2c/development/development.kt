package dev.the_nerd2.jovialcoffee.j2c.development

private val unknownOpcodes = mutableSetOf<String>()
fun registerUnknownOpcode(mnemonic: String) = unknownOpcodes.add(mnemonic)
fun printAll() = println("Unknown opcodes encountered: $unknownOpcodes")