plugins {
    id("com.kukso.minecraft.plugin")
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.21.11-R0.1-SNAPSHOT")
    compileOnly(project(":dialogs"))
}
