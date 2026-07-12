plugins {
    id("com.kukso.minecraft.plugin")
    id("xyz.jpenilla.run-paper")
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.21.11-R0.1-SNAPSHOT")
    // KuksoLib is a soft dependency: compiled against, but optional at runtime.
    // KuksoVotes runs fully standalone and only enhances when KuksoLib is present.
    compileOnly(project(":lib"))
}
