group = "dev.the_nerd2.jovialcoffee"
version = "0.1.0-alpha"

tasks.register<Exec>("prepareCMake") {
    workingDir("$projectDir/build")
    commandLine("cmake", "..")
}

tasks.register<Exec>("buildNative") {
    workingDir("$projectDir/build")
    commandLine("cmake", "--build", ".")
    dependsOn("prepareCMake")
}
