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
    implementation("org.javassist:javassist:3.30.2-GA")
    implementation("org.reflections:reflections:0.10.2")
    implementation(kotlin("reflect"))
    implementation("ch.qos.logback:logback-classic:1.5.13")
    implementation(files("j2c/native/build/libj2c.so"))
}

tasks.test {
    systemProperty("java.library.path", file("j2c/native/build").absolutePath)
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(21)
}

tasks.compileKotlin {
    dependsOn(":j2c:native:buildNative")
}