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
    implementation("org.reflections:reflections:0.10.2")
    implementation("ch.qos.logback:logback-classic:1.5.13")
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(21)
}