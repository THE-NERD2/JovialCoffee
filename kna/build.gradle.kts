plugins {
    kotlin("jvm")
    java
}

group = "dev.the_nerd2.jovialcoffee"
version = "0.1.0-alpha"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
    implementation(kotlin("reflect"))
    implementation(files("native/build/libkna.so"))
}

tasks.test {
    systemProperty("java.library.path", file("native/build").absolutePath)
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(21)
}

tasks.compileKotlin {
    dependsOn(":kna:native:buildNative")
}