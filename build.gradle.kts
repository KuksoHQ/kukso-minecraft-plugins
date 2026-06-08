plugins {
    id("xyz.jpenilla.run-paper") version "2.3.1" apply false
}

tasks.register("printModules") {
    group = "help"
    description = "Prints Kukso Minecraft modules."
    doLast {
        subprojects.forEach { println("${it.path} -> ${it.projectDir}") }
    }
}
