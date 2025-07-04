plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
}

rootProject.name = "jovialcoffee"
include(":j2c:native")
include("j2c")

include("jastgen")
include("kna")
include("kna:native")