plugins {
    id("com.kukso.minecraft.plugin")
    id("xyz.jpenilla.run-paper")
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.21.11-R0.1-SNAPSHOT")
    compileOnly(project(":lib"))
    compileOnly("org.jetbrains:annotations:24.0.1")
    compileOnly("me.clip:placeholderapi:2.11.6")
}
