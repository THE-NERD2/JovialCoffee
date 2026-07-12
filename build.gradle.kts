plugins {
    kotlin("jvm") version "2.4.0"
    java
}

group = "dev.the_nerd2.jovialcoffee"
version = "0.1.0-alpha"

repositories {
    mavenCentral()
}

kotlin {
    jvmToolchain(25)
}