plugins {
    kotlin("jvm")
}

group = "dev.the_nerd2.jovialcoffee"
version = "0.1.0-alpha"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
    implementation(kotlin("reflect"))
    implementation("org.javassist:javassist:3.30.2-GA")
    implementation("io.github.classgraph:classgraph:4.8.184")
}

tasks.test {
    useJUnitPlatform()
}